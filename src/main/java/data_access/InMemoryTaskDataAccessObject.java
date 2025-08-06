package data_access;

import entity.Task;
import entity.TaskID;
import use_case.markTaskComplete.MarkTaskCompleteDataAccessInterface;
import use_case.deleteTask.DeleteTaskDataAccessInterface;
import use_case.editTask.EditTaskDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskDataAccessObject implements
        EditTaskDataAccessInterface,
        DeleteTaskDataAccessInterface,
        MarkTaskCompleteDataAccessInterface {

    private final Map<TaskID, Task> taskMap = new HashMap<>();
    private String currentUsername;

    public void addTask(String username, Task task) {
        this.currentUsername = username;
        taskMap.put(task.getTaskInfo().getId(), task);
    }

    @Override
    public void markAsComplete(String username, TaskID taskId) {
        Task task = taskMap.get(taskId);
        if (task != null) {
            task.getTaskInfo().setTaskStatus("Complete");
        }
    }

    @Override
    public void deleteTask(String username, TaskID taskId) {
            taskMap.remove(taskId);
        }

    @Override
    public Task getTaskByIdAndEmail(String email, TaskID id) {
        return taskMap.get(id);
    }

    @Override
    public Task getTaskById(String username, TaskID taskId) {
        return taskMap.get(taskId);
    }

    @Override
    public void updateTask(String username, Task task) {
        taskMap.put(task.getTaskInfo().getId(), task);
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }
}
