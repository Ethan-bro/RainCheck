package interface_adapter.deleteCustomTag;

import interface_adapter.events.TagChangeEventNotifier;
import interface_adapter.manageTags.ManageTagsViewModel;

import use_case.deleteCustomTag.DeleteCustomTagOutputBoundary;
import use_case.deleteCustomTag.DeleteCustomTagOutputData;

public class DeleteCustomTagPresenter implements DeleteCustomTagOutputBoundary {

    private final ManageTagsViewModel manageTagsViewModel;

    public DeleteCustomTagPresenter(ManageTagsViewModel manageTagsViewModel) {
        this.manageTagsViewModel = manageTagsViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteCustomTagOutputData successOutput) {
        manageTagsViewModel.firePropertyChanged("tagDeleted");

        // notify all subscribed views/viewModels of tag changes
        TagChangeEventNotifier.fire();
    }
}
