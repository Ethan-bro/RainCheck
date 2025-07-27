package use_case.MarkTaskComplete;

/**
 * Output Data for the MarkTaskComplete Use Case.
 */
public class MarkTaskCompleteOutputData {
     private final int taskId;
     private final boolean useCaseFailed;

     public MarkTaskCompleteOutputData(int taskId, boolean useCaseFailed) {
         this.taskId = taskId;
         this.useCaseFailed = useCaseFailed;
     }

     public int getTaskId() {
         return taskId;
     }

     public boolean isUseCaseFailed() {
         return useCaseFailed;
     }
}
