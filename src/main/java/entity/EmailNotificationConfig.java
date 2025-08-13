package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity representing email notification configuration for a user.
 */
public class EmailNotificationConfig {
    private String userEmail;
    private boolean emailNotificationsEnabled;

    /**
     * Constructs an EmailNotificationConfig with the given user email and notification flag.
     * @param userEmail the user's email
     * @param emailNotificationsEnabled true if notifications are enabled
     */
    @JsonCreator
    public EmailNotificationConfig(
            @JsonProperty("userEmail") String userEmail,
            @JsonProperty("emailNotificationsEnabled") boolean emailNotificationsEnabled) {
        this.userEmail = userEmail;
        this.emailNotificationsEnabled = emailNotificationsEnabled;
    }

    // Getters and setters
    /**
     * Gets the user email.
     * @return the user email
     */
    @JsonProperty("userEmail")
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Checks if email notifications are enabled.
     * @return true if enabled, false otherwise
     */
    @JsonProperty("emailNotificationsEnabled")
    public boolean isEmailNotificationsEnabled() {
        return emailNotificationsEnabled;
    }

    /**
     * Checks equality with another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (this == o) {
            result = true;
        }
        else if (o == null || getClass() != o.getClass()) {
            result = false;
        }
        else {
            final EmailNotificationConfig that = (EmailNotificationConfig) o;
            result = emailNotificationsEnabled == that.emailNotificationsEnabled && userEmail.equals(that.userEmail);
        }
        return result;
    }

    /**
     * Returns the hash code for this config.
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return userEmail.hashCode() + Boolean.hashCode(emailNotificationsEnabled);
    }
}
