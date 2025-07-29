package interface_adapter.addTask;

public class AddTaskState {
    private String errorMessage = "";
    private boolean success = false;

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
}
