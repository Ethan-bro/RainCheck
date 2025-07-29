package use_case.task;

import entity.Task;
import java.util.List;

public interface ListTasksOutputBoundary {
    void presentTasks(List<Task> tasks);
}
