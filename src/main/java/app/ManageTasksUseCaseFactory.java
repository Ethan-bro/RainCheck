package app;

import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_customTag.CCTController;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import view.ManageTagsView;

public class ManageTasksUseCaseFactory {

    private ManageTasksUseCaseFactory() {}

    public static ManageTagsView create(
            LoggedInViewModel loggedInVM,
            ViewManagerModel viewManagerModel,
            ManageTagsViewModel viewModel,
            CustomTagDataAccessInterface tagDao
    ) {

        return new ManageTagsView(loggedInVM, viewManagerModel, viewModel, tagDao);

    }
}
