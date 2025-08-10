package interface_adapter.DeleteTag;

import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.DeleteTag.DeleteTagOutputBoundary;
import use_case.DeleteTag.DeleteTagOutputData;

public class DeleteTagPresenter implements DeleteTagOutputBoundary {

    private final ManageTagsViewModel manageTagsViewModel;

    public DeleteTagPresenter(ManageTagsViewModel manageTagsViewModel) {
        this.manageTagsViewModel = manageTagsViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteTagOutputData successOutput) {
        manageTagsViewModel.firePropertyChanged("tagDeleted");

        // notify all subscribed views/viewModels of tag changes
        TagChangeEventNotifier.fire();
    }
}
