package use_case.notification;

import entity.ScheduledNotification;
import entity.Task;
import entity.User;
import entity.Reminder;

/**
 * Interface for email notification service operations.
 */
public interface EmailNotificationServiceInterface {

    /**
     * Sends an immediate email notification for a task reminder.
     */
    void sendTaskReminder(User user, Task task, String userEmail);

    /**
     * Schedules an email notification based on task and reminder settings.
     */
    ScheduledNotification scheduleEmailReminder(Task task, String userEmail, Reminder reminder);

    /**
     * Cancels a scheduled email notification.
     */
    void cancelEmailReminder(String notificationId);

    /**
     * Checks and sends any pending notifications.
     */
    void processPendingNotifications();
}