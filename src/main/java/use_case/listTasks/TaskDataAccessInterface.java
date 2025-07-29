package use_case.listTasks;

import entity.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskDataAccessInterface {

    List<Task> getTasksByDateRange(String username, LocalDate startDate, LocalDate endDate);

    void addTask(String username, Task task);


}
