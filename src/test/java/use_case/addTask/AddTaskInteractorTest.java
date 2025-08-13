package use_case.addTask;

import data_access.InMemoryTaskDataAccessObject;
import data_access.WeatherApiService;
import entity.*;
import interface_adapter.addTask.TaskIDGenerator;
import interface_adapter.addTask.UuidGenerator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import use_case.notification.ScheduleNotificationInputData;
import use_case.notification.ScheduleNotificationInteractor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Unit tests for AddTaskInteractor
 */
class AddTaskInteractorTest {

    static final String username = "Bob";

    private AddTaskInputData makeInput(String name,
                                       LocalDateTime start,
                                       LocalDateTime end,
                                       Priority priority,
                                       CustomTag tag,
                                       Reminder reminder) {
        return new AddTaskInputData(name, start, end, priority, tag, reminder);
    }

    /**
     * Simple test-double that records whether scheduleNotification was called and with what input.
     */
    private static class NotificationRecorderInteractor extends ScheduleNotificationInteractor {
        public boolean called = false;
        public ScheduleNotificationInputData lastInput = null;

        public NotificationRecorderInteractor() {
            super(null, null, null); // we override the behaviour used in tests
        }

        @Override
        public void scheduleNotification(ScheduleNotificationInputData inputData) {
            this.called = true;
            this.lastInput = inputData;
        }
    }

    @Test
    void successfulAddTaskWithoutReminder() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();
        TaskIDGenerator idGen = new UuidGenerator();

        // recorder that should NOT be called for Reminder.NONE
        NotificationRecorderInteractor recorder = new NotificationRecorderInteractor();

        AddTaskInteractor interactor = getAddTaskInteractor(dao, idGen, recorder);

        LocalDateTime now = LocalDateTime.now().plusMinutes(1);
        AddTaskInputData input = makeInput(
                "New Task",
                now,
                now.plusHours(1),
                Priority.MEDIUM,
                new CustomTag("Work", "\uD83D\uDCBC"),
                Reminder.NONE
        );

        interactor.execute(input, username);

        // notification should NOT have been scheduled
        Assertions.assertFalse(recorder.called, "Notification should NOT be scheduled for Reminder.NONE");
    }

    @NotNull
    private static AddTaskInteractor getAddTaskInteractor(InMemoryTaskDataAccessObject dao, TaskIDGenerator idGen, NotificationRecorderInteractor recorder) throws IOException {
        AddTaskOutputBoundary presenter = new AddTaskOutputBoundary() {
            @Override
            public void prepareFailView(AddTaskOutputData failedOutputData) {
                Assertions.fail("Expected success but got failure: " + failedOutputData.getErrorMessage());
            }

            @Override
            public void prepareSuccessView(AddTaskOutputData successOutputData) {
                Assertions.assertNotNull(successOutputData.getNewTask());
                Task saved = dao.getTaskById(username, successOutputData.getNewTask().getTaskInfo().getId());
                Assertions.assertNotNull(saved, "Task should be saved in DAO");
                Assertions.assertEquals(successOutputData.getNewTask().getTaskInfo().getTaskName(),
                        saved.getTaskInfo().getTaskName());
            }
        };

        WeatherApiService weather = new WeatherApiService();
        return new AddTaskInteractor(dao, idGen, presenter, weather, recorder);
    }

    @Test
    void successfulAddTaskSchedulesNotificationWhenReminderSet() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();
        TaskIDGenerator idGen = new UuidGenerator();

        NotificationRecorderInteractor recorder = new NotificationRecorderInteractor();

        AddTaskInteractor interactor = getTaskInteractor(dao, idGen, recorder);

        LocalDateTime start = LocalDateTime.now().plusHours(2);
        AddTaskInputData input = makeInput(
                "Notify Task",
                start,
                start.plusHours(1),
                Priority.LOW,
                null,
                new Reminder(10) // non-NONE reminder
        );

        interactor.execute(input, username);

        // verify the notification interactor was called and the input contains the expected username
        Assertions.assertTrue(recorder.called, "Notification interactor should be called for non-NONE reminder");
        ScheduleNotificationInputData calledInput = recorder.lastInput;
        Assertions.assertNotNull(calledInput, "Notification input should be present");
        Assertions.assertEquals(username, calledInput.username(), "Notification input must carry username");
        // taskId string should be parseable as UUID
        Assertions.assertDoesNotThrow(() -> UUID.fromString(calledInput.taskId()));
    }

    @NotNull
    private static AddTaskInteractor getTaskInteractor(InMemoryTaskDataAccessObject dao, TaskIDGenerator idGen, NotificationRecorderInteractor recorder) throws IOException {
        AddTaskOutputBoundary presenter = new AddTaskOutputBoundary() {
            @Override
            public void prepareFailView(AddTaskOutputData failedOutputData) {
                Assertions.fail("Expected success but got failure: " + failedOutputData.getErrorMessage());
            }

            @Override
            public void prepareSuccessView(AddTaskOutputData successOutputData) {
                Assertions.assertNotNull(successOutputData.getNewTask());
                Task saved = dao.getTaskById(username, successOutputData.getNewTask().getTaskInfo().getId());
                Assertions.assertNotNull(saved);
            }
        };

        WeatherApiService weather = new WeatherApiService();
        return new AddTaskInteractor(dao, idGen, presenter, weather, recorder);
    }

    @Test
    void failAddEmptyName() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();
        TaskIDGenerator idGen = new UuidGenerator();
        NotificationRecorderInteractor recorder = new NotificationRecorderInteractor();

        AddTaskInteractor interactor = getInteractor(dao, idGen, recorder);

        LocalDateTime now = LocalDateTime.now().plusMinutes(1);
        AddTaskInputData emptyName = makeInput(
                "",
                now,
                now.plusHours(1),
                Priority.LOW,
                null,
                Reminder.NONE
        );

        interactor.execute(emptyName, username);

        Assertions.assertFalse(recorder.called, "No notification should be scheduled on validation failure");
    }

    @NotNull
    private static AddTaskInteractor getInteractor(InMemoryTaskDataAccessObject dao, TaskIDGenerator idGen, NotificationRecorderInteractor recorder) throws IOException {
        AddTaskOutputBoundary presenter = new AddTaskOutputBoundary() {
            @Override
            public void prepareFailView(AddTaskOutputData failedOutputData) {
                Assertions.assertTrue(failedOutputData.isAddTaskFailed());
                Assertions.assertNotNull(failedOutputData.getErrorMessage());
            }

            @Override
            public void prepareSuccessView(AddTaskOutputData successOutputData) {
                Assertions.fail("Expected failure due to empty name but got success");
            }
        };

        WeatherApiService weather = new WeatherApiService();
        return new AddTaskInteractor(dao, idGen, presenter, weather, recorder);
    }

    @Test
    void failAddEndBeforeStart() throws IOException {
        InMemoryTaskDataAccessObject dao = new InMemoryTaskDataAccessObject();
        TaskIDGenerator idGen = new UuidGenerator();
        NotificationRecorderInteractor recorder = new NotificationRecorderInteractor();

        AddTaskInteractor interactor = getAddTaskInteractor1(dao, idGen, recorder);

        LocalDateTime now = LocalDateTime.now().plusHours(2);
        AddTaskInputData badTimes = makeInput(
                "Bad Times",
                now.plusHours(2),
                now, // end before start
                Priority.MEDIUM,
                null,
                Reminder.NONE
        );

        interactor.execute(badTimes, username);
        Assertions.assertFalse(recorder.called, "No notification scheduling on validation failure");
    }

    @NotNull
    private static AddTaskInteractor getAddTaskInteractor1(InMemoryTaskDataAccessObject dao, TaskIDGenerator idGen, NotificationRecorderInteractor recorder) throws IOException {
        AddTaskOutputBoundary presenter = new AddTaskOutputBoundary() {
            @Override
            public void prepareFailView(AddTaskOutputData failedOutputData) {
                Assertions.assertTrue(failedOutputData.isAddTaskFailed());
                Assertions.assertNotNull(failedOutputData.getErrorMessage());
            }

            @Override
            public void prepareSuccessView(AddTaskOutputData successOutputData) {
                Assertions.fail("Expected failure because end time is before start");
            }
        };

        WeatherApiService weather = new WeatherApiService();
        return new AddTaskInteractor(dao, idGen, presenter, weather, recorder);
    }
}
