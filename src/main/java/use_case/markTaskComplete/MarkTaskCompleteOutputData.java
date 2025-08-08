package use_case.markTaskComplete;

import entity.TaskID;

/**
 * Output Data for the markTaskComplete Use Case.
 *
 * @param taskId the ID of the task being marked complete
 * @param useCaseFailed true if the use case failed, false otherwise
 */
public record MarkTaskCompleteOutputData(TaskID taskId, boolean useCaseFailed) {

}
