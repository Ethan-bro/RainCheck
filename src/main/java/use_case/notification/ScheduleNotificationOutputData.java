package use_case.notification;

/**
 * Output data for notification scheduling results.
 *
 * @param notificationId the unique identifier of the notification
 * @param success       indicates whether the scheduling was successful
 * @param message       additional information or error message related to scheduling
 */
public record ScheduleNotificationOutputData(String notificationId, boolean success, String message) {
}
