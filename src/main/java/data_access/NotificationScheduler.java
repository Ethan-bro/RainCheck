package data_access;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import entity.ScheduledNotification;
import entity.Task;
import use_case.editTask.EditTaskDataAccessInterface;
import use_case.notification.EmailNotificationServiceInterface;
import use_case.notification.NotificationDataAccessInterface;

/**
 * Background scheduler that processes pending notifications.
 */
public class NotificationScheduler {
    private final NotificationDataAccessInterface notificationDataAccess;
    private final EmailNotificationServiceInterface emailService;
    private final EditTaskDataAccessInterface taskDataAccess;
    private final ScheduledExecutorService scheduler;

    /**
     * Constructs a NotificationScheduler with the required dependencies.
     * @param notificationDataAccess the notification data access interface
     * @param emailService the email notification service
     * @param taskDataAccess the task data access interface
     */
    public NotificationScheduler(NotificationDataAccessInterface notificationDataAccess,
                                 EmailNotificationServiceInterface emailService,
                                 EditTaskDataAccessInterface taskDataAccess) {
        this.notificationDataAccess = notificationDataAccess;
        this.emailService = emailService;
        this.taskDataAccess = taskDataAccess;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    /**
     * Starts the notification scheduler. Checks every minute for pending notifications.
     */
    public void start() {
        scheduler.scheduleAtFixedRate(this::processPendingNotifications, 0, 1, TimeUnit.MINUTES);
        System.out.println("Notification scheduler started - checking every minute.");
    }

    /**
     * Stops the notification scheduler.
     */
    public void stop() {
        scheduler.shutdown();
        System.out.println("Notification scheduler stopped.");
    }

    // Processes all pending notifications and sends them if needed.
    private void processPendingNotifications() {
        final LocalDateTime now = LocalDateTime.now();
        final List<ScheduledNotification> pendingNotifications =
                notificationDataAccess.getPendingNotifications(now);

        System.out.println("Found " + pendingNotifications.size() + " pending notifications.");

        for (ScheduledNotification notification : pendingNotifications) {
            sendNotification(notification);
        }
    }

    // Sends a single notification if the associated task exists.
    private void sendNotification(ScheduledNotification notification) {
        final Task task = taskDataAccess.getTaskByIdAndEmail(
                notification.getUserEmail(),
                entity.TaskID.from(java.util.UUID.fromString(notification.getTaskId()))
        );

        if (task != null) {
            emailService.sendTaskReminder(null, task, notification.getUserEmail());
            notificationDataAccess.markNotificationAsSent(notification.getNotificationId());
            System.out.println("Sent notification for task: " + task.getTaskInfo().getTaskName());
        }
        else {
            System.err.println("Task not found for notification: " + notification.getTaskId());
        }
    }
}
