package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.createTag.CreateCustomTagViewModel;
import interface_adapter.editTag.EditTagViewModel;
import interface_adapter.manageTags.DeleteTagController;
import interface_adapter.manageTags.ManageTagsViewModel;

import use_case.createCustomTag.CustomTagDataAccessInterface;

import view.ManageTagsView;

/**
 * Factory class for creating ManageTagsView.
 */
public final class ManageTagsUseCaseFactory {

    /** Prevent instantiation. */
    private ManageTagsUseCaseFactory() {
        // intentionally empty
    }

    /**
     * Creates a ManageTagsView instance with dependencies.
     *
     * @param viewManagerModel the ViewManagerModel to inject
     * @param viewModel the ManageTagsViewModel to inject
     * @param CreateCustomTagViewModel the CreateCustomTagViewModel to inject
     * @param editTagViewModel the EditTagViewModel to inject
     * @param tagDao the CustomTagDataAccessInterface to inject
     * @return a ManageTagsView instance
     */
    public static ManageTagsView create(
            final ViewManagerModel viewManagerModel,
            final ManageTagsViewModel viewModel,
            final CreateCustomTagViewModel CreateCustomTagViewModel,
            final EditTagViewModel editTagViewModel,
            final CustomTagDataAccessInterface tagDao
    ) {
        final DeleteTagController deleteTagController = new DeleteTagController(tagDao, viewModel);

        return new ManageTagsView(
                viewManagerModel,
                viewModel,
                CreateCustomTagViewModel,
                editTagViewModel,
                deleteTagController
        );
    }
}
