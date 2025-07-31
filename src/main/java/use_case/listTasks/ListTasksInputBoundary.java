package use_case.listTasks;

import java.time.LocalDate;

public interface ListTasksInputBoundary {
    void listTasks(String username, LocalDate startDate, LocalDate endDate);
}
