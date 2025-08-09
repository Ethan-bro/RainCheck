package interface_adapter.DeleteCT;

import interface_adapter.manageTags.ManageTagsViewModel;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.DeleteCustomTag.DeleteCustomTagOutputBoundary;
import use_case.DeleteCustomTag.DeleteCustomTagOutputData;

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
