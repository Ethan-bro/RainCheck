package app;

import data_access.SupabaseTagDataAccessObject;
import data_access.SupabaseTaskDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.deleteTask.DeleteTaskViewModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskPresenter;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;
import interface_adapter.task.TaskBoxDependencies;
import use_case.editTask.EditTaskInteractor;
import view.LoggedInView;

import java.io.IOException;

public class LoggedInUseCaseFactory {

    private LoggedInUseCaseFactory() {}

    public static LoggedInView createLoggedInView(LoggedInViewModel loggedInViewModel,
                                                  LogoutController logoutController,
                                                  ViewManagerModel viewManagerModel,
                                                  AddTaskViewModel addTaskViewModel,
                                                  SupabaseTagDataAccessObject tagDao,
                                                  SupabaseTaskDataAccessObject taskDao) throws IOException {

        MarkTaskCompleteViewModel markTaskCompleteViewModel = new MarkTaskCompleteViewModel();
        EditTaskController editTaskController = buildEditTaskController(tagDao, taskDao, viewManagerModel);

        TaskBoxDependencies taskBoxDependencies = new TaskBoxDependencies(
                markTaskCompleteViewModel,
                new DeleteTaskViewModel(),
                viewManagerModel,
                editTaskController
        );

        return new LoggedInView(
                loggedInViewModel,
                logoutController,
                tagDao,
                taskDao,
                addTaskViewModel,
                taskBoxDependencies
        );
    }

    private static EditTaskController buildEditTaskController(SupabaseTagDataAccessObject tagDao,
                                                              SupabaseTaskDataAccessObject taskDao,
                                                              ViewManagerModel viewManagerModel) {
        EditTaskViewModel editTaskViewModel = new EditTaskViewModel(tagDao);
        EditTaskPresenter editTaskPresenter = new EditTaskPresenter(editTaskViewModel);
        EditTaskInteractor editTaskInteractor = new EditTaskInteractor(taskDao, editTaskPresenter);
        return new EditTaskController(editTaskInteractor, viewManagerModel);
    }
}
