package tools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import use_case.notification.NotificationDataAccessInterface;

import java.io.FileReader;

public class EnsureEmailConfigForUser {

    private EnsureEmailConfigForUser() {}

    public static void ensureEmailConfigForUser(NotificationDataAccessInterface notificationDataAccess, String username) {
        try {
            entity.EmailNotificationConfig emailConfig = notificationDataAccess.getEmailConfig(username);
            if (emailConfig == null) {
                JsonObject config = JsonParser.parseReader(new FileReader("config/secrets.json")).getAsJsonObject();
                if (config.has("email_username")) {
                    String email = config.get("email_username").getAsString();
                    entity.EmailNotificationConfig newConfig = new entity.EmailNotificationConfig(email, true);
                    notificationDataAccess.saveEmailConfig(username, newConfig);
                    System.out.println("Created email notification config for user: " + username);
                } else {
                    System.out.println("No email_username in secrets.json, cannot create email config");
                }
            } else {
                System.out.println("Email config already exists for user: " + username);
            }
        } catch (Exception e) {
            System.err.println("Error ensuring email config for user " + username + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

}
