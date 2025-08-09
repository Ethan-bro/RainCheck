package use_case.notification;

import entity.EmailNotificationConfig;
import entity.ScheduledNotification;
import entity.Task;
import entity.TaskID;

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

    /**
     * Constructs a ScheduleNotificationInteractor.
     *
     * @param notificationDataAccess The data access interface for notification-related data.
     * @param taskDataAccess The data access interface for task-related data.
     * @param outputBoundary The output boundary to present scheduling results.
     */
    public ScheduleNotificationInteractor(
            final NotificationDataAccessInterface notificationDataAccess,
            final EditTaskDataAccessInterface taskDataAccess,
            final ScheduleNotificationOutputBoundary outputBoundary
    ) {
        this.notificationDataAccess = notificationDataAccess;
        this.taskDataAccess = taskDataAccess;
        this.outputBoundary = outputBoundary;
    }

    /**
     * Schedules an email notification for a given task and reminder.
     *
     * @param inputData Input data containing task ID, username, and reminder details.
     */
    public void scheduleNotification(final ScheduleNotificationInputData inputData) {
        final String username = inputData.username();
        final ScheduleNotificationOutputData[] outputData = new ScheduleNotificationOutputData[1];

        try {
            final EmailNotificationConfig emailConfig = notificationDataAccess.getEmailConfig(username);

            if (emailConfig == null || !emailConfig.isEmailNotificationsEnabled()) {
                outputData[0] = new ScheduleNotificationOutputData(
                        null, false, "Email notifications not enabled for user");
            }
            else {
                final TaskID taskId = TaskID.from(UUID.fromString(inputData.taskId()));
                final Task task = taskDataAccess.getTaskById(username, taskId);

                if (task == null) {
                    outputData[0] = new ScheduleNotificationOutputData(
                            null, false, "Task not found");
                }
                else {
                    final LocalDateTime taskTime = task.getTaskInfo().getStartDateTime();
                    final LocalDateTime notificationTime = taskTime.minusMinutes(
                            inputData.reminder().getMinutesBefore()
                    );

                    if (notificationTime.isBefore(LocalDateTime.now())) {
                        outputData[0] = new ScheduleNotificationOutputData(
                                null, false, "Cannot schedule notification for past time");
                    }
                    else {
                        final ScheduledNotification notification = new ScheduledNotification(
                                inputData.taskId(), notificationTime, emailConfig.getUserEmail());

                        notificationDataAccess.saveScheduledNotification(notification);

                        outputData[0] = new ScheduleNotificationOutputData(
                                notification.getNotificationId(), true, "Notification scheduled successfully");
                    }
                }
            }
        }
        catch (IllegalArgumentException ex) {
            outputData[0] = new ScheduleNotificationOutputData(
                    null, false, "Error scheduling notification: " + ex.getMessage());
        }

        outputBoundary.presentScheduleResult(outputData[0]);
    }
}
