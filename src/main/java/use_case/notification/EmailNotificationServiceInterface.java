package use_case.notification;

import entity.Reminder;
import entity.ScheduledNotification;
import entity.Task;
import entity.User;

/**
 * Interface for email notification service operations.
 */
public interface EmailNotificationServiceInterface {

    /**
     * Sends an immediate email notification for a task reminder.
     *
     * @param user the user to notify
     * @param task the task related to the reminder
     * @param userEmail the email address to send the notification to
     */
    void sendTaskReminder(User user, Task task, String userEmail);

    /**
     * Schedules an email notification based on task and reminder settings.
     *
     * @param task the task to schedule a reminder for
     * @param userEmail the email address to send the notification to
     * @param reminder the reminder details
     * @return the ScheduledNotification created for the reminder
     */
    ScheduledNotification scheduleEmailReminder(Task task, String userEmail, Reminder reminder);

    /**
     * Cancels a scheduled email notification.
     *
     * @param notificationId the ID of the notification to cancel
     */
    void cancelEmailReminder(String notificationId);

    /**
     * Checks and sends any pending notifications.
     */
    void processPendingNotifications();
}
