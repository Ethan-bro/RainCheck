package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.createTag.cctViewModel;
import interface_adapter.editTag.editTagViewModel;
import interface_adapter.manageTags.deleteTagController;
import interface_adapter.manageTags.manageTagsViewModel;

import use_case.createCustomTag.customTagDataAccessInterface;

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
     * @param viewModel the manageTagsViewModel to inject
     * @param cctViewModel the cctViewModel to inject
     * @param editTagViewModel the editTagViewModel to inject
     * @param tagDao the customTagDataAccessInterface to inject
     * @return a ManageTagsView instance
     */
    public static ManageTagsView create(
            final ViewManagerModel viewManagerModel,
            final manageTagsViewModel viewModel,
            final cctViewModel cctViewModel,
            final editTagViewModel editTagViewModel,
            final customTagDataAccessInterface tagDao
    ) {
        final deleteTagController deleteTagController = new deleteTagController(tagDao, viewModel);

        return new ManageTagsView(
                viewManagerModel,
                viewModel,
                cctViewModel,
                editTagViewModel,
                deleteTagController
        );
    }
}
