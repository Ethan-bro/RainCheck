package use_case.editTask;

import entity.Task;
import entity.TaskInfo;
import interface_adapter.addTask.Constants;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import data_access.LocationService;
import data_access.WeatherApiService;

/**
 * Interactor for the editTask use case
 */
public class EditTaskInteractor implements EditTaskInputBoundary {

    private final EditTaskDataAccessInterface dataAccess;
    private final EditTaskOutputBoundary presenter;
    private final WeatherApiService weatherApiService;

    public EditTaskInteractor(EditTaskDataAccessInterface dataAccess,
                              EditTaskOutputBoundary presenter,
                              WeatherApiService weatherApiService) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.weatherApiService = weatherApiService;
    }

    @Override
    public void execute(String username, EditTaskInputData inputData) {
        Task updatedTask = inputData.getUpdatedTask();
        Task existingTask = dataAccess.getTaskById(username, updatedTask.getTaskInfo().getId());

        if (existingTask == null) {
            presenter.prepareFailView("Task not found.");
            return;
        }

        TaskInfo info = updatedTask.getTaskInfo();

        // Validation checks:
        if (info.getTaskName() == null || info.getTaskName().isEmpty()) {
            presenter.prepareFailView("Task name cannot be empty.");
            return;
        }

        if (info.getTaskName().length() > Constants.CHAR_LIMIT) {
            presenter.prepareFailView("Task name exceeds character limit of " + Constants.CHAR_LIMIT);
            return;
        }

        if (info.getStartDateTime() == null || info.getEndDateTime() == null) {
            presenter.prepareFailView("Start and end time must be set.");
            return;
        }

        if (!info.getEndDateTime().isAfter(info.getStartDateTime())) {
            presenter.prepareFailView("End time must be after start time.");
            return;
        }

        if (info.getTag() == null) {
            presenter.prepareFailView("Please select a category/tag.");
            return;
        }

        // Update weather info based on updated start time
        String description = "";
        String feels = "";
        String iconName = "";

        try {
            LocalDateTime startDateTime = info.getStartDateTime();
            LocalDate date = startDateTime.toLocalDate();
            int hour = startDateTime.getHour();

            List<Map<String,String>> hourly = weatherApiService.getHourlyWeather(LocationService.getUserCity(),
                    date, hour, hour);

            if (!hourly.isEmpty()) {
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

        // Create a new TaskInfo with updated weather data but preserve ID and deletion flag and status
        TaskInfo updatedTaskInfo = new TaskInfo(
                info.getId(),
                info.getTaskName(),
                info.getStartDateTime(),
                info.getEndDateTime(),
                info.getPriority(),
                info.getTag(),
                info.getReminder(),
                info.getIsDeleted(),
                description,
                iconName,
                temp
        );
        updatedTaskInfo.setTaskStatus(info.getTaskStatus());

        Task taskToUpdate = new Task(updatedTaskInfo);

        // Update in data access
        dataAccess.updateTask(username, taskToUpdate);

        EditTaskOutputData outputData = new EditTaskOutputData(
                taskToUpdate.getTaskInfo().getId(),
                false
        );
        presenter.prepareSuccessView(outputData);
    }
}
