package use_case.notification;

import entity.Reminder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleNotificationInputDataTest {

    @Test
    void testInputDataCreation() {
        // Given
        String taskId = "test-task-id";
        String username = "testuser";
        Reminder reminder = new Reminder(30);

        // When
        ScheduleNotificationInputData inputData = new ScheduleNotificationInputData(taskId, username, reminder);

        // Then
        assertEquals(taskId, inputData.taskId());
        assertEquals(username, inputData.username());
        assertEquals(reminder, inputData.reminder());
    }

    @Test
    void testInputDataEquality() {
        // Given
        Reminder reminder = new Reminder(30);
        ScheduleNotificationInputData inputData1 = new ScheduleNotificationInputData("task1", "user1", reminder);
        ScheduleNotificationInputData inputData2 = new ScheduleNotificationInputData("task1", "user1", reminder);

        // Then
        assertEquals(inputData1, inputData2);
        assertEquals(inputData1.hashCode(), inputData2.hashCode());
    }
}