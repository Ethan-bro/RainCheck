package interface_adapter.deleteTask;

public final class DeleteTaskState {

    private boolean success;
    private String error;

    /**
     * Returns whether the task deletion was successful.
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets task deletion to success.
     * @param success success of the task
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
