package app;

import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.ManageTags.EditTagController;
import interface_adapter.ManageTags.DeleteTagController;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_customTag.CCTViewModel;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import view.ManageTagsView;

public class ManageTagsUseCaseFactory {

    private ManageTagsUseCaseFactory() {}

    public static ManageTagsView create(
            ViewManagerModel viewManagerModel,
            ManageTagsViewModel viewModel,
            CCTViewModel cctViewModel,
            CustomTagDataAccessInterface tagDao
    ) {
        EditTagController editTagController = new EditTagController(tagDao, viewModel);
        DeleteTagController deleteTagController = new DeleteTagController(tagDao, viewModel);

        return new ManageTagsView(
                viewManagerModel,
                viewModel,
                cctViewModel,
                editTagController,
                deleteTagController
        );
    }
}
