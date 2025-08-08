package use_case.notification;

import entity.Reminder;

/**
 * Input data for scheduling email notifications.
 *
 * @param taskId   the ID of the task for which the notification is scheduled
 * @param username the username of the user receiving the notification
 * @param reminder the reminder details associated with the notification
 */
public record ScheduleNotificationInputData(String taskId, String username, Reminder reminder) {
}
