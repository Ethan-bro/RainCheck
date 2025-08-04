package app;

import entity.Task;
import interface_adapter.ViewManagerModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskPresenter;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.task.TaskViewModel;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.editTask.EditTaskDataAccessInterface;
import use_case.editTask.EditTaskInputBoundary;
import use_case.editTask.EditTaskInteractor;
import use_case.editTask.EditTaskOutputBoundary;
import view.EditTaskView;

public class EditTaskUseCaseFactory {

    private EditTaskUseCaseFactory() {}

    public static EditTaskView create(
            Task taskToEdit,
            String username,
            ViewManagerModel viewManagerModel,
            EditTaskDataAccessInterface taskDao,
            EditTaskViewModel editTaskViewModel,
            String mainViewKey
    ) {
        final EditTaskController controller = createEditTaskUseCase(
                viewManagerModel,
                taskDao,
                editTaskViewModel
        );

        controller.setUsername(username);
        controller.setCurrentTask(taskToEdit);

        return new EditTaskView(controller, editTaskViewModel, viewManagerModel, mainViewKey);

    }

    public static EditTaskController createEditTaskUseCase(
            ViewManagerModel viewManagerModel,
            EditTaskDataAccessInterface taskDao,
            EditTaskViewModel editTaskViewModel
    ) {

        final EditTaskOutputBoundary presenter = new EditTaskPresenter(editTaskViewModel);

        final EditTaskInputBoundary interactor = new EditTaskInteractor(taskDao, presenter);

        return new EditTaskController(interactor, viewManagerModel);

    }
}
