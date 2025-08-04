package app;

import data_access.SupabaseTagDataAccessObject;
import data_access.SupabaseTaskDataAccessObject;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.logged_in.LoggedInDependencies;
import interface_adapter.task.TaskBoxDependencies;
import view.LoggedInView;

import java.io.IOException;

public class LoggedInUseCaseFactory {

    private LoggedInUseCaseFactory() {}

    public static LoggedInView createLoggedInView(
            LoggedInDependencies loggedInDependencies,
            AddTaskViewModel addTaskViewModel,
            SupabaseTagDataAccessObject tagDao,
            SupabaseTaskDataAccessObject taskDao,
            TaskBoxDependencies taskBoxDependencies
    ) throws IOException {
        return new LoggedInView(
                loggedInDependencies,
                tagDao,
                taskDao,
                addTaskViewModel,
                taskBoxDependencies
        );
    }
}
