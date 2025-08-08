package app;

import interface_adapter.EditTag.EditTagController;
import interface_adapter.EditTag.EditTagPresenter;
import interface_adapter.EditTag.EditTagViewModel;
import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.ViewManagerModel;
import use_case.EditCT.EditTagInputBoundary;
import use_case.EditCT.EditTagInteractor;
import use_case.EditCT.EditTagOutputBoundary;
import use_case.CreateCT.CustomTagDataAccessInterface;
import view.EditTagView;

public class EditTagUseCaseFactory {

    private EditTagUseCaseFactory() {}

    public static EditTagView create(ViewManagerModel viewManagerModel,
                                     EditTagViewModel viewModel, ManageTagsViewModel manageTagsViewModel,
                                     CustomTagDataAccessInterface customTagDataAccessInterface) {

        EditTagController controller = createEditTagUseCase(viewManagerModel, viewModel,
                manageTagsViewModel, customTagDataAccessInterface);

        return new EditTagView(viewManagerModel, manageTagsViewModel, viewModel, controller);
    }

    public static EditTagController createEditTagUseCase(ViewManagerModel viewManagerModel,
                                                  EditTagViewModel viewModel,
                                                  ManageTagsViewModel manageTagsViewModel,
                                                  CustomTagDataAccessInterface tagDao) {

        EditTagOutputBoundary presenter = new EditTagPresenter(viewManagerModel, viewModel, manageTagsViewModel);

        EditTagInputBoundary interactor = new EditTagInteractor(tagDao, presenter);

        return new EditTagController(tagDao, manageTagsViewModel, interactor);
    }
}
