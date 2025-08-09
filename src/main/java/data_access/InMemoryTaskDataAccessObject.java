package data_access;

import entity.Task;
import entity.TaskID;

import use_case.deleteTask.DeleteTaskDataAccessInterface;
import use_case.editTask.EditTaskDataAccessInterface;
import use_case.markTaskComplete.MarkTaskCompleteDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * A fake in-memory DAO used only for unit tests. No external I/O.
 */
public class InMemoryTaskDataAccessObject implements
        EditTaskDataAccessInterface,
        DeleteTaskDataAccessInterface,
        MarkTaskCompleteDataAccessInterface {

    private final Map<TaskID, Task> taskMap = new HashMap<>();
    private String currentUsername;

    /**
     * Adds a task for the specified username.
     *
     * @param username the username to associate the task with
     * @param task the task to add
     */
    public void addTask(String username, Task task) {
        this.currentUsername = username;
        taskMap.put(task.getTaskInfo().getId(), task);
    }

    /**
     * Marks the task as complete for the given username and task ID.
     *
     * @param username the username of the user
     * @param taskId the ID of the task to mark complete
     */
    @Override
    public void markAsComplete(String username, TaskID taskId) {
        final Task task = taskMap.get(taskId);
        if (task != null) {
            task.getTaskInfo().setTaskStatus("Complete");
        }
    }

    /**
     * Deletes the task with the specified task ID for the given username.
     *
     * @param username the username of the user
     * @param taskId the ID of the task to delete
     */
    @Override
    public void deleteTask(String username, TaskID taskId) {
        taskMap.remove(taskId);
    }

    /**
     * Retrieves a task by email and ID.
     *
     * @param email the email of the user
     * @param id the task ID
     * @return the task if found, null otherwise
     */
    @Override
    public Task getTaskByIdAndEmail(String email, TaskID id) {
        return taskMap.get(id);
    }

    /**
     * Retrieves a task by username and task ID.
     *
     * @param username the username of the user
     * @param taskId the task ID
     * @return the task if found, null otherwise
     */
    @Override
    public Task getTaskById(String username, TaskID taskId) {
        return taskMap.get(taskId);
    }

    /**
     * Updates an existing task.
     *
     * @param username the username of the user
     * @param task the updated task
     */
    @Override
    public void updateTask(String username, Task task) {
        taskMap.put(task.getTaskInfo().getId(), task);
    }

    /**
     * Returns the current username.
     *
     * @return the current username
     */
    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Sets the current username.
     *
     * @param currentUsername the username to set
     */
    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }
}
