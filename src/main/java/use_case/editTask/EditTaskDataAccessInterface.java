package use_case.editTask;
import entity.Task;
import entity.TaskID;

import java.io.IOException;

public interface EditTaskDataAccessInterface {

    Task getTaskById(String username, TaskID taskId);

    void updateTask(String username, Task task);

}
