package use_case.addTask;

import data_access.LocationService;
import data_access.SupabaseTaskDataAccessObject;
import data_access.WeatherApiService;
import entity.Task;
import entity.TaskID;
import entity.TaskInfo;
import interface_adapter.addTask.Constants;
import interface_adapter.addTask.TaskIDGenerator;
import use_case.listTasks.TaskDataAccessInterface;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AddTaskInteractor implements AddTaskInputBoundary {

    private final TaskDataAccessInterface dao;
    private final AddTaskOutputBoundary addTaskPresenter;
    private final TaskIDGenerator taskIDGenerator;
    private final WeatherApiService weatherApiService;

    public AddTaskInteractor(TaskDataAccessInterface dao, TaskIDGenerator taskIDGenerator,
                             AddTaskOutputBoundary addTaskPresenter, WeatherApiService weatherApiService) {
        this.dao = dao;
        this.taskIDGenerator = taskIDGenerator;
        this.addTaskPresenter = addTaskPresenter;
        this.weatherApiService = weatherApiService;
    }

    @Override
    public void execute(AddTaskInputData inputData, String username){
        if (inputData.getTaskName() == null || inputData.getTaskName().isEmpty()){
            AddTaskOutputData failedOutput = new AddTaskOutputData(AddTaskError.EMPTY_NAME);
            addTaskPresenter.prepareFailView(failedOutput);
            return;
        }

        if (inputData.getTaskName().length() > Constants.CHAR_LIMIT){
            AddTaskOutputData failedOutput = new AddTaskOutputData(AddTaskError.EXCEEDS_CHAR_LIM);
            addTaskPresenter.prepareFailView(failedOutput);
            return;
        }

        if (inputData.getStartDateTime() == null || inputData.getEndDateTime() == null){
            AddTaskOutputData failedOutput = new AddTaskOutputData(AddTaskError.EMPTY_TIME);
            addTaskPresenter.prepareFailView(failedOutput);
            return;
        }

        if (!inputData.getEndDateTime().isAfter(inputData.getStartDateTime())){
            AddTaskOutputData failedOutput = new AddTaskOutputData(AddTaskError.START_AFTER_END);
            addTaskPresenter.prepareFailView(failedOutput);
            return;
        }

        TaskID newID = taskIDGenerator.generateTaskID();

        String description = "";
        String feels = "";
        String iconName = "";
        try {
            LocalDateTime startDateTime = inputData.getStartDateTime();
            LocalDate date = startDateTime.toLocalDate();
            int hour = startDateTime.getHour();

            List<Map<String,String>> hourly = weatherApiService.getHourlyWeather(LocationService.getUserCity(),
                    date, hour, hour);

            if (!hourly.isEmpty()){
                Map<String, String> hourlyMap = hourly.get(0);
                description = hourlyMap.get("description");
                feels = hourlyMap.get("feels");
                Map<String, Object> daily = weatherApiService.getDailyWeather(LocationService.getUserCity(),
                        date);
                iconName = daily.get("iconName") != null ? daily.get("iconName").toString() : "";
            }
        } catch (IOException e) {
            System.err.println("Weather Lookup Failed: " + e.getMessage());
        }
        String temp = feels;


        TaskInfo newTaskInfo = new TaskInfo(newID, inputData.getTaskName(), inputData.getStartDateTime(),
                inputData.getEndDateTime(), inputData.getPriority(), inputData.getTag(),
                inputData.getReminder(), description, iconName, temp
                );

        Task newTask = new Task(newTaskInfo);

        dao.addTask(username, newTask);

        AddTaskOutputData outputData = new AddTaskOutputData(newTask);
        addTaskPresenter.prepareSuccessView(outputData);
    }
}
