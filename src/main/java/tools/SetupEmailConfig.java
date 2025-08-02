package tools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import data_access.FileNotificationDataAccess;
import entity.EmailNotificationConfig;

import java.io.FileReader;

/**
 * Utility to set up email configuration for a user.
 * This is necessary before notifications can be scheduled.
 */
public class SetupEmailConfig {

    public static void main(String[] args) {
        try {
            System.out.println("Setting up email notification config...");

            // 1. Load email credentials
            JsonObject config = JsonParser.parseReader(new FileReader("config/secrets.json")).getAsJsonObject();
            String email = config.get("email_username").getAsString();

            // 2. Create notification data access
            FileNotificationDataAccess notificationDataAccess = new FileNotificationDataAccess(
                    "data/email_configs.json",
                    "data/scheduled_notifications.json"
            );

            // 3. Set up configuration for various usernames
            // Replace these with actual usernames in your system
            setupUserEmail(notificationDataAccess, "testuser", email);
            setupUserEmail(notificationDataAccess, "user", email);
            setupUserEmail(notificationDataAccess, "admin", email);
            setupUserEmail(notificationDataAccess, "default", email);
            setupUserEmail(notificationDataAccess, "kian", email);

            // For any username you provide at runtime
            if (args.length > 0) {
                String username = args[0];
                setupUserEmail(notificationDataAccess, username, email);
            }

            System.out.println("\nEmail configuration completed successfully!");
            System.out.println("You should now be able to schedule notifications.");

        } catch (Exception e) {
            System.err.println("Failed to set up email config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void setupUserEmail(FileNotificationDataAccess dataAccess, String username, String email) {
        try {
            // Check if config already exists
            EmailNotificationConfig existingConfig = dataAccess.getEmailConfig(username);

            if (existingConfig != null) {
                System.out.println(String.format("Config for '%s' already exists: %s (enabled: %s)",
                        username, existingConfig.getUserEmail(), 
                        existingConfig.isEmailNotificationsEnabled() ? "yes" : "no"));
            } else {
                // Create and save config with notifications enabled
                EmailNotificationConfig newConfig = new EmailNotificationConfig(email, true);
                dataAccess.saveEmailConfig(username, newConfig);
                System.out.println(String.format("Created email config for '%s': %s", username, email));
            }
        } catch (Exception e) {
            System.err.println("Error setting up email for user " + username + ": " + e.getMessage());
        }
    }
}
