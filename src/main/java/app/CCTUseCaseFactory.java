package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.create_customTag.CCTController;
import interface_adapter.create_customTag.CCTPresenter;
import interface_adapter.create_customTag.CCTViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.createCustomTag.CCTInputBoundary;
import use_case.createCustomTag.CCTInteractor;
import use_case.createCustomTag.CCTOutputBoundary;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import view.CCTView;

public class CCTUseCaseFactory {

    private CCTUseCaseFactory() {}

    public static CCTController create(
            ViewManagerModel viewManagerModel,
            CCTViewModel viewModel,
            CustomTagDataAccessInterface tagDao,
            LoggedInViewModel loggedInViewModel,
            AddTaskViewModel addTaskViewModel) {

        final CCTOutputBoundary Presenter = new CCTPresenter(viewManagerModel, viewModel, addTaskViewModel);

        final CCTInputBoundary Interactor = new CCTInteractor(tagDao, Presenter);

        CCTView view = new CCTView(viewModel, null, loggedInViewModel);

        // make a controller that passes in the view
        CCTController controller = new CCTController(Interactor, view);

        // wire the view to use that controller
        view.setController(controller);

        return controller;
    }
}