package interface_adapter.addTask;

import entity.CustomTag;

import java.util.List;

public class AddTaskState {
    private String errorMessage = "";
    private boolean success;
    private List<CustomTag> tagOptions;

    public AddTaskState() {
        success = false;
    }

    /**
     * Gets the error message for the add task operation.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message for the add task operation.
     *
     * @param errorMessage the error message to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Indicates whether the task was successfully added.
     *
     * @return true if the task was added, false otherwise
     */
    public boolean isTaskAdded() {
        return success;
    }

    /**
     * Sets whether the task was successfully added.
     *
     * @param taskAdded true if the task was added, false otherwise
     */
    public void setTaskAdded(boolean taskAdded) {
        this.success = taskAdded;
    }

    /**
     * Sets the available custom tag options for the task.
     *
     * @param tags the list of custom tags
     */
    public void setTagOptions(List<CustomTag> tags) {
        this.tagOptions = tags;
    }

    /**
     * Gets the available custom tag options for the task.
     *
     * @return the list of custom tags
     */
    public List<CustomTag> getTagOptions() {
        return tagOptions;
    }
}
