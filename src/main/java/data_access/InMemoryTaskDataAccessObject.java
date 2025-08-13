package data_access;

import entity.Task;
import entity.TaskID;

import use_case.deleteTask.DeleteTaskDataAccessInterface;
import use_case.editTask.EditTaskDataAccessInterface;
import use_case.markTaskComplete.MarkTaskCompleteDataAccessInterface;
import use_case.listTasks.TaskDataAccessInterface;

import java.time.LocalDate;
import java.util.*;

/**
 * A fake in-memory DAO used only for unit tests. No external I/O.
 * Now stores tasks per-username so it can be used by both EditTask tests
 * and AddTask tests (which call getTasksByDateRange).
 */
public class InMemoryTaskDataAccessObject implements
        EditTaskDataAccessInterface,
        DeleteTaskDataAccessInterface,
        MarkTaskCompleteDataAccessInterface,
        TaskDataAccessInterface {

    // per-username storage: username -> (taskId -> Task)
    private final Map<String, Map<TaskID, Task>> tasksByUser = new HashMap<>();

    private String currentUsername;

    /**
     * Constructs an in-memory task data access object.
     */
    public InMemoryTaskDataAccessObject() {
        this.currentUsername = null;
    }

    /**
     * Adds a task for the specified username.
     *
     * @param username the username to associate the task with
     * @param task     the task to add
     */
    @Override
    public void addTask(String username, Task task) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(task);
        tasksByUser
                .computeIfAbsent(username, _ -> new HashMap<>())
                .put(task.getTaskInfo().getId(), task);
        this.currentUsername = username;
    }

    /**
     * Marks the task as complete for the given username and task ID.
     *
     * @param username the username of the user
     * @param taskId   the ID of the task to mark complete
     */
    @Override
    public void markAsComplete(String username, TaskID taskId) {
        Map<TaskID, Task> userMap = tasksByUser.get(username);
        if (userMap == null) return;
        final Task task = userMap.get(taskId);
        if (task != null) {
            task.getTaskInfo().setTaskStatus("Complete");
        }
    }

    /**
     * Deletes the task with the specified task ID for the given username.
     *
     * @param username the username of the user
     * @param taskId   the ID of the task to delete
     */
    @Override
    public void deleteTask(String username, TaskID taskId) {
        Map<TaskID, Task> userMap = tasksByUser.get(username);
        if (userMap != null) {
            userMap.remove(taskId);
        }
    }

    /**
     * Retrieves a task by email and ID.
     * (For tests that call this signature; in this in-memory dao we treat email same as username.)
     *
     * @param email the email (treated as username here)
     * @param id    the task ID
     * @return the task if found, null otherwise
     */
    @Override
    public Task getTaskByIdAndEmail(String email, TaskID id) {
        Map<TaskID, Task> userMap = tasksByUser.get(email);
        if (userMap == null) return null;
        return userMap.get(id);
    }

    /**
     * Retrieves a task by username and task ID.
     *
     * @param username the username of the user
     * @param taskId   the task ID
     * @return the task if found, null otherwise
     */
    @Override
    public Task getTaskById(String username, TaskID taskId) {
        Map<TaskID, Task> userMap = tasksByUser.get(username);
        if (userMap == null) return null;
        return userMap.get(taskId);
    }

    /**
     * Updates an existing task.
     *
     * @param username the username of the user
     * @param task     the updated task
     */
    @Override
    public void updateTask(String username, Task task) {
        Map<TaskID, Task> userMap = tasksByUser.computeIfAbsent(username, _ -> new HashMap<>());
        userMap.put(task.getTaskInfo().getId(), task);
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

    /**
     * Returns tasks that start within the given date range (inclusive) for the given username.
     * This implements the TaskDataAccessInterface required by AddTaskInteractor.
     * NOTE: filtering is based on task.getTaskInfo().getStartDateTime().toLocalDate()
     *
     * @param username  the username
     * @param startDate inclusive start date
     * @param endDate   inclusive end date
     * @return list of matching tasks (empty list if none)
     */
    @Override
    public synchronized List<Task> getTasksByDateRange(String username, LocalDate startDate, LocalDate endDate) {
        Map<TaskID, Task> userMap = tasksByUser.get(username);
        if (userMap == null || userMap.isEmpty()) return Collections.emptyList();

        List<Task> out = new ArrayList<>();
        for (Task t : userMap.values()) {
            LocalDate s = t.getTaskInfo().getStartDateTime().toLocalDate();
            if ((!s.isBefore(startDate)) && (!s.isAfter(endDate))) {
                out.add(t);
            }
        }
        return out;
    }
}
