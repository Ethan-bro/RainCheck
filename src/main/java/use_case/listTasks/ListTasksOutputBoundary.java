package use_case.listTasks;

import entity.Task;

import java.util.List;

/**
 * Output boundary interface for the ListTasks use case.
 * Defines the method to present a list of tasks after they are retrieved.
 */
public interface ListTasksOutputBoundary {

    /**
     * Presents the given list of tasks.
     *
     * @param tasks the list of tasks to present
     */
    void presentTasks(List<Task> tasks);
}

