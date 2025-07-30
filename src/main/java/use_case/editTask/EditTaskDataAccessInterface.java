package use_case.editTask;
import entity.Task;
import entity.TaskID;

public interface EditTaskDataAccessInterface {

    Task getTaskById(String username, TaskID taskId);

    void updateTask(String username, Task task);

}
