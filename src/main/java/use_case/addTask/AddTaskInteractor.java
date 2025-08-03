package use_case.addTask;

import data_access.LocationService;
import data_access.SupabaseTaskDataAccessObject;
import data_access.WeatherApiService;
import entity.Task;
import entity.TaskID;
import entity.TaskInfo;
import entity.WeatherInfo;
import interface_adapter.addTask.Constants;
import interface_adapter.addTask.TaskIDGenerator;
import use_case.listTasks.TaskDataAccessInterface;
import use_case.weather.WeatherInfoGetter;

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

        if (inputData.getTag() == null) {
            AddTaskOutputData failedOutput = new AddTaskOutputData(AddTaskError.NO_TAG_SELECTED);
            addTaskPresenter.prepareFailView(failedOutput);
            return;
        }

        TaskID newID = taskIDGenerator.generateTaskID();

        WeatherInfo weatherInfo = WeatherInfoGetter.getWeatherInfo(weatherApiService, inputData.getStartDateTime());

        TaskInfo newTaskInfo = new TaskInfo(newID, inputData.getTaskName(), inputData.getStartDateTime(),
                inputData.getEndDateTime(), inputData.getPriority(), inputData.getTag(),
                inputData.getReminder(), inputData.getIsDeleted(),
                weatherInfo.description(), weatherInfo.iconName(), weatherInfo.temperature()
                );

        Task newTask = new Task(newTaskInfo);

        dao.addTask(username, newTask);

        AddTaskOutputData outputData = new AddTaskOutputData(newTask);
        addTaskPresenter.prepareSuccessView(outputData);
    }
}
