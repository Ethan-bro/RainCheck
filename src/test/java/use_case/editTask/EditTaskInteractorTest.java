package use_case.editTask;

import data_access.InMemoryTaskDataAccessObject;
import data_access.WeatherApiService;
import entity.*;
import interface_adapter.addTask.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class EditTaskInteractorTest {

    static String username = "Bob";

    @Test
    void successfulEditTask() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        // Creating original task
        TaskID taskId = new TaskID(UUID.randomUUID());
        TaskInfo originalInfo = new TaskInfo(
                taskId, "Original Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.MEDIUM,
                new CustomTag("Tag", "📚"),
                null,
                "No",
                "",
                "",
                "25.0",
                "2");
        Task originalTask = new Task(originalInfo);
        dao.addTask(username, originalTask);

        // Creating updated task with same ID
        TaskInfo updatedInfo = new TaskInfo(
                taskId,
                "Updated Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                Priority.HIGH,
                new CustomTag("Tag", "📚"),
                null,
                "No",
                "",
                "",
                "25.0",
                "2");
        Task updatedTask = new Task(updatedInfo);

        EditTaskOutputBoundary presenter = new EditTaskOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTaskOutputData outputData) {
                Task result = dao.getTaskById(username, taskId);
                Assertions.assertNotNull(result);
                Assertions.assertEquals("Updated Task", result.getTaskInfo().getTaskName());
                Assertions.assertEquals(Priority.HIGH, result.getTaskInfo().getPriority());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                Assertions.fail("Expected success but got failure: " + errorMessage);
            }
        };

        EditTaskInteractor interactor = new EditTaskInteractor(dao, presenter, new WeatherApiService());
        EditTaskInputData input = new EditTaskInputData(username, updatedTask);
        interactor.execute(username, input);
    }

    @Test
    void failEditNonExistentTask() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        // Creating updated version of task that has ID that doesn't exist in dao
        TaskID taskId = new TaskID(UUID.randomUUID());
        TaskInfo updatedInfo = new TaskInfo(
                taskId, "Updated Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.LOW,
                new CustomTag("Tag", "👽"),
                null,
                "No",
                "",
                "",
                "25.0",
                "2");
        Task updatedTask = new Task(updatedInfo);

        EditTaskOutputBoundary presenter = new EditTaskOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTaskOutputData outputData) {
                Assertions.fail("Expected task update to fail, but it succeeded");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                Assertions.assertEquals("Task not found.", errorMessage);
            }
        };
        EditTaskInteractor interactor = new EditTaskInteractor(dao, presenter, new WeatherApiService());
        EditTaskInputData input = new EditTaskInputData(username, updatedTask);
        interactor.execute(username, input);
    }

    @Test
    void failEditEmptyTaskName() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        TaskID taskId = new TaskID(UUID.randomUUID());

        TaskInfo originalInfo = new TaskInfo(
                taskId, "Original Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.LOW,
                new CustomTag("Tag", "👽"),
                null,
                "No",
                "",
                "",
                "25.0",
                "2");
        Task originalTask = new Task(originalInfo);
        dao.addTask(username, originalTask);

        TaskInfo updatedInfo = new TaskInfo(
                taskId, "",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.LOW,
                new CustomTag("Tag", "👽"),
                null,
                "No",
                "",
                "",
                "25.0",
                "2");
        Task updatedTask = new Task(updatedInfo);

        EditTaskOutputBoundary presenter = new EditTaskOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTaskOutputData outputData) {
                Assertions.fail("Expected task update to fail, but it succeeded");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                Assertions.assertEquals("Task name cannot be empty.", errorMessage);
            }
        };

        EditTaskInteractor interactor = new EditTaskInteractor(dao, presenter, new WeatherApiService());
        EditTaskInputData input = new EditTaskInputData(username, updatedTask);
        interactor.execute(username, input);
    }

    @Test
    void failEditTaskNameTooLong() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        TaskID taskId = new TaskID(UUID.randomUUID());

        TaskInfo originalInfo = new TaskInfo(
                taskId, "Original Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.HIGH,
                new CustomTag("Tag", "🌷"),
                null,
                "No",
                "",
                "",
                "25.0",
                "2");
        Task originalTask = new Task(originalInfo);
        dao.addTask(username, originalTask);

        TaskInfo updatedInfo = new TaskInfo(
                taskId, "UpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdated",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.HIGH,
                new CustomTag("Tag", "🌷"),
                null,
                "No",
                "",
                "",
                "25.0",
                "2");
        Task updatedTask = new Task(updatedInfo);

        EditTaskOutputBoundary presenter = new EditTaskOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTaskOutputData outputData) {
                Assertions.fail("Expected task update to fail, but it succeeded");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                Assertions.assertTrue(
                        errorMessage.contains("Task name exceeds character limit of " + Constants.CHAR_LIMIT)
                );
            }
        };

        EditTaskInteractor interactor = new EditTaskInteractor(dao, presenter, new WeatherApiService());
        EditTaskInputData input = new EditTaskInputData(username, updatedTask);
        interactor.execute(username, input);
    }

    @Test
    void failEditEndTimeBeforeStart() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();
        TaskID taskId = new TaskID(UUID.randomUUID());

        TaskInfo originalInfo = new TaskInfo(
                taskId, "Original Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.HIGH,
                new CustomTag("Tag", "🌷"),
                null,
                "No",
                "",
                "",
                "25.0",
                "2");
        Task originalTask = new Task(originalInfo);
        dao.addTask(username, originalTask);

        TaskInfo updatedInfo = new TaskInfo(
                taskId, "Updated Task",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now(),
                Priority.HIGH,
                new CustomTag("Tag", "🌷"),
                null,
                "No",
                "",
                "",
                "25.0",
                "2");
        Task updatedTask = new Task(updatedInfo);

        EditTaskOutputBoundary presenter = new EditTaskOutputBoundary() {
            @Override
            public void prepareSuccessView(EditTaskOutputData outputData) {
                Assertions.fail("Expected task update to fail, but it succeeded");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                Assertions.assertEquals("End time must be after start time.", errorMessage);
            }
        };

        EditTaskInteractor interactor = new EditTaskInteractor(dao, presenter, new WeatherApiService());
        EditTaskInputData input = new EditTaskInputData(username, updatedTask);
        interactor.execute(username, input);
    }
}
