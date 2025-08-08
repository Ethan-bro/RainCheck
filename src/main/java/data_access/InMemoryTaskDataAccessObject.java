package data_access;

import java.util.HashMap;
import java.util.Map;

import entity.Task;
import entity.TaskID;
import use_case.deleteTask.DeleteTaskDataAccessInterface;
import use_case.editTask.EditTaskDataAccessInterface;
import use_case.markTaskComplete.MarkTaskCompleteDataAccessInterface;

/**
 * A fake in-memory DAO used only for unit tests. No external I/O.
 */
public final class InMemoryTaskDataAccessObject implements
        EditTaskDataAccessInterface,
        DeleteTaskDataAccessInterface,
        MarkTaskCompleteDataAccessInterface {

    private final Map<TaskID, Task> taskMap = new HashMap<>();
    private String currentUsername;

    /**
     * Adds task to the in memory task DAO.
     * @param username the user's username
     * @param task a task belonging to the user
     */
    public void addTask(String username, Task task) {
        this.currentUsername = username;
        taskMap.put(task.getTaskInfo().getId(), task);
    }

    @Override
    public void markAsComplete(String username, TaskID taskId) {
        Task task = taskMap.get(taskId);
        if (task != null) {
            task.getTaskInfo().setTaskStatus("Complete");
        }
    }

    @Override
    public void deleteTask(String username, TaskID taskId) {
        taskMap.remove(taskId);
    }

    @Override
    public Task getTaskByIdAndEmail(String email, TaskID id) {
        return taskMap.get(id);
    }

    @Override
    public Task getTaskById(String username, TaskID taskId) {
        return taskMap.get(taskId);
    }

    @Override
    public void updateTask(String username, Task task) {
        taskMap.put(task.getTaskInfo().getId(), task);
    }

    /**
     * Retrieves user's current username as a string.
     * @return the user's current username
     */
    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Sets the username for the current session context.
     * @param currentUsername the username to set
     */
    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }
}
