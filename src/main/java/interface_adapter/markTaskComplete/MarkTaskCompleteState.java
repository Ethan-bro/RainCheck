package interface_adapter.markTaskComplete;

/**
 * Holds the state of a mark-task-complete operation.
 */
public class MarkTaskCompleteState {

    private boolean success;
    private String error;

    /**
     * Returns whether the operation was successful.
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets whether the operation was successful.
     * @param success true if successful, false otherwise
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Returns the error message, if any.
     * @return the error message, if any
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message.
     *
     * @param error the error message to set
     */
    public void setError(String error) {
        this.error = error;
    }
}
