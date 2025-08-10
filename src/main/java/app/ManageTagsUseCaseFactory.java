package app;

import interface_adapter.DeleteTag.DeleteTagPresenter;
import interface_adapter.EditTag.EditTagViewModel;
import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.DeleteTag.DeleteTagController;
import interface_adapter.ViewManagerModel;
import interface_adapter.CreateTag.CreateTagViewModel;
import use_case.CreateTag.TagDataAccessInterface;
import use_case.DeleteTag.DeleteTagInputBoundary;
import use_case.DeleteTag.DeleteTagInteractor;
import view.ManageTagsView;

public class ManageTagsUseCaseFactory {

    private ManageTagsUseCaseFactory() {}

    public static ManageTagsView create(
            ViewManagerModel viewManagerModel,
            ManageTagsViewModel viewModel,
            CreateTagViewModel cctViewModel,
            EditTagViewModel editTagViewModel,
            TagDataAccessInterface tagDao
    ) {

        DeleteTagPresenter deleteCTPresenter = new DeleteTagPresenter(viewModel);
        DeleteTagInputBoundary deleteCTInteractor = new DeleteTagInteractor(deleteCTPresenter, tagDao);
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
