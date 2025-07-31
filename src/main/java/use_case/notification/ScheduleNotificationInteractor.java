package use_case.notification;

import entity.*;
import use_case.editTask.EditTaskDataAccessInterface;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Interactor for scheduling email notifications.
 */
public class ScheduleNotificationInteractor {
    private final NotificationDataAccessInterface notificationDataAccess;
    private final EditTaskDataAccessInterface taskDataAccess;
    private final ScheduleNotificationOutputBoundary outputBoundary;

    public ScheduleNotificationInteractor(NotificationDataAccessInterface notificationDataAccess,
                                          EditTaskDataAccessInterface taskDataAccess,
                                          ScheduleNotificationOutputBoundary outputBoundary) {
        this.notificationDataAccess = notificationDataAccess;
        this.taskDataAccess = taskDataAccess;
        this.outputBoundary = outputBoundary;
    }

    public void scheduleNotification(ScheduleNotificationInputData inputData) {
        try {
            // Get user's email configuration
            EmailNotificationConfig emailConfig = notificationDataAccess.getEmailConfig(inputData.getUsername());

            if (emailConfig == null || !emailConfig.isEmailNotificationsEnabled()) {
                outputBoundary.presentScheduleResult(new ScheduleNotificationOutputData(
                        null, false, "Email notifications not enabled for user"));
                return;
            }

            // Create TaskID from UUID string
            TaskID taskId = TaskID.from(UUID.fromString(inputData.getTaskId()));

            // Get the task to schedule notification for
            Task task = taskDataAccess.getTaskById(inputData.getUsername(), taskId);
            if (task == null) {
                outputBoundary.presentScheduleResult(new ScheduleNotificationOutputData(
                        null, false, "Task not found"));
                return;
            }

            // Get scheduled date time from task
            LocalDateTime taskTime = task.getTaskInfo().getStartDateTime();
            LocalDateTime notificationTime = taskTime.minusMinutes(inputData.getReminder().getMinutesBefore());

            // Don't schedule notifications for past times
            if (notificationTime.isBefore(LocalDateTime.now())) {
                outputBoundary.presentScheduleResult(new ScheduleNotificationOutputData(
                        null, false, "Cannot schedule notification for past time"));
                return;
            }

            // Create and save scheduled notification
            ScheduledNotification notification = new ScheduledNotification(
                    inputData.getTaskId(), notificationTime, emailConfig.getUserEmail());

            notificationDataAccess.saveScheduledNotification(notification);

            outputBoundary.presentScheduleResult(new ScheduleNotificationOutputData(
                    notification.getNotificationId(), true, "Notification scheduled successfully"));

        } catch (Exception e) {
            outputBoundary.presentScheduleResult(new ScheduleNotificationOutputData(
                    null, false, "Error scheduling notification: " + e.getMessage()));
        }
    }
}