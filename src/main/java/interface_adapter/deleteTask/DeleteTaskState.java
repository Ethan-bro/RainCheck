package interface_adapter.deleteTask;

public class DeleteTaskState {

    private boolean success;
    private String error;


    /**
     * Returns whether the task deletion was successful.
     *
     * @return true if the task was deleted successfully, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }


    /**
     * Sets the success status of the task deletion.
     *
     * @param success true if the task was deleted successfully, false otherwise
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }


    /**
     * Returns the error message associated with the task deletion, if any.
     *
     * @return the error message, or null if there was no error
     */
    public String getError() {
        return error;
    }


    /**
     * Sets the error message for the task deletion.
     *
     * @param error the error message to set
     */
    public void setError(String error) {
        this.error = error;
    }
}
