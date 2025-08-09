package interface_adapter.addTask;

import interface_adapter.ViewManagerModel;
import interface_adapter.events.TagChangeEventNotifier;

import use_case.addTask.AddTaskOutputBoundary;
import use_case.addTask.AddTaskOutputData;

public class AddTaskPresenter implements AddTaskOutputBoundary {

    private final AddTaskViewModel addTaskViewModel;
    private final ViewManagerModel viewManagerModel;
    private final String mainViewKey;

    public AddTaskPresenter(AddTaskViewModel addTaskViewModel,
                            ViewManagerModel viewManagerModel,
                            String mainViewKey) {
        this.addTaskViewModel = addTaskViewModel;
        this.viewManagerModel = viewManagerModel;
        this.mainViewKey = mainViewKey;
    }

    @Override
    public void prepareFailView(AddTaskOutputData addTaskOutputData) {
        final AddTaskState addTaskState = addTaskViewModel.getState();
        addTaskState.setErrorMessage(addTaskOutputData.getErrorMessage());
        addTaskState.setTaskAdded(false);
        addTaskViewModel.firePropertyChanged("errorMessage");
    }

    @Override
    public void prepareSuccessView(AddTaskOutputData addTaskOutputData) {
        final AddTaskState addTaskState = addTaskViewModel.getState();
        addTaskState.setErrorMessage(null);
        addTaskState.setTaskAdded(true);
        addTaskViewModel.firePropertyChanged("taskAdded");

        // Notify globally that tags might have changed
        TagChangeEventNotifier.fire();

        // Navigate back to main view
        viewManagerModel.setState(mainViewKey);
    }
}
