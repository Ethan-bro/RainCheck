package app;

import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.CreateTag.CreateTagController;
import interface_adapter.CreateTag.CreateTagPresenter;
import interface_adapter.CreateTag.CreateTagViewModel;
import use_case.CreateTag.CreateTagInputBoundary;
import use_case.CreateTag.CreateTagInteractor;
import use_case.CreateTag.CreateTagOutputBoundary;
import use_case.CreateTag.TagDataAccessInterface;
import view.CreateTagView;

public class CCTUseCaseFactory {

    private CCTUseCaseFactory() {}

    public static CreateTagView create (
            ViewManagerModel viewManagerModel,
            CreateTagViewModel viewModel,
            ManageTagsViewModel manageTagsViewModel,
            TagDataAccessInterface CTDataAccessInterface) {

        final CreateTagController controller = createCCTUseCase(viewManagerModel, viewModel, manageTagsViewModel,
                CTDataAccessInterface);

        return new CreateTagView(viewManagerModel, viewModel, controller);
    }

    public static CreateTagController createCCTUseCase(ViewManagerModel viewManagerModel,
                                                       CreateTagViewModel cctViewModel,
                                                       ManageTagsViewModel manageTagsViewModel,
                                                       TagDataAccessInterface customTagDataAccessInterface) {

        final CreateTagOutputBoundary Presenter = new CreateTagPresenter(viewManagerModel, cctViewModel, manageTagsViewModel);

        final CreateTagInputBoundary Interactor = new CreateTagInteractor(customTagDataAccessInterface, Presenter);

        return new CreateTagController(Interactor);
    }

}