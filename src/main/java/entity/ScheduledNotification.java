package entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a scheduled email notification for a task.
 */
public class ScheduledNotification {
    private final String notificationId;
    private final String taskId;
    private final LocalDateTime scheduledTime;
    private final String userEmail;
    private final boolean sent;

    public ScheduledNotification(String taskId, LocalDateTime scheduledTime, String userEmail) {
        this.notificationId = UUID.randomUUID().toString();
        this.taskId = taskId;
        this.scheduledTime = scheduledTime;
        this.userEmail = userEmail;
        this.sent = false;
    }

    // Constructor for loading existing notifications
    public ScheduledNotification(String notificationId, String taskId, LocalDateTime scheduledTime,
                                 String userEmail, boolean sent) {
        this.notificationId = notificationId;
        this.taskId = taskId;
        this.scheduledTime = scheduledTime;
        this.userEmail = userEmail;
        this.sent = sent;
    }

    // Getters
    public String getNotificationId() { return notificationId; }
    public String getTaskId() { return taskId; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public String getUserEmail() { return userEmail; }
    public boolean isSent() { return sent; }

    public ScheduledNotification markAsSent() {
        return new ScheduledNotification(notificationId, taskId, scheduledTime, userEmail, true);
    }
}
