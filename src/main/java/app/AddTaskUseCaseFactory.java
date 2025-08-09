package app;

import data_access.WeatherApiService;

import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskController;
import interface_adapter.addTask.AddTaskPresenter;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.addTask.UuidGenerator;
import interface_adapter.logged_in.LoggedInViewModel;

import use_case.addTask.AddTaskInputBoundary;
import use_case.addTask.AddTaskInteractor;
import use_case.listTasks.TaskDataAccessInterface;
import use_case.notification.ScheduleNotificationInteractor;

import view.AddTaskView;

/**
 * Factory class for creating and wiring up all components required
 * for the Add Task use case.
 */
public final class AddTaskUseCaseFactory {

    /**
     * Private constructor to prevent instantiation.
     */
    private AddTaskUseCaseFactory() {
        // Prevent instantiation
    }

    /**
     * Creates an {@link AddTaskView} instance with all necessary dependencies.
     *
     * @param viewManagerModel       the view manager model
     * @param addTaskViewModel       the view model for adding tasks
     * @param loggedInViewModel      the view model for the logged-in user
     * @param taskDao                the task data access interface
     * @param weatherApiService      the weather API service for task weather info
     * @param notificationInteractor the interactor for scheduling notifications
     * @param mainViewKey            the key of the main view to return to
     * @return a fully wired {@link AddTaskView} instance
     */
    public static AddTaskView create(
            ViewManagerModel viewManagerModel,
            AddTaskViewModel addTaskViewModel,
            LoggedInViewModel loggedInViewModel,
            TaskDataAccessInterface taskDao,
            WeatherApiService weatherApiService,
            ScheduleNotificationInteractor notificationInteractor,
            String mainViewKey) {

        final AddTaskPresenter addTaskPresenter =
                new AddTaskPresenter(addTaskViewModel, viewManagerModel, mainViewKey);

        final AddTaskInputBoundary addTaskInteractor =
                new AddTaskInteractor(taskDao, new UuidGenerator(), addTaskPresenter,
                        weatherApiService, notificationInteractor);

        final AddTaskController addTaskController =
                new AddTaskController(addTaskInteractor, viewManagerModel, addTaskViewModel);

        return new AddTaskView(addTaskController, addTaskViewModel, loggedInViewModel, viewManagerModel);
    }
}
