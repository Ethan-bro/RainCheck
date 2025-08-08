package app;

import data_access.SupabaseTaskDataAccessObject;

import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.logged_in.LoggedInDependencies;
import interface_adapter.manageTags.ManageTagsViewModel;
import interface_adapter.task.TaskBoxDependencies;

import use_case.notification.NotificationDataAccessInterface;

import view.LoggedInView;

import java.io.IOException;

/**
 * Factory class to create LoggedInView.
 */
public final class LoggedInUseCaseFactory {

    private LoggedInUseCaseFactory() {

    }

    /**
     * Creates a LoggedInView with the given dependencies.
     *
     * @param loggedInDependencies dependencies for logged in view
     * @param addTaskViewModel add task view model
     * @param manageTagsViewModel manage tags view model
     * @param taskDao task data access object
     * @param taskBoxDependencies dependencies for task box
     * @param notificationDataAccess notification data access interface
     * @return a new LoggedInView instance
     * @throws IOException if creation fails due to I/O error
     */
    public static LoggedInView createLoggedInView(
            LoggedInDependencies loggedInDependencies,
            AddTaskViewModel addTaskViewModel,
            ManageTagsViewModel manageTagsViewModel,
            SupabaseTaskDataAccessObject taskDao,
            TaskBoxDependencies taskBoxDependencies,
            NotificationDataAccessInterface notificationDataAccess
    ) throws IOException {
        return new LoggedInView(
                loggedInDependencies,
                taskDao,
                addTaskViewModel,
                taskBoxDependencies,
                manageTagsViewModel,
                notificationDataAccess
        );
    }
}
