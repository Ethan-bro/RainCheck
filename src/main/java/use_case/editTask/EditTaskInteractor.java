package use_case.editTask;

import entity.Task;
import entity.TaskInfo;
import entity.WeatherInfo;
import interface_adapter.addTask.Constants;
import data_access.WeatherApiService;
import org.jetbrains.annotations.NotNull;
import use_case.weather.WeatherInfoGetter;

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
