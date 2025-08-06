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
     */
    void saveEmailConfig(String username, EmailNotificationConfig config);

    /**
     * Get email notification configuration for a user.
     */
    EmailNotificationConfig getEmailConfig(String username);

    /**
     * Save a scheduled notification.
     */
    void saveScheduledNotification(ScheduledNotification notification);

    /**
     * Get all pending notifications scheduled before the given time.
     */
    List<ScheduledNotification> getPendingNotifications(LocalDateTime beforeTime);

    /**
     * Mark a notification as sent.
     */
    void markNotificationAsSent(String notificationId);

    /**
     * Delete a scheduled notification.
     */
    void deleteScheduledNotification(String notificationId);

    /**
     * Get all scheduled notifications for a specific task.
     */
    List<ScheduledNotification> getNotificationsForTask(String taskId);
}