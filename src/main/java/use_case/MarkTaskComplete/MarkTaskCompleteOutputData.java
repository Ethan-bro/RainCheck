package use_case.MarkTaskComplete;

import entity.TaskID;

/**
 * Output Data for the MarkTaskComplete Use Case.
 */
public class MarkTaskCompleteOutputData {
     private final TaskID taskId;
     private final boolean useCaseFailed;

     public MarkTaskCompleteOutputData(TaskID taskId, boolean useCaseFailed) {
         this.taskId = taskId;
         this.useCaseFailed = useCaseFailed;
     }

     public TaskID getTaskId() {
         return taskId;
     }

     public boolean isUseCaseFailed() {
         return useCaseFailed;
     }
}
