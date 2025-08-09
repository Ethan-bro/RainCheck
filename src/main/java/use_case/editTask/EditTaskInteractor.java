package use_case.editTask;

import entity.Task;
import entity.TaskInfo;
import entity.WeatherInfo;

import use_case.weather.WeatherApiInterface;

import constants.Constants;

import org.jetbrains.annotations.NotNull;

/**
 * Interactor for the editTask use case.
 */
public class EditTaskInteractor implements EditTaskInputBoundary {

    private final EditTaskDataAccessInterface dataAccess;
    private final EditTaskOutputBoundary presenter;
    private final WeatherApiInterface weatherApiService;

    public EditTaskInteractor(EditTaskDataAccessInterface dataAccess,
                              EditTaskOutputBoundary presenter,
                              WeatherApiInterface weatherApiService) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.weatherApiService = weatherApiService;
    }

    @Override
    public void execute(String username, EditTaskInputData inputData) {
        final Task updatedTask = inputData.updatedTask();
        final TaskInfo taskInfo = updatedTask.getTaskInfo();
        final Task existingTask = dataAccess.getTaskById(username, taskInfo.getId());

        boolean failed = false;
        String failureMessage = "";

        if (existingTask == null) {
            failed = true;
            failureMessage = "Task not found.";
        }
        else {
            final String taskName = taskInfo.getTaskName();

            if (taskName == null || taskName.isEmpty()) {
                failed = true;
                failureMessage = "Task name cannot be empty.";
            }
            else if (taskName.length() > Constants.TASK_NAME_CHAR_LIMIT) {
                failed = true;
                failureMessage = "Task name exceeds character limit of " + Constants.TASK_NAME_CHAR_LIMIT;
            }
            else if (!taskInfo.getEndDateTime().isAfter(taskInfo.getStartDateTime())) {
                failed = true;
                failureMessage = "End time must be after start time.";
            }
            else if (taskInfo.getTag() == null) {
                failed = true;
                failureMessage = "Please select a category/tag.";
            }
        }

        if (failed) {
            presenter.prepareFailView(failureMessage);
        }
        else {
            final WeatherInfo weatherInfo = weatherApiService.getWeatherInfo(taskInfo.getStartDateTime());
            final Task taskToUpdate = getTask(taskInfo, weatherInfo);

            dataAccess.updateTask(username, taskToUpdate);

            final EditTaskOutputData outputData = new EditTaskOutputData(
                    taskToUpdate.getTaskInfo().getId(),
                    false
            );
            presenter.prepareSuccessView(outputData);
        }
    }

    @NotNull
    private static Task getTask(TaskInfo info, WeatherInfo weatherInfo) {
        final TaskInfo updatedTaskInfo = new TaskInfo();

        updatedTaskInfo.setCoreDetails(
                info.getId(),
                info.getTaskName(),
                info.getStartDateTime(),
                info.getEndDateTime()
        );

        updatedTaskInfo.setAdditionalDetails(
                info.getPriority(),
                info.getTag(),
                info.getReminder(),
                info.getIsDeleted()
        );

        updatedTaskInfo.setWeatherInfo(
                weatherInfo.description(),
                weatherInfo.iconName(),
                weatherInfo.temperature(),
                weatherInfo.uvIndex()
        );

        updatedTaskInfo.setTaskStatus(info.getTaskStatus());

        return new Task(updatedTaskInfo);
    }
}
