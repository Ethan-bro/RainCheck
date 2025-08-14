package use_case.notification;

import entity.Task;
import entity.TaskID;
import use_case.editTask.EditTaskDataAccessInterface;

public class MockTaskDataAccess implements EditTaskDataAccessInterface {

    private Task task;
    private boolean shouldThrowException = false;
    private String exceptionMessage = "Test exception";

    public void setTask(Task task) {
        this.task = task;
    }

    public void setShouldThrowException(boolean shouldThrow) {
        this.shouldThrowException = shouldThrow;
    }

    public void setExceptionMessage(String message) {
        this.exceptionMessage = message;
    }

    @Override
    public Task getTaskByIdAndEmail(String email, TaskID id) {
        if (shouldThrowException) {
            throw new IllegalArgumentException(exceptionMessage);
        }
        return task;
    }

    @Override
    public Task getTaskById(String username, TaskID taskId) {
        if (shouldThrowException) {
            throw new IllegalArgumentException(exceptionMessage);
        }
        return task;
    }

    @Override
    public void updateTask(String username, Task task) {
        if (shouldThrowException) {
            throw new IllegalArgumentException(exceptionMessage);
        }
        this.task = task;
    }
}