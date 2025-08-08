package use_case.notification;

import entity.EmailNotificationConfig;
import entity.ScheduledNotification;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data access interface for notification persistence.
 */
public interface NotificationDataAccessInterface {

    /**
     * Save email notification configuration for a user.
     *
     * @param username the username of the user
     * @param config the email notification configuration to save
     */
    void saveEmailConfig(String username, EmailNotificationConfig config);

    /**
     * Get email notification configuration for a user.
     *
     * @param username the username of the user
     * @return the email notification configuration, or null if not found
     */
    EmailNotificationConfig getEmailConfig(String username);

    /**
     * Save a scheduled notification.
     *
     * @param notification the scheduled notification to save
     */
    void saveScheduledNotification(ScheduledNotification notification);

    /**
     * Get all pending notifications scheduled before the given time.
     *
     * @param beforeTime the cutoff time to find notifications scheduled before
     * @return list of scheduled notifications pending before the specified time
     */
    List<ScheduledNotification> getPendingNotifications(LocalDateTime beforeTime);

    /**
     * Mark a notification as sent.
     *
     * @param notificationId the ID of the notification to mark as sent
     */
    void markNotificationAsSent(String notificationId);

    /**
     * Delete a scheduled notification.
     *
     * @param notificationId the ID of the notification to delete
     */
    void deleteScheduledNotification(String notificationId);

    /**
     * Get all scheduled notifications for a specific task.
     *
     * @param taskId the ID of the task
     * @return list of scheduled notifications associated with the task
     */
    List<ScheduledNotification> getNotificationsForTask(String taskId);
}

