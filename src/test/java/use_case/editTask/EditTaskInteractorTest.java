package use_case.editTask;

import data_access.InMemoryTaskDataAccessObject;
import data_access.WeatherApiService;
import data_access.WeatherApiServiceAdapter;
import entity.*;
import interface_adapter.addTask.Constants;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

class EditTaskInteractorTest {

    static String username = "Bob";

    private TaskInfo createTaskInfo(TaskID taskId, String name, LocalDateTime start, LocalDateTime end,
                                    Priority priority, CustomTag tag, Reminder reminder, String isDeleted,
                                    String temp, String uvIndex) {
        TaskInfo info = new TaskInfo();
        info.setCoreDetails(taskId, name, start, end);
        info.setAdditionalDetails(priority, tag, reminder, isDeleted);
        info.setWeatherInfo(name, name, temp, uvIndex); // For simplicity, reuse name for description/icon
        return info;
    }

    @Test
    void successfulEditTask() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        TaskID taskId = new TaskID(UUID.randomUUID());
        TaskInfo originalInfo = createTaskInfo(
                taskId, "Original Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.MEDIUM,
                new CustomTag("Tag", "📚"),
                null,
                "No",
                "25.0",
                "2");
        Task originalTask = new Task(originalInfo);
        dao.addTask(username, originalTask);

        TaskInfo updatedInfo = createTaskInfo(
                taskId,
                "Updated Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                Priority.HIGH,
                new CustomTag("Tag", "📚"),
                null,
                "No",
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

        WeatherApiServiceAdapter adapter = new WeatherApiServiceAdapter(new WeatherApiService());
        EditTaskInteractor interactor = new EditTaskInteractor(dao, presenter, adapter);
        EditTaskInputData input = new EditTaskInputData(username, updatedTask);
        interactor.execute(username, input);
    }

    @Test
    void failEditNonExistentTask() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        TaskID taskId = new TaskID(UUID.randomUUID());
        TaskInfo updatedInfo = createTaskInfo(
                taskId, "Updated Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.LOW,
                new CustomTag("Tag", "👽"),
                null,
                "No",
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

        WeatherApiServiceAdapter adapter = new WeatherApiServiceAdapter(new WeatherApiService());
        EditTaskInteractor interactor = new EditTaskInteractor(dao, presenter, adapter);
        EditTaskInputData input = new EditTaskInputData(username, updatedTask);
        interactor.execute(username, input);
    }

    @Test
    void failEditEmptyTaskName() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        TaskID taskId = new TaskID(UUID.randomUUID());
        TaskInfo originalInfo = createTaskInfo(
                taskId, "Original Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.LOW,
                new CustomTag("Tag", "👽"),
                null,
                "No",
                "25.0",
                "2");
        Task originalTask = new Task(originalInfo);
        dao.addTask(username, originalTask);

        TaskInfo updatedInfo = createTaskInfo(
                taskId, "",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.LOW,
                new CustomTag("Tag", "👽"),
                null,
                "No",
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

        WeatherApiServiceAdapter adapter = new WeatherApiServiceAdapter(new WeatherApiService());
        EditTaskInteractor interactor = new EditTaskInteractor(dao, presenter, adapter);
        EditTaskInputData input = new EditTaskInputData(username, updatedTask);
        interactor.execute(username, input);
    }

    @Test
    void failEditTaskNameTooLong() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        TaskID taskId = new TaskID(UUID.randomUUID());
        TaskInfo originalInfo = createTaskInfo(
                taskId, "Original Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.HIGH,
                new CustomTag("Tag", "🌷"),
                null,
                "No",
                "25.0",
                "2");
        Task originalTask = new Task(originalInfo);
        dao.addTask(username, originalTask);

        TaskInfo updatedInfo = createTaskInfo(
                taskId,
                "UpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdated",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.HIGH,
                new CustomTag("Tag", "🌷"),
                null,
                "No",
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

        WeatherApiServiceAdapter adapter = new WeatherApiServiceAdapter(new WeatherApiService());
        EditTaskInteractor interactor = new EditTaskInteractor(dao, presenter, adapter);
        EditTaskInputData input = new EditTaskInputData(username, updatedTask);
        interactor.execute(username, input);
    }

    @Test
    void failEditEndTimeBeforeStart() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();

        TaskID taskId = new TaskID(UUID.randomUUID());
        TaskInfo originalInfo = createTaskInfo(
                taskId, "Original Task",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Priority.HIGH,
                new CustomTag("Tag", "🌷"),
                null,
                "No",
                "25.0",
                "2");
        Task originalTask = new Task(originalInfo);
        dao.addTask(username, originalTask);

        TaskInfo updatedInfo = createTaskInfo(
                taskId,
                "Updated Task",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now(),
                Priority.HIGH,
                new CustomTag("Tag", "🌷"),
                null,
                "No",
                "25.0",
                "2");
        Task updatedTask = new Task(updatedInfo);

        EditTaskInteractor interactor = getEditTaskInteractor(dao);
        EditTaskInputData input = new EditTaskInputData(username, updatedTask);
        interactor.execute(username, input);
    }

    @NotNull
    private static EditTaskInteractor getEditTaskInteractor(InMemoryTaskDataAccessObject dao) throws IOException {
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

        WeatherApiServiceAdapter adapter = new WeatherApiServiceAdapter(new WeatherApiService());
        return new EditTaskInteractor(dao, presenter, adapter);
    }
}
