package app;

import data_access.SupabaseTagDataAccessObject;
import data_access.SupabaseTaskDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskPresenter;
import interface_adapter.editTask.EditTaskViewModel;
import use_case.editTask.EditTaskInputBoundary;
import use_case.editTask.EditTaskInteractor;
import use_case.editTask.EditTaskOutputBoundary;
import view.EditTaskView;

public class EditTaskUseCaseFactory {

    private EditTaskUseCaseFactory() {}

    public static EditTaskViewModel createViewModel(SupabaseTagDataAccessObject tagDao) {
        return new EditTaskViewModel(tagDao);
    }

    public static EditTaskController createController(
            SupabaseTaskDataAccessObject taskDao,
            EditTaskViewModel viewModel,
            ViewManagerModel viewManagerModel
    ) {
        EditTaskOutputBoundary presenter = new EditTaskPresenter(viewModel);
        EditTaskInputBoundary interactor = new EditTaskInteractor(taskDao, presenter);
        return new EditTaskController(interactor, viewManagerModel);
    }

    public static EditTaskView createView(
            EditTaskController controller,
            EditTaskViewModel viewModel,
            ViewManagerModel viewManagerModel,
            String mainViewKey
    ) {
        return new EditTaskView(controller, viewModel, viewManagerModel, mainViewKey);
    }
}
