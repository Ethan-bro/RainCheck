package use_case.markTaskComplete;

import data_access.InMemoryTaskDataAccessObject;
import entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

public class MarkTaskCompleteInteractorTest {

    static final String username = "Bob";

    @Test
    void successfulMarkTaskComplete() {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        TaskID taskId = new TaskID(UUID.randomUUID());
        TaskInfo taskInfo = new TaskInfo(
                taskId, "Original Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.MEDIUM,
                new CustomTag("Tag", "📚"),
                null,
                "Incomplete",  // <-- more explicit than "No"
                "",
                "",
                "25.0",
                "2");
        Task task = new Task(taskInfo);
        dao.addTask(username, task);

        MarkTaskCompleteOutputBoundary presenter = new MarkTaskCompleteOutputBoundary() {
            @Override
            public void prepareSuccessView(MarkTaskCompleteOutputData outputData) {
                Assertions.assertEquals(taskId, outputData.getTaskId());
                Assertions.assertFalse(outputData.isUseCaseFailed());

                Task updatedTask = dao.getTaskById(username, taskId);
                Assertions.assertNotNull(updatedTask);
                Assertions.assertEquals("Complete", updatedTask.getTaskInfo().getTaskStatus());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                Assertions.fail("Expected successful completion, but got error: " + errorMessage);
            }
        };

        MarkTaskCompleteInteractor interactor = new MarkTaskCompleteInteractor(dao, presenter);
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(username, taskId);
        interactor.execute(username, inputData);
    }

    @Test
    void failedMarkNonexistentTaskComplete() {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        MarkTaskCompleteOutputBoundary presenter = new MarkTaskCompleteOutputBoundary() {
            @Override
            public void prepareSuccessView(MarkTaskCompleteOutputData outputData) {
                Assertions.fail("Expected task completion to fail, but succeeded.");

            }

            @Override
            public void prepareFailView(String errorMessage) {
                Assertions.assertEquals("Task not found.", errorMessage);
            }
        };

        MarkTaskCompleteInteractor interactor = new MarkTaskCompleteInteractor(dao, presenter);
        MarkTaskCompleteInputData inputData = new MarkTaskCompleteInputData(username, null);
        interactor.execute(username, inputData);
    }
}
