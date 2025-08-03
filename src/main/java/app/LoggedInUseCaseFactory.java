package app;

import data_access.SupabaseTagDataAccessObject;
import data_access.SupabaseTaskDataAccessObject;
import interface_adapter.DynamicViewManager;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.deleteTask.DeleteTaskController;
import interface_adapter.deleteTask.DeleteTaskPresenter;
import interface_adapter.deleteTask.DeleteTaskViewModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskPresenter;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.markTaskComplete.MarkTaskCompleteController;
import interface_adapter.markTaskComplete.MarkTaskCompletePresenter;
import interface_adapter.markTaskComplete.MarkTaskCompleteViewModel;
import use_case.DeleteTask.DeleteTaskInteractor;
import use_case.MarkTaskComplete.MarkTaskCompleteInteractor;
import use_case.editTask.EditTaskInteractor;
import view.LoggedInView;

import java.io.IOException;

public class LoggedInUseCaseFactory {

    private LoggedInUseCaseFactory() {}

    public static LoggedInView createLoggedInView(LoggedInViewModel loggedInViewModel,
                                                  LogoutController logoutController,
                                                  ViewManagerModel viewManagerModel,
                                                  DynamicViewManager dynamicViewManager,
                                                  AddTaskViewModel addTaskViewModel,
                                                  EditTaskViewModel editTaskViewModel,
                                                  SupabaseTagDataAccessObject tagDao,
                                                  SupabaseTaskDataAccessObject taskDao) throws IOException {

        MarkTaskCompleteController markTaskCompleteController = buildMarkTaskCompleteController(taskDao);
        DeleteTaskController deleteTaskController = buildDeleteTaskController(taskDao);
        EditTaskController editTaskController = buildEditTaskController(tagDao, taskDao, viewManagerModel);

        return new LoggedInView(
                loggedInViewModel,
                logoutController,
                viewManagerModel,
                dynamicViewManager,
                tagDao,
                taskDao,
                editTaskViewModel,
                addTaskViewModel,
                markTaskCompleteController,
                deleteTaskController,
                editTaskController);
         
    }

    private static MarkTaskCompleteController buildMarkTaskCompleteController(SupabaseTaskDataAccessObject taskDao) {
        MarkTaskCompletePresenter markTaskCompletePresenter = new MarkTaskCompletePresenter(new MarkTaskCompleteViewModel());
        MarkTaskCompleteInteractor markTaskCompleteInteractor = new MarkTaskCompleteInteractor(taskDao, markTaskCompletePresenter);
        return new MarkTaskCompleteController(markTaskCompleteInteractor);
    }

    private static DeleteTaskController buildDeleteTaskController(SupabaseTaskDataAccessObject taskDao) {
        DeleteTaskPresenter deleteTaskPresenter = new DeleteTaskPresenter(new DeleteTaskViewModel());
        DeleteTaskInteractor deleteTaskInteractor = new DeleteTaskInteractor(taskDao, deleteTaskPresenter);
        return new DeleteTaskController(deleteTaskInteractor);
    }

    private static EditTaskController buildEditTaskController(SupabaseTagDataAccessObject tagDao, SupabaseTaskDataAccessObject taskDao, ViewManagerModel viewManagerModel) {
        EditTaskViewModel editTaskViewModel = new EditTaskViewModel(tagDao);
        EditTaskPresenter editTaskPresenter = new EditTaskPresenter(editTaskViewModel);
        EditTaskInteractor editTaskInteractor = new EditTaskInteractor(taskDao, editTaskPresenter);
        return new EditTaskController(editTaskInteractor, viewManagerModel);
    }

}
