package use_case.addTask;

import data_access.LocationService;
import data_access.WeatherApiService;

import entity.Reminder;
import entity.Task;
import entity.TaskID;
import entity.TaskInfo;

import interface_adapter.addTask.Constants;
import interface_adapter.addTask.TaskIDGenerator;

import use_case.listTasks.TaskDataAccessInterface;
import use_case.notification.ScheduleNotificationInputData;
import use_case.notification.ScheduleNotificationInteractor;

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
    private final ScheduleNotificationInteractor notificationInteractor;

    public AddTaskInteractor(TaskDataAccessInterface dao, TaskIDGenerator taskIDGenerator,
                             AddTaskOutputBoundary addTaskPresenter, WeatherApiService weatherApiService,
                             ScheduleNotificationInteractor notificationInteractor) {
        this.dao = dao;
        this.taskIDGenerator = taskIDGenerator;
        this.addTaskPresenter = addTaskPresenter;
        this.weatherApiService = weatherApiService;
        this.notificationInteractor = notificationInteractor;
    }

    @Override
    public void execute(AddTaskInputData inputData, String username) {

        final AddTaskOutputData validationFailure = validateInput(inputData);

        if (validationFailure != null) {
            addTaskPresenter.prepareFailView(validationFailure);
        }
        else {
            processNewTaskCreation(inputData, username);
        }

    }

    private void processNewTaskCreation(AddTaskInputData inputData, String username) {
        final TaskID newID = taskIDGenerator.generateTaskID();

        final String description;
        final String feels;
        final String iconName;

        try {
            final LocalDateTime startDateTime = inputData.getStartDateTime();
            final LocalDate date = startDateTime.toLocalDate();
            final int hour = startDateTime.getHour();

            final List<Map<String, String>> hourly = weatherApiService.getHourlyWeather(
                    LocationService.getUserCity(), date, hour, hour);

            if (!hourly.isEmpty()) {
                final Map<String, String> hourlyMap = hourly.get(0);
                description = hourlyMap.get("description");
                feels = hourlyMap.get("feelslike");

                final Map<String, Object> daily = weatherApiService.getDailyWeather(
                        LocationService.getUserCity(), date);

                if (daily.get("iconName") != null) {
                    iconName = daily.get("iconName").toString();
                }
                else {
                    iconName = "";
                }
            }
            else {
                description = "";
                feels = "";
                iconName = "";
            }
        }
        catch (IOException ex) {
            System.err.println("Weather Lookup Failed: " + ex.getMessage());
            description = "";
            feels = "";
            iconName = "";
        }

        final String temp = feels;

        final TaskInfo newTaskInfo = new TaskInfo(newID, inputData.getTaskName(), inputData.getStartDateTime(),
                inputData.getEndDateTime(), inputData.getPriority(), inputData.getTag(),
                inputData.getReminder(), "No", description, iconName, temp, "");

        final Task newTask = new Task(newTaskInfo);

        dao.addTask(username, newTask);

        if (!inputData.getReminder().equals(Reminder.NONE)) {
            final ScheduleNotificationInputData notificationInput = new ScheduleNotificationInputData(
                    newTask.getTaskInfo().getId().toString(),
                    username,
                    inputData.getReminder()
            );
            notificationInteractor.scheduleNotification(notificationInput);
        }

        final AddTaskOutputData outputData = new AddTaskOutputData(newTask);
        addTaskPresenter.prepareSuccessView(outputData);
    }

    private AddTaskOutputData validateInput(AddTaskInputData inputData) {
        AddTaskOutputData error = null;

        if (inputData.getTaskName() == null || inputData.getTaskName().isEmpty()) {
            error = new AddTaskOutputData(AddTaskError.EMPTY_NAME);
        }
        else if (inputData.getTaskName().length() > Constants.CHAR_LIMIT) {
            error = new AddTaskOutputData(AddTaskError.EXCEEDS_CHAR_LIM);
        }
        else if (inputData.getStartDateTime() == null || inputData.getEndDateTime() == null) {
            error = new AddTaskOutputData(AddTaskError.EMPTY_TIME);
        }
        else if (!inputData.getEndDateTime().isAfter(inputData.getStartDateTime())) {
            error = new AddTaskOutputData(AddTaskError.START_AFTER_END);
        }

        return error;
    }
}
