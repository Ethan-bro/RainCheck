package app;

import data_access.SupabaseTaskDataAccessObject;
import data_access.WeatherApiService;

import interface_adapter.ViewManagerModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskPresenter;
import interface_adapter.editTask.EditTaskViewModel;

import use_case.editTask.EditTaskInputBoundary;
import use_case.editTask.EditTaskInteractor;
import use_case.editTask.EditTaskOutputBoundary;

import view.EditTaskView;

/**
 * Factory class for creating EditTask use case components.
 */
public final class EditTaskUseCaseFactory {

    /** Prevent instantiation. */
    private EditTaskUseCaseFactory() {
        // intentionally empty
    }

    /**
     * Creates an EditTaskController instance with dependencies.
     *
     * @param taskDao the data access object for tasks
     * @param viewModel the EditTaskViewModel to inject
     * @param viewManagerModel the ViewManagerModel to inject
     * @param weatherApiService the WeatherApiService to inject
     * @return a new EditTaskController instance
     */
    public static EditTaskController createController(
            final SupabaseTaskDataAccessObject taskDao,
            final EditTaskViewModel viewModel,
            final ViewManagerModel viewManagerModel,
            final WeatherApiService weatherApiService
    ) {
        final EditTaskOutputBoundary presenter = new EditTaskPresenter(viewModel);
        final EditTaskInputBoundary interactor = new EditTaskInteractor(taskDao, presenter, weatherApiService);
        return new EditTaskController(interactor, viewManagerModel);
    }

    /**
     * Creates an EditTaskView instance.
     *
     * @param controller the EditTaskController to inject
     * @param viewModel the EditTaskViewModel to inject
     * @param viewManagerModel the ViewManagerModel to inject
     * @param mainViewKey the key of the main view for navigation
     * @return a new EditTaskView instance
     */
    public static EditTaskView createView(
            final EditTaskController controller,
            final EditTaskViewModel viewModel,
            final ViewManagerModel viewManagerModel,
            final String mainViewKey
    ) {
        return new EditTaskView(controller, viewModel, viewManagerModel, mainViewKey);
    }
}
