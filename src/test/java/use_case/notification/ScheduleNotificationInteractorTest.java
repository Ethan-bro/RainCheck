package use_case.notification;

import entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleNotificationInteractorTest {

    private MockNotificationDataAccess notificationDataAccess;
    private MockTaskDataAccess taskDataAccess;
    private MockOutputBoundary outputBoundary;
    private ScheduleNotificationInteractor interactor;

    @BeforeEach
    void setUp() {
        notificationDataAccess = new MockNotificationDataAccess();
        taskDataAccess = new MockTaskDataAccess();
        outputBoundary = new MockOutputBoundary();

        // Fix: Constructor takes 3 arguments, not 4
        interactor = new ScheduleNotificationInteractor(notificationDataAccess, taskDataAccess, outputBoundary);
    }

    @Test
    void testScheduleNotificationSuccess() {
        // Given
        String username = "testuser";
        String taskId = UUID.randomUUID().toString();
        EmailNotificationConfig emailConfig = new EmailNotificationConfig("test@example.com", true);
        notificationDataAccess.setEmailConfig(emailConfig);

        Task task = createTestTask();
        taskDataAccess.setTask(task);

        Reminder reminder = new Reminder(30); // 30 minutes before
        ScheduleNotificationInputData inputData = new ScheduleNotificationInputData(taskId, username, reminder);

        // When
        interactor.scheduleNotification(inputData);

        // Then
        ScheduleNotificationOutputData result = outputBoundary.getLastResult();
        assertNotNull(result);
        assertTrue(result.success());
        assertNotNull(result.notificationId());
        assertEquals("Notification scheduled successfully", result.message());
    }

    @Test
    void testScheduleNotificationEmailNotEnabled() {
        // Given
        String username = "testuser";
        String taskId = UUID.randomUUID().toString();
        EmailNotificationConfig emailConfig = new EmailNotificationConfig("test@example.com", false);
        notificationDataAccess.setEmailConfig(emailConfig);

        Reminder reminder = new Reminder(30);
        ScheduleNotificationInputData inputData = new ScheduleNotificationInputData(taskId, username, reminder);

        // When
        interactor.scheduleNotification(inputData);

        // Then
        ScheduleNotificationOutputData result = outputBoundary.getLastResult();
        assertNotNull(result);
        assertFalse(result.success());
        assertNull(result.notificationId());
        assertEquals("Email notifications not enabled for user", result.message());
    }

    @Test
    void testScheduleNotificationNoEmailConfig() {
        // Given
        String username = "testuser";
        String taskId = UUID.randomUUID().toString();
        notificationDataAccess.setEmailConfig(null);

        Reminder reminder = new Reminder(30);
        ScheduleNotificationInputData inputData = new ScheduleNotificationInputData(taskId, username, reminder);

        // When
        interactor.scheduleNotification(inputData);

        // Then
        ScheduleNotificationOutputData result = outputBoundary.getLastResult();
        assertNotNull(result);
        assertFalse(result.success());
        assertNull(result.notificationId());
        assertEquals("Email notifications not enabled for user", result.message());
    }

    @Test
    void testScheduleNotificationTaskNotFound() {
        // Given
        String username = "testuser";
        String taskId = UUID.randomUUID().toString();
        EmailNotificationConfig emailConfig = new EmailNotificationConfig("test@example.com", true);
        notificationDataAccess.setEmailConfig(emailConfig);

        taskDataAccess.setTask(null); // Task not found

        Reminder reminder = new Reminder(30);
        ScheduleNotificationInputData inputData = new ScheduleNotificationInputData(taskId, username, reminder);

        // When
        interactor.scheduleNotification(inputData);

        // Then
        ScheduleNotificationOutputData result = outputBoundary.getLastResult();
        assertNotNull(result);
        assertFalse(result.success());
        assertNull(result.notificationId());
        assertEquals("Task not found", result.message());
    }

    @Test
    void testScheduleNotificationPastTime() {
        // Given
        String username = "testuser";
        String taskId = UUID.randomUUID().toString();
        EmailNotificationConfig emailConfig = new EmailNotificationConfig("test@example.com", true);
        notificationDataAccess.setEmailConfig(emailConfig);

        // Create task with past start time
        Task task = createTestTaskWithTime(LocalDateTime.now().minusHours(1));
        taskDataAccess.setTask(task);

        Reminder reminder = new Reminder(30);
        ScheduleNotificationInputData inputData = new ScheduleNotificationInputData(taskId, username, reminder);

        // When
        interactor.scheduleNotification(inputData);

        // Then
        ScheduleNotificationOutputData result = outputBoundary.getLastResult();
        assertNotNull(result);
        assertFalse(result.success());
        assertNull(result.notificationId());
        assertEquals("Cannot schedule notification for past time", result.message());
    }

    @Test
    void testScheduleNotificationWithException() {
        // Given
        String username = "testuser";
        String taskId = UUID.randomUUID().toString();
        notificationDataAccess.setShouldThrowException(true);
        notificationDataAccess.setExceptionMessage("Database error");

        Reminder reminder = new Reminder(30);
        ScheduleNotificationInputData inputData = new ScheduleNotificationInputData(taskId, username, reminder);

        // When
        interactor.scheduleNotification(inputData);

        // Then
        ScheduleNotificationOutputData result = outputBoundary.getLastResult();
        assertNotNull(result);
        assertFalse(result.success());
        assertNull(result.notificationId());
        assertTrue(result.message().contains("Error scheduling notification: Database error"));
    }

    @Test
    void testScheduleNotificationInvalidTaskId() {
        // Given
        String username = "testuser";
        String invalidTaskId = "invalid-uuid";
        EmailNotificationConfig emailConfig = new EmailNotificationConfig("test@example.com", true);
        notificationDataAccess.setEmailConfig(emailConfig);

        Reminder reminder = new Reminder(30);
        ScheduleNotificationInputData inputData = new ScheduleNotificationInputData(invalidTaskId, username, reminder);

        // When
        interactor.scheduleNotification(inputData);

        // Then
        ScheduleNotificationOutputData result = outputBoundary.getLastResult();
        assertNotNull(result);
        assertFalse(result.success());
        assertNull(result.notificationId());
        assertTrue(result.message().contains("Error scheduling notification"));
    }

    @Test
    void testScheduleNotificationTaskDataAccessException() {
        // Given
        String username = "testuser";
        String taskId = UUID.randomUUID().toString();
        EmailNotificationConfig emailConfig = new EmailNotificationConfig("test@example.com", true);
        notificationDataAccess.setEmailConfig(emailConfig);

        taskDataAccess.setShouldThrowException(true);
        taskDataAccess.setExceptionMessage("Task access error");

        Reminder reminder = new Reminder(30);
        ScheduleNotificationInputData inputData = new ScheduleNotificationInputData(taskId, username, reminder);

        // When
        interactor.scheduleNotification(inputData);

        // Then
        ScheduleNotificationOutputData result = outputBoundary.getLastResult();
        assertNotNull(result);
        assertFalse(result.success());
        assertNull(result.notificationId());
        assertTrue(result.message().contains("Error scheduling notification"));
    }

    @Test
    void testScheduleNotificationSaveException() {
        // Given
        String username = "testuser";
        String taskId = UUID.randomUUID().toString();
        EmailNotificationConfig emailConfig = new EmailNotificationConfig("test@example.com", true);
        notificationDataAccess.setEmailConfig(emailConfig);

        Task task = createTestTask();
        taskDataAccess.setTask(task);

        Reminder reminder = new Reminder(30);
        ScheduleNotificationInputData inputData = new ScheduleNotificationInputData(taskId, username, reminder);

        // Now test save exception
        notificationDataAccess.setShouldThrowException(true);
        notificationDataAccess.setExceptionMessage("Save failed");

        interactor.scheduleNotification(inputData);

        // Then
        ScheduleNotificationOutputData result = outputBoundary.getLastResult();
        assertNotNull(result);
        assertFalse(result.success());
        assertTrue(result.message().contains("Error scheduling notification"));
    }

    private Task createTestTask() {
        return createTestTaskWithTime(LocalDateTime.now().plusHours(2));
    }

    private Task createTestTaskWithTime(LocalDateTime startTime) {
        // Fix: TaskInfo has no args constructor, use builder pattern
        TaskInfo taskInfo = new TaskInfo();
        TaskID taskId = TaskID.from(UUID.randomUUID());

        // Fix: Use the proper initialization methods
        taskInfo.setCoreDetails(taskId, "Test Task", startTime, startTime.plusHours(1));
        taskInfo.setAdditionalDetails(Priority.MEDIUM, null, null, "No");
        taskInfo.setWeatherInfo("Sunny", "sun", "20", "3");

        // Fix: Task constructor takes just TaskInfo
        return new Task(taskInfo);
    }
}