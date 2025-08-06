package data_access;

import entity.Task;
import entity.User;
import entity.ScheduledNotification;
import use_case.notification.NotificationDataAccessInterface;
import use_case.notification.EmailNotificationServiceInterface;
import use_case.editTask.EditTaskDataAccessInterface;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Background scheduler that processes pending notifications.
 */
public class NotificationScheduler {
    private final NotificationDataAccessInterface notificationDataAccess;
    private final EmailNotificationServiceInterface emailService;
    private final EditTaskDataAccessInterface taskDataAccess;
    private final ScheduledExecutorService scheduler;

    public NotificationScheduler(NotificationDataAccessInterface notificationDataAccess,
                                 EmailNotificationServiceInterface emailService,
                                 EditTaskDataAccessInterface taskDataAccess) {
        this.notificationDataAccess = notificationDataAccess;
        this.emailService = emailService;
        this.taskDataAccess = taskDataAccess;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    /**
     * Start the notification scheduler - checks every minute for pending notifications
     */
    public void start() {
        scheduler.scheduleAtFixedRate(this::processPendingNotifications, 0, 1, TimeUnit.MINUTES);
        System.out.println("Notification scheduler started - checking every minute");
    }

    /**
     * Stop the notification scheduler
     */
    public void stop() {
        scheduler.shutdown();
        System.out.println("Notification scheduler stopped");
    }

    private void processPendingNotifications() {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<ScheduledNotification> pendingNotifications =
                    notificationDataAccess.getPendingNotifications(now);

            System.out.println("Found " + pendingNotifications.size() + " pending notifications");

            for (ScheduledNotification notification : pendingNotifications) {
                sendNotification(notification);
            }
        } catch (Exception e) {
            System.err.println("Error processing pending notifications: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendNotification(ScheduledNotification notification) {
        try {
            // Get the task details
            Task task = taskDataAccess.getTaskByIdAndEmail(
                    notification.getUserEmail(),
                    entity.TaskID.from(java.util.UUID.fromString(notification.getTaskId()))
            );

            if (task != null) {
                // Send the email
                emailService.sendTaskReminder(null, task, notification.getUserEmail());

                // Mark as sent
                notificationDataAccess.markNotificationAsSent(notification.getNotificationId());

                System.out.println("Sent notification for task: " + task.getTaskInfo().getTaskName());
            } else {
                System.err.println("Task not found for notification: " + notification.getTaskId());
            }
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }
}