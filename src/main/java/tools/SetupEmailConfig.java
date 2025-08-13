package tools;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import data_access.FileNotificationDataAccess;
import entity.EmailNotificationConfig;

/**
 * Utility to set up email configuration for a user.
 * This is necessary before notifications can be scheduled.
 */
public class SetupEmailConfig {

    /**
     * Entry point to run the email config setup utility.
     *
     * @param args optional first argument is username to configure
     */
    public static void main(String[] args) {
        try {
            System.out.println("Setting up email notification config...");

            final JsonObject config = JsonParser.parseReader(new FileReader("config/secrets.json")).getAsJsonObject();
            final String email = config.get("email_username").getAsString();

            final FileNotificationDataAccess notificationDataAccess = new FileNotificationDataAccess(
                    "data/email_configs.json",
                    "data/scheduled_notifications.json"
            );

            // Set up config for fixed usernames
            setupUserEmail(notificationDataAccess, "testuser", email);
            setupUserEmail(notificationDataAccess, "user", email);
            setupUserEmail(notificationDataAccess, "admin", email);
            setupUserEmail(notificationDataAccess, "default", email);
            setupUserEmail(notificationDataAccess, "kian", email);

            // Set up config for username passed as argument (if any)
            if (args.length > 0) {
                final String username = args[0];
                setupUserEmail(notificationDataAccess, username, email);
            }

            System.out.println("\nEmail configuration completed successfully!");
            System.out.println("You should now be able to schedule notifications.");
        }
        catch (FileNotFoundException ex) {
            System.err.println("Config file not found: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Sets up the email notification configuration for a user.
     *
     * @param dataAccess the notification data access object
     * @param username the username to configure
     * @param email the user's email address
     */
    private static void setupUserEmail(
            FileNotificationDataAccess dataAccess, String username, String email) {
        final EmailNotificationConfig existingConfig = dataAccess.getEmailConfig(username);

        if (existingConfig != null) {
            final String enabledStr;
            if (existingConfig.isEmailNotificationsEnabled()) {
                enabledStr = "yes";
            }
            else {
                enabledStr = "no";
            }
            System.out.printf("Config for '%s' already exists: %s (enabled: %s)%n",
                    username, existingConfig.getUserEmail(), enabledStr);
        }
        else {
            final EmailNotificationConfig newConfig = new EmailNotificationConfig(email, true);
            dataAccess.saveEmailConfig(username, newConfig);
            System.out.printf("Created email config for '%s': %s%n", username, email);
        }
    }
}
