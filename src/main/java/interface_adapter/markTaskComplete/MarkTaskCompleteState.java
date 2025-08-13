package interface_adapter.markTaskComplete;

public class MarkTaskCompleteState {

    private boolean success;
    private String error;

    /**
     * Returns whether the task was marked as complete successfully.
     *
     * @return true if the task was marked complete, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success status for marking the task as complete.
     *
     * @param success true if the task was marked complete, false otherwise
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Returns the error message, if any, for marking the task as complete.
     *
     * @return the error message, or null if there was no error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message for marking the task as complete.
     *
     * @param error the error message to set
     */
    public void setError(String error) {
        this.error = error;
    }
}
