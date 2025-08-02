package tools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import data_access.EmailNotificationService;
import data_access.FileNotificationDataAccess;
import entity.*;
import use_case.notification.ScheduleNotificationInputData;
import use_case.notification.ScheduleNotificationInteractor;
import use_case.notification.ScheduleNotificationOutputBoundary;
import use_case.notification.ScheduleNotificationOutputData;

import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Utility to test the full notification flow including email configuration.
 * Use this to verify that notifications are being properly scheduled and sent.
 */
public class EmailConfigTester {

    public static void main(String[] args) {
        try {
            System.out.println("Email Notification Flow Tester");
            System.out.println("============================\n");

            // Step 1: Get username from args or use default
            String username = args.length > 0 ? args[0] : "testuser";
            System.out.println("Testing for user: " + username);

            // Step 2: Load email configuration
            JsonObject config = JsonParser.parseReader(new FileReader("config/secrets.json")).getAsJsonObject();
            String email = config.get("email_username").getAsString();

            // Step 3: Set up data access objects
            FileNotificationDataAccess notificationDataAccess = new FileNotificationDataAccess(
                    "data/email_configs.json",
                    "data/scheduled_notifications.json"
            );

            // Step 4: Create email service
            EmailNotificationService emailService = new EmailNotificationService(
                    "smtp.gmail.com", // Gmail SMTP server
                    "587",  // TLS port for Gmail
                    config.get("email_username").getAsString(),
                    config.get("email_password").getAsString()
            );

            // Step 5: Check if user has email config set up
            EmailNotificationConfig userConfig = notificationDataAccess.getEmailConfig(username);

            if (userConfig == null) {
                System.out.println("Creating email configuration for user: " + username);
                userConfig = new EmailNotificationConfig(email, true); // Enable email notifications
                notificationDataAccess.saveEmailConfig(username, userConfig);
                System.out.println("✓ Email configuration created\n");
            } else {
                System.out.println("Found existing email config for user: " + username);
                System.out.println("Email: " + userConfig.getUserEmail());
                System.out.println("Notifications enabled: " + userConfig.isEmailNotificationsEnabled() + "\n");
            }

            // Step 6: Create a test task
            System.out.println("Creating test task...");
            LocalDateTime startTime = LocalDateTime.now().plusMinutes(2);
            Task task = createTestTask("Email Test Task", startTime);

            // Step 7: Send a test immediate email
            System.out.println("Sending immediate test email...");
            try {
                emailService.sendTaskReminder(null, task, userConfig.getUserEmail());
                System.out.println("✓ Immediate test email sent successfully to " + userConfig.getUserEmail());
            } catch (Exception e) {
                System.out.println("✗ Failed to send immediate email: " + e.getMessage());
                e.printStackTrace();
            }

            // Step 8: Schedule a notification (if we had a task DAO)
            System.out.println("\nCreating a scheduled notification...");
            Reminder reminder = new Reminder(1); // 1 minute before
            ScheduledNotification notification = emailService.scheduleEmailReminder(task, userConfig.getUserEmail(), reminder);

            System.out.println("Task ID: " + task.getTaskInfo().getId());
            System.out.println("Notification scheduled for: " + notification.getScheduledTime());
            System.out.println("Email recipient: " + notification.getUserEmail());

            // Step 9: Save the notification
            notificationDataAccess.saveScheduledNotification(notification);
            System.out.println("✓ Notification saved successfully");

            System.out.println("\nTest completed successfully!");
            System.out.println("You should receive the immediate test email shortly.");
            System.out.println("The scheduled notification will be sent at the scheduled time if the app is running.");

        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Task createTestTask(String name, LocalDateTime startTime) {
        LocalDateTime endTime = startTime.plusHours(1);

        TaskInfo taskInfo = new TaskInfo(
                TaskID.from(UUID.randomUUID()),
                name,
                startTime,
                endTime,
                Priority.HIGH,
                null, // No tag
                new Reminder(1), // 1 minute reminder
                "Clear skies",
                "clear-day",
                "25" // 25 degrees
        );

        return new Task(taskInfo);
    }
}
