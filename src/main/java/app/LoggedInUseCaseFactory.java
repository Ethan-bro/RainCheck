package app;

import data_access.SupabaseTagDataAccessObject;
import data_access.SupabaseTaskDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.deleteTask.DeleteTaskViewModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskPresenter;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.logged_in.LoggedInDependencies;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;
import interface_adapter.task.TaskBoxDependencies;
import use_case.editTask.EditTaskInteractor;
import view.LoggedInView;

import java.io.IOException;

public class LoggedInUseCaseFactory {

    private LoggedInUseCaseFactory() {}

    public static LoggedInView createLoggedInView(LoggedInDependencies loggedInDependencies,
                                                  ViewManagerModel viewManagerModel,
                                                  AddTaskViewModel addTaskViewModel,
                                                  SupabaseTagDataAccessObject tagDao,
                                                  SupabaseTaskDataAccessObject taskDao,
                                                  EditTaskViewModel editTaskViewModel,
                                                  EditTaskController editTaskController) throws IOException {

        MarkTaskCompleteViewModel markTaskCompleteViewModel = new MarkTaskCompleteViewModel();

        TaskBoxDependencies taskBoxDependencies = new TaskBoxDependencies(
                markTaskCompleteViewModel,
                new DeleteTaskViewModel(),
                viewManagerModel,
                editTaskController,
                editTaskViewModel
        );

        return new LoggedInView(
                loggedInDependencies.loggedInViewModel(),
                loggedInDependencies.logoutController(),
                tagDao,
                taskDao,
                addTaskViewModel,
                taskBoxDependencies
        );
    }
}
