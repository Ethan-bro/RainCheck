package use_case.listTasks;

import java.time.LocalDate;
import java.util.List;

import entity.Task;

public class ListTasksInteractor implements ListTasksInputBoundary {
    private final TaskDataAccessInterface taskDao;
    private final ListTasksOutputBoundary presenter;

    /**
     * Constructs a ListTasksInteractor.
     *
     * @param taskDao the data access interface for tasks
     * @param presenter the output boundary to present results
     */
    public ListTasksInteractor(TaskDataAccessInterface taskDao, ListTasksOutputBoundary presenter) {
        this.taskDao = taskDao;
        this.presenter = presenter;
    }

    /**
     * Retrieves and presents tasks for a user within a specified date range.
     *
     * @param username the username of the user whose tasks are listed
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     */
    @Override
    public void listTasks(String username, LocalDate startDate, LocalDate endDate) {
        final List<Task> tasks = taskDao.getTasksByDateRange(username, startDate, endDate);
        presenter.presentTasks(tasks);
    }
}
