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
 * A factory class responsible for creating instances of components involved
 * in the Edit Task use case, such as the controller and view.
 */
public final class EditTaskUseCaseFactory {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private EditTaskUseCaseFactory() {
    }

    /**
     * Creates and returns an EditTaskController for handling Edit Task actions.
     *
     * @param taskDao            The data access object for tasks (e.g., Supabase).
     * @param viewModel          The view model managing the state for Edit Task UI.
     * @param viewManagerModel   The model responsible for view navigation and management.
     * @param weatherApiService  The service to fetch weather data related to tasks.
     * @return A fully constructed instance of EditTaskController
     */
    public static EditTaskController createController(
            SupabaseTaskDataAccessObject taskDao,
            EditTaskViewModel viewModel,
            ViewManagerModel viewManagerModel,
            WeatherApiService weatherApiService
    ) {
        EditTaskOutputBoundary presenter = new EditTaskPresenter(viewModel);
        EditTaskInputBoundary interactor = new EditTaskInteractor(taskDao, presenter, weatherApiService);
        return new EditTaskController(interactor, viewManagerModel);
    }

    /**
     * Creates and returns an EditTaskView for editing tasks.
     *
     * @param controller       The controller to handle UI actions and business logic.
     * @param viewModel        The view model associated with Edit Task.
     * @param viewManagerModel The view manager for switching views.
     * @param mainViewKey      The identifier for the main view (to navigate back).
     * @return A fully constructed instance of EditTaskView
     */
    public static EditTaskView createView(
            EditTaskController controller,
            EditTaskViewModel viewModel,
            ViewManagerModel viewManagerModel,
            String mainViewKey
    ) {
        return new EditTaskView(controller, viewModel, viewManagerModel, mainViewKey);
    }
}
