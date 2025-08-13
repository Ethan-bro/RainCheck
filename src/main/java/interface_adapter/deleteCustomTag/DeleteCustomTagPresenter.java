package interface_adapter.deleteCustomTag;

import interface_adapter.events.TagChangeEventNotifier;
import interface_adapter.manageTags.ManageTagsViewModel;
import use_case.deleteCustomTag.DeleteCustomTagOutputBoundary;
import use_case.deleteCustomTag.DeleteCustomTagOutputData;

public class DeleteCustomTagPresenter implements DeleteCustomTagOutputBoundary {

    private final ManageTagsViewModel manageTagsViewModel;


    /**
     * Constructs a DeleteCustomTagPresenter with the given ManageTagsViewModel.
     * This presenter acts as the output boundary for the delete custom tag use case,
     * adhering to Clean Architecture by decoupling the use case logic from the view layer.
     *
     * @param manageTagsViewModel the view model responsible for managing tags in the UI
     */
    public DeleteCustomTagPresenter(ManageTagsViewModel manageTagsViewModel) {
        this.manageTagsViewModel = manageTagsViewModel;
    }

    /**
     * Prepares the view for a successful custom tag deletion.
     * Notifies the ManageTagsViewModel and all subscribed views/view models of tag changes,
     * supporting the Dependency Inversion Principle by communicating through interfaces.
     *
     * @param successOutput the output data from the delete custom tag use case
     */
    @Override
    public void prepareSuccessView(DeleteCustomTagOutputData successOutput) {
        manageTagsViewModel.firePropertyChanged("tagDeleted");

        // notify all subscribed views/viewModels of tag changes
        TagChangeEventNotifier.fire();
    }
}
