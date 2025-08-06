package interface_adapter.addTask;

import entity.CustomTag;

import java.util.List;

public class AddTaskState {
    private String errorMessage = "";
    private boolean success = false;
    private List<CustomTag> tagOptions;

    public AddTaskState() {
        // empty constructor
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isTaskAdded() {
        return success;
    }

    public void setTaskAdded(boolean taskAdded) {
        this.success = taskAdded;
    }

    public void setTagOptions(List<CustomTag> tags) {
        this.tagOptions = tags;
    }

    public List<CustomTag> getTagOptions() {
        return tagOptions;
    }
}
