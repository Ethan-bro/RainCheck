package use_case.listTasks;

import java.time.LocalDate;

/**
 * Input boundary interface for the ListTasks use case.
 * Provides a method to request a list of tasks for a user within a specified date range.
 */
public interface ListTasksInputBoundary {

    /**
     * Lists tasks for the given user between the specified start and end dates (inclusive).
     *
     * @param username the username whose tasks are to be listed
     * @param startDate the start date of the task listing range (inclusive)
     * @param endDate the end date of the task listing range (inclusive)
     */
    void listTasks(String username, LocalDate startDate, LocalDate endDate);
}
