package interface_adapter.addTask;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;

import interface_adapter.ViewManagerModel;

import use_case.addTask.AddTaskInputBoundary;
import use_case.addTask.AddTaskInputData;

import java.time.LocalDateTime;

public class AddTaskController {

    private final AddTaskInputBoundary addTaskInteractor;
    private final ViewManagerModel viewManagerModel;
    private final AddTaskViewModel addTaskViewModel;

    public AddTaskController(AddTaskInputBoundary addTaskInteractor,
                             ViewManagerModel viewManagerModel,
                             AddTaskViewModel addTaskViewModel) {
        this.addTaskInteractor = addTaskInteractor;
        this.viewManagerModel = viewManagerModel;
        this.addTaskViewModel = addTaskViewModel;
    }

    /**
     * Executes the add task use case with the provided parameters.
     * If the username is null, logs an error and does not proceed.
     *
     * @param taskName the name of the task
     * @param startDateTime the start date and time of the task
     * @param endDateTime the end date and time of the task
     * @param priority the priority level of the task
     * @param customTag the custom tag associated with the task
     * @param reminder the reminder time before the task
     */
    public void execute(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                        Priority priority, CustomTag customTag, Reminder reminder) {

        final String username = addTaskViewModel.getUsername();

        if (username == null) {
            System.err.println("AddTaskController Error: Username is null. Aborting task creation.");
        }
        else {
            final AddTaskInputData inputData = new AddTaskInputData(taskName, startDateTime, endDateTime,
                    priority, customTag, reminder);
            addTaskInteractor.execute(inputData, username);
        }
    }

    /**
     * Changes the view state to 'createCustomTag'.
     */
    public void createCustomTag() {
        viewManagerModel.setState("createCustomTag");
    }
}
