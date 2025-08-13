package entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity representing a scheduled email notification for a task.
 */
public class ScheduledNotification {

    private final String notificationId;
    private final String taskId;
    private final LocalDateTime scheduledTime;
    private final String userEmail;
    private final boolean sent;

    /**
     * Constructs a new ScheduledNotification with a generated notification ID
     * and sent flag set to false.
     *
     * @param taskId        the associated task's ID
     * @param scheduledTime the time the notification is scheduled for
     * @param userEmail     the email address to send the notification to
     */
    public ScheduledNotification(String taskId, LocalDateTime scheduledTime, String userEmail) {
        this.notificationId = UUID.randomUUID().toString();
        this.taskId = taskId;
        this.scheduledTime = scheduledTime;
        this.userEmail = userEmail;
        this.sent = false;
    }

    /**
     * Constructor used for loading existing notifications (e.g., from JSON).
     *
     * @param notificationId the unique notification ID
     * @param taskId         the associated task's ID
     * @param scheduledTime  the scheduled notification time
     * @param userEmail      the target email address
     * @param sent           whether the notification has been sent
     */
    @JsonCreator
    public ScheduledNotification(@JsonProperty("notificationId") String notificationId,
                                 @JsonProperty("taskId") String taskId,
                                 @JsonProperty("scheduledTime") LocalDateTime scheduledTime,
                                 @JsonProperty("userEmail") String userEmail,
                                 @JsonProperty("sent") boolean sent) {
        this.notificationId = notificationId;
        this.taskId = taskId;
        this.scheduledTime = scheduledTime;
        this.userEmail = userEmail;
        this.sent = sent;
    }

    /**
     * Gets the notification ID.
     * @return the notification ID
     */
    public String getNotificationId() {
        return notificationId;
    }

    /**
     * Gets the associated task ID.
     * @return the task ID
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * Gets the scheduled notification time.
     * @return the scheduled time
     */
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    /**
     * Gets the user email address for the notification.
     * @return the user email
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Checks if the notification has been sent.
     * @return true if sent, false otherwise
     */
    public boolean isSent() {
        return sent;
    }

    /**
     * Returns a new ScheduledNotification marked as sent.
     *
     * @return a new ScheduledNotification instance with sent = true
     */
    public ScheduledNotification markAsSent() {
        return new ScheduledNotification(notificationId, taskId, scheduledTime, userEmail, true);
    }
}
