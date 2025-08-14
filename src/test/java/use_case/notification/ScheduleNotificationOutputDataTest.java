package use_case.notification;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleNotificationOutputDataTest {

    @Test
    void testOutputDataCreationSuccess() {
        // Given
        String notificationId = "notification-123";
        boolean success = true;
        String message = "Success message";

        // When
        ScheduleNotificationOutputData outputData = new ScheduleNotificationOutputData(notificationId, success, message);

        // Then
        assertEquals(notificationId, outputData.notificationId());
        assertTrue(outputData.success());
        assertEquals(message, outputData.message());
    }

    @Test
    void testOutputDataCreationFailure() {
        // Given
        String notificationId = null;
        boolean success = false;
        String message = "Error message";

        // When
        ScheduleNotificationOutputData outputData = new ScheduleNotificationOutputData(notificationId, success, message);

        // Then
        assertNull(outputData.notificationId());
        assertFalse(outputData.success());
        assertEquals(message, outputData.message());
    }

    @Test
    void testOutputDataEquality() {
        // Given
        ScheduleNotificationOutputData outputData1 = new ScheduleNotificationOutputData("id1", true, "message");
        ScheduleNotificationOutputData outputData2 = new ScheduleNotificationOutputData("id1", true, "message");

        // Then
        assertEquals(outputData1, outputData2);
        assertEquals(outputData1.hashCode(), outputData2.hashCode());
    }
}