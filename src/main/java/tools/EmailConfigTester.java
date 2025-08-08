package tools;

import data_access.EmailNotificationService;
import data_access.FileNotificationDataAccess;

import entity.EmailNotificationConfig;
import entity.Priority;
import entity.Reminder;
import entity.ScheduledNotification;
import entity.Task;
import entity.TaskID;
import entity.TaskInfo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Utility to test the full notification flow including email configuration.
 * Use this to verify that notifications are being properly scheduled and sent.
 */
public class EmailConfigTester {

    private static final String CONFIG_PATH = "config/secrets.json";
    private static final String EMAIL_CONFIGS_PATH = "data/email_configs.json";
    private static final String NOTIFICATIONS_PATH = "data/scheduled_notifications.json";
    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME_KEY = "email_username";
    private static final String EMAIL_PASSWORD_KEY = "email_password";

    /**
     * Entry point to run the email notification flow test.
     * @param args optional first arg is username to test
     */
    public static void main(String[] args) {
        try {
            System.out.println("Email Notification Flow Tester");
            System.out.println("============================\n");

            final String username;
            if (args.length > 0) {
                username = args[0];
            }
            else {
                username = "testuser";
            }
            System.out.println("Testing for user: " + username);

            final JsonObject config = JsonParser.parseReader(new FileReader(CONFIG_PATH)).getAsJsonObject();

            final String email = config.get(EMAIL_USERNAME_KEY).getAsString();
            final String password = config.get(EMAIL_PASSWORD_KEY).getAsString();

            final FileNotificationDataAccess notificationDataAccess = new FileNotificationDataAccess(
                    EMAIL_CONFIGS_PATH, NOTIFICATIONS_PATH
            );

            final EmailNotificationService emailService = new EmailNotificationService(
                    SMTP_SERVER, SMTP_PORT, email, password
            );

            final EmailNotificationConfig userConfig = getOrCreateUserEmailConfig(
                    username, email, notificationDataAccess
            );

            final Task task = createAndPrintTestTask();

            sendImmediateEmail(emailService, task, userConfig.getUserEmail());

            scheduleAndSaveNotification(emailService, task, userConfig.getUserEmail(),
                    new Reminder(1), notificationDataAccess);

            System.out.println("\nTest completed successfully!");
            System.out.println("You should receive the immediate test email shortly.");
            System.out.println("The scheduled notification will be sent at the scheduled time if the app is running.");
        }
        catch (FileNotFoundException ex) {
            System.err.println("Config file not found: " + ex.getMessage());
            ex.printStackTrace();
        }
        catch (IOException ex) {
            System.err.println("IO error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static EmailNotificationConfig getOrCreateUserEmailConfig(
            String username, String email, FileNotificationDataAccess notificationDataAccess
    ) {
        EmailNotificationConfig userConfig = notificationDataAccess.getEmailConfig(username);
        if (userConfig == null) {
            System.out.println("Creating email configuration for user: " + username);
            userConfig = new EmailNotificationConfig(email, true);
            notificationDataAccess.saveEmailConfig(username, userConfig);
            System.out.println("✓ Email configuration created\n");
        }
        else {
            System.out.println("Found existing email config for user: " + username);
            System.out.println("Email: " + userConfig.getUserEmail());
            System.out.println("Notifications enabled: " + userConfig.isEmailNotificationsEnabled() + "\n");
        }
        return userConfig;
    }

    private static Task createAndPrintTestTask() {
        System.out.println("Creating test task...");
        final LocalDateTime startTime = LocalDateTime.now().plusMinutes(2);
        return createTestTask(startTime);
    }

    private static void sendImmediateEmail(EmailNotificationService emailService, Task task, String recipientEmail) {
        System.out.println("Sending immediate test email...");
        emailService.sendTaskReminder(null, task, recipientEmail);
        System.out.println("✓ Immediate test email sent successfully to " + recipientEmail);
    }

    private static void scheduleAndSaveNotification(
            EmailNotificationService emailService,
            Task task,
            String recipientEmail,
            Reminder reminder,
            FileNotificationDataAccess notificationDataAccess
    ) {
        System.out.println("\nCreating a scheduled notification...");
        final ScheduledNotification notification = emailService.scheduleEmailReminder(task, recipientEmail, reminder);
        System.out.println("Task ID: " + task.getTaskInfo().getId());
        System.out.println("Notification scheduled for: " + notification.getScheduledTime());
        System.out.println("Email recipient: " + notification.getUserEmail());
        notificationDataAccess.saveScheduledNotification(notification);
        System.out.println("✓ Notification saved successfully");
    }

    /**
     * Creates a test Task with the given name and start time.
     *
     * @param startTime the task start time
     * @return a new Task instance
     */
    private static Task createTestTask(LocalDateTime startTime) {
        final LocalDateTime endTime = startTime.plusHours(1);

        final TaskInfo taskInfo = new TaskInfo();

        // Set core details
        taskInfo.setCoreDetails(
                TaskID.from(UUID.randomUUID()),
                "Email Test Task",
                startTime,
                endTime
        );

        // Set additional details
        taskInfo.setAdditionalDetails(
                Priority.HIGH,
                null,
                new Reminder(1),
                "No"
        );

        // Set weather info
        taskInfo.setWeatherInfo(
                "Clear skies",
                "clear-day",
                "25",
                ""
        );

        return new Task(taskInfo);
    }
}
