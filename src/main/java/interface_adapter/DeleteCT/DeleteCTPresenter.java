package interface_adapter.DeleteCT;

import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.DeleteCT.DeleteCTOutputBoundary;
import use_case.DeleteCT.DeleteCTOutputData;

public class DeleteCTPresenter implements DeleteCTOutputBoundary {

    private final ManageTagsViewModel manageTagsViewModel;

    public DeleteCTPresenter(ManageTagsViewModel manageTagsViewModel) {
        this.manageTagsViewModel = manageTagsViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteCTOutputData successOutput) {
        manageTagsViewModel.firePropertyChanged("tagDeleted");

        // notify all subscribed views/viewModels of tag changes
        TagChangeEventNotifier.fire();
    }
}
