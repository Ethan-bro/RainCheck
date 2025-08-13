package interface_adapter.editTask;

public class EditTaskState {

    private boolean success;
    private String error;

    /**
     * Returns whether the task edit was successful.
     *
     * @return true if the task was edited successfully, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success status of the task edit.
     *
     * @param success true if the task was edited successfully, false otherwise
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Returns the error message associated with the task edit, if any.
     *
     * @return the error message, or null if there was no error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message for the task edit.
     *
     * @param error the error message to set
     */
    public void setError(String error) {
        this.error = error;
    }
}
