package use_case.deleteTask;

import data_access.InMemoryTaskDataAccessObject;
import entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class DeleteTaskInteractorTest {

    static String username = "Bob";

    @Test
    void successfulDeleteTask() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        TaskID taskId = new TaskID(UUID.randomUUID());
        TaskInfo taskInfo = new TaskInfo();

        taskInfo.setCoreDetails(
                taskId,
                "Original Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );

        taskInfo.setAdditionalDetails(
                Priority.MEDIUM,
                new CustomTag("Tag", "📚"),
                null,
                "No"
        );

        taskInfo.setWeatherInfo(
                "",
                "",
                "25.0",
                "2"
        );
        Task task = new Task(taskInfo);
        dao.addTask(username, task);

        DeleteTaskOutputBoundary presenter = new DeleteTaskOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteTaskOutputData outputData) {
                Assertions.assertEquals(taskId, outputData.getTaskId());
                Assertions.assertFalse(outputData.isUseCaseFailed());

                // Ensure the task was deleted
                Assertions.assertNull(dao.getTaskById(username, taskId));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                Assertions.fail("Expected successful deletion, but got error: " + errorMessage);
            }
        };

        DeleteTaskInteractor interactor = new DeleteTaskInteractor(dao, presenter);
        DeleteTaskInputData input = new DeleteTaskInputData(username, taskId);
        interactor.execute(input);
    }

    @Test
    void failDeleteNonexistentTask() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        DeleteTaskOutputBoundary presenter = new DeleteTaskOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteTaskOutputData outputData) {
                Assertions.fail("Expected task deletion to fail, but succeeded.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                Assertions.assertEquals("Task not found.", errorMessage);
            }
        };

        DeleteTaskInteractor interactor = new DeleteTaskInteractor(dao, presenter);
        DeleteTaskInputData input = new DeleteTaskInputData(username, null);
        interactor.execute(input);
    }
}
