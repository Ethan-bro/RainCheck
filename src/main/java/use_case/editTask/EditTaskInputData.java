package use_case.editTask;

import entity.Task;

/**
 * The Input Data for editing a task.
 *
 * @param username the username of the user editing the task
 * @param updatedTask the task object containing updated information
 */
public record EditTaskInputData(String username, Task updatedTask) {

}
