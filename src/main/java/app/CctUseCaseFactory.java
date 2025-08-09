package app;

import interface_adapter.createTag.CreateCustomTagController;
import interface_adapter.createTag.CreateCustomTagPresenter;
import interface_adapter.createTag.CreateCustomTagViewModel;
import interface_adapter.manageTags.ManageTagsViewModel;
import interface_adapter.ViewManagerModel;

import use_case.createCustomTag.CreateCustomTagInputBoundary;
import use_case.createCustomTag.CreateCustomTagInteractor;
import use_case.createCustomTag.CreateCustomTagOutputBoundary;
import use_case.createCustomTag.CustomTagDataAccessInterface;

import view.CreateCustomTagView;

public class CctUseCaseFactory {

    private CctUseCaseFactory() {}

    public static CreateCustomTagView create (
            ViewManagerModel viewManagerModel,
            CreateCustomTagViewModel viewModel,
            ManageTagsViewModel manageTagsViewModel,
            CustomTagDataAccessInterface CTDataAccessInterface) {

        final CreateCustomTagController controller = createCCTUseCase(viewManagerModel, viewModel, manageTagsViewModel,
                CTDataAccessInterface);

        return new CreateCustomTagView(viewManagerModel, viewModel, controller);
    }

    public static CreateCustomTagController createCCTUseCase(ViewManagerModel viewManagerModel,
                                                 CreateCustomTagViewModel cctViewModel,
                                                 ManageTagsViewModel manageTagsViewModel,
                                                 CustomTagDataAccessInterface customTagDataAccessInterface) {

        final CreateCustomTagOutputBoundary Presenter = new CreateCustomTagPresenter(viewManagerModel, cctViewModel, manageTagsViewModel);

        final CreateCustomTagInputBoundary Interactor = new CreateCustomTagInteractor(customTagDataAccessInterface, Presenter);

        return new CreateCustomTagController(Interactor);
    }

}