package use_case.deleteTask;

import entity.TaskID;

/**
 * Input Data for the DeleteTask use case.
 *
 * @param username the username of the user requesting the delete
 * @param taskId the unique identifier of the task to delete
 */
public record DeleteTaskInputData(String username, TaskID taskId) {

}
