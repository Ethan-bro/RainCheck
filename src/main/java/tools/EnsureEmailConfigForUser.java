package tools;

import use_case.notification.NotificationDataAccessInterface;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Utility to ensure an email notification config exists for a given user.
 */
public final class EnsureEmailConfigForUser {

    private EnsureEmailConfigForUser() {
        // Prevent instantiation
    }

    /**
     * Ensures email notification config exists for the given username.
     * Creates a new config using the email from secrets.json if none exists.
     *
     * @param notificationDataAccess the notification data access interface
     * @param username the username to ensure config for
     */
    public static void ensureEmailConfigForUser(
            NotificationDataAccessInterface notificationDataAccess,
            String username) {
        try {
            final entity.EmailNotificationConfig emailConfig = notificationDataAccess.getEmailConfig(username);
            if (emailConfig == null) {
                final JsonObject config = JsonParser.parseReader(new FileReader("config/secrets.json"))
                        .getAsJsonObject();

                if (config.has("email_username")) {
                    final String email = config.get("email_username").getAsString();

                    final entity.EmailNotificationConfig newConfig =
                            new entity.EmailNotificationConfig(email, true);

                    notificationDataAccess.saveEmailConfig(username, newConfig);

                    System.out.println("Created email notification config for user: " + username);
                }
                else {
                    System.out.println("No email_username in secrets.json, cannot create email config");
                }
            }
            else {
                System.out.println("Email config already exists for user: " + username);
            }
        }
        catch (IOException ex) {
            System.err.println("Error ensuring email config for user " + username + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
