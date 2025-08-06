package use_case.editTask;
import entity.Task;
import entity.TaskID;

public interface EditTaskDataAccessInterface {

    Task getTaskByIdAndEmail(String email, TaskID id);

    Task getTaskById(String username, TaskID taskId);

    void updateTask(String username, Task task);

}
