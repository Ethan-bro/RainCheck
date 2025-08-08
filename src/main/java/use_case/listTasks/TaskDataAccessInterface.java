package use_case.listTasks;

import entity.Task;

import java.time.LocalDate;
import java.util.List;

/**
 * Data access interface for retrieving and adding tasks within a date range for a user.
 */
public interface TaskDataAccessInterface {

    /**
     * Retrieves a list of tasks for the given user that fall within the specified date range.
     *
     * @param username the username of the user whose tasks to retrieve
     * @param startDate the start date of the range (inclusive)
     * @param endDate the end date of the range (inclusive)
     * @return a list of tasks within the specified date range
     */
    List<Task> getTasksByDateRange(String username, LocalDate startDate, LocalDate endDate);

    /**
     * Adds a new task for the given user.
     *
     * @param username the username of the user to add the task for
     * @param task the task to add
     */
    void addTask(String username, Task task);

}
