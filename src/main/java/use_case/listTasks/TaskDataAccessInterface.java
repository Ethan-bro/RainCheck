package use_case.task;

import entity.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskDataAccessInterface {

    List<okhttp3.internal.concurrent.Task> getTasksByDateRange(String username, LocalDate startDate, LocalDate endDate);
}
