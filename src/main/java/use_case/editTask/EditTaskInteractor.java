package use_case.editTask;

import org.jetbrains.annotations.NotNull;

import data_access.WeatherApiService;
import entity.Task;
import entity.TaskInfo;
import entity.WeatherInfo;
import interface_adapter.addTask.Constants;
import use_case.weather.WeatherInfoGetter;

/**
 * Interactor for the editTask use case.
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
        System.out.println("[Interactor] Looking for task ID: " + updatedTask.getTaskInfo().getId());
        Task existingTask = dataAccess.getTaskById(username, updatedTask.getTaskInfo().getId());

        if (existingTask == null) {
            System.out.println("[Interactor] Task not found for ID: " + updatedTask.getTaskInfo().getId());
            presenter.prepareFailView("Task not found.");
        }
        else {

            System.out.println("[Interactor] Found existing task with ID: " + existingTask.getTaskInfo().getId());

            TaskInfo info = updatedTask.getTaskInfo();

            // Validation checks:
            if (info.getTaskName() == null || info.getTaskName().isEmpty()) {
                presenter.prepareFailView("Task name cannot be empty.");
            }
            else if (info.getTaskName().length() > Constants.CHAR_LIMIT) {
                presenter.prepareFailView("Task name exceeds character limit of " + Constants.CHAR_LIMIT);
            }
            else if (!info.getEndDateTime().isAfter(info.getStartDateTime())) {
                presenter.prepareFailView("End time must be after start time.");
            }
            else if (info.getTag() == null) {
                presenter.prepareFailView("Please select a category/tag.");
            }
            else {
                // Update weather info based on updated start time
                WeatherInfo weatherInfo = WeatherInfoGetter.getWeatherInfo(weatherApiService, info.getStartDateTime());

                Task taskToUpdate = getTask(info, weatherInfo);

                // Update in data access
                dataAccess.updateTask(username, taskToUpdate);

                EditTaskOutputData outputData = new EditTaskOutputData(
                        taskToUpdate.getTaskInfo().getId(),
                        false
                );
                presenter.prepareSuccessView(outputData);
            }
        }
    }

    @NotNull
    private static Task getTask(TaskInfo info, WeatherInfo weatherInfo) {
        TaskInfo updatedTaskInfo = new TaskInfo(
                info.getId(),
                info.getTaskName(),
                info.getStartDateTime(),
                info.getEndDateTime(),
                info.getPriority(),
                info.getTag(),
                info.getReminder(),
                info.getIsDeleted(),
                weatherInfo.description(),
                weatherInfo.iconName(),
                weatherInfo.temperature(),
                weatherInfo.uvIndex()
        );
        updatedTaskInfo.setTaskStatus(info.getTaskStatus());

        return new Task(updatedTaskInfo);
    }
}
