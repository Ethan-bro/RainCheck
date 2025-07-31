package use_case.notification;

/**
 * Output data for notification scheduling results.
 */
public class ScheduleNotificationOutputData {
    private final String notificationId;
    private final boolean success;
    private final String message;

    public ScheduleNotificationOutputData(String notificationId, boolean success, String message) {
        this.notificationId = notificationId;
        this.success = success;
        this.message = message;
    }

    public String getNotificationId() { return notificationId; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
