package use_case.task;

import java.time.LocalDate;
import java.util.List;

public class ListTasksInteractor implements ListTasksInputBoundary{
    private final TaskDataAccessInterface taskDao;
    private final ListTasksOutputBoundary presenter;

    public ListTasksInteractor(TaskDataAccessInterface taskDao, ListTasksOutputBoundary presenter) {
        this.taskDao = taskDao;
        this.presenter = presenter;
    }

    @Override
    public void listTasks(String username, LocalDate startDate, LocalDate endDate) {
        List<Task> tasks = taskDao.getTasksByDateRange(username, startDate, endDate);
        presenter.presentTasks(tasks);
    }
}
