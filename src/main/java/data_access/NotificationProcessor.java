package data_access;

import entity.Task;
import entity.User;
import entity.ScheduledNotification;
import entity.TaskID;
import use_case.notification.NotificationDataAccessInterface;
import use_case.notification.EmailNotificationServiceInterface;
import use_case.editTask.EditTaskDataAccessInterface;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class NotificationProcessor {
    private final NotificationDataAccessInterface notificationDataAccess;
    private final EditTaskDataAccessInterface taskDataAccess;
    private final EmailNotificationService emailService;
    private final ScheduledExecutorService scheduler;

    public NotificationProcessor(NotificationDataAccessInterface notificationDataAccess,
                                 EditTaskDataAccessInterface taskDataAccess,
                                 EmailNotificationService emailService) {
        this.notificationDataAccess = notificationDataAccess;
        this.taskDataAccess = taskDataAccess;
        this.emailService = emailService;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startProcessing() {
        // Check for pending notifications every minute
        scheduler.scheduleAtFixedRate(this::processPendingNotifications, 0, 1, TimeUnit.MINUTES);
    }

    public void stopProcessing() {
        scheduler.shutdown();
    }

    private void processPendingNotifications() {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<ScheduledNotification> pendingNotifications =
                    notificationDataAccess.getPendingNotifications(now);

            for (ScheduledNotification notification : pendingNotifications) {
                try {
                    // Get the task details
                    Task task = taskDataAccess.getTaskById(
                            extractUsernameFromEmail(notification.getUserEmail()),
                            TaskID.from(UUID.fromString(notification.getTaskId()))
                    );

                    if (task != null) {
                        // Send the email
                        emailService.sendTaskReminder(null, task, notification.getUserEmail());

                        // Mark as sent
                        notificationDataAccess.markNotificationAsSent(notification.getNotificationId());

                        System.out.println("Email reminder sent for task: " + task.getTaskInfo().getTaskName());
                    }
                } catch (Exception e) {
                    System.err.println("Failed to send notification: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing notifications: " + e.getMessage());
        }
    }

    private String extractUsernameFromEmail(String email) {
        // You'll need to implement this based on how you store username-email mapping
        // For now, return a placeholder or implement proper user lookup
        return "defaultUser";
    }
}
