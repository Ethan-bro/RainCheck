package app;

import interface_adapter.DeleteCT.DeleteCTPresenter;
import interface_adapter.EditTag.EditTagViewModel;
import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.DeleteCT.DeleteTagController;
import interface_adapter.ViewManagerModel;
import interface_adapter.CreateTag.CCTViewModel;
import use_case.CreateCT.CustomTagDataAccessInterface;
import use_case.DeleteCT.DeleteCTInputBoundary;
import use_case.DeleteCT.DeleteCTInteractor;
import view.ManageTagsView;

public class ManageTagsUseCaseFactory {

    private ManageTagsUseCaseFactory() {}

    public static ManageTagsView create(
            ViewManagerModel viewManagerModel,
            ManageTagsViewModel viewModel,
            CCTViewModel cctViewModel,
            EditTagViewModel editTagViewModel,
            CustomTagDataAccessInterface tagDao
    ) {

        DeleteCTPresenter deleteCTPresenter = new DeleteCTPresenter(viewModel);
        DeleteCTInputBoundary deleteCTInteractor = new DeleteCTInteractor(deleteCTPresenter, tagDao);
        DeleteTagController deleteTagController = new DeleteTagController(tagDao, viewModel, deleteCTInteractor);

        return new ManageTagsView(
                viewManagerModel,
                viewModel,
                cctViewModel,
                editTagViewModel,
                deleteTagController
        );
    }
}
