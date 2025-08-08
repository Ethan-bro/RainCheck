package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity representing email notification configuration for a user.
 */
public class EmailNotificationConfig {
    private String userEmail;
    private boolean emailNotificationsEnabled;

    @JsonCreator
    public EmailNotificationConfig(
            @JsonProperty("userEmail") String userEmail,
            @JsonProperty("emailNotificationsEnabled") boolean emailNotificationsEnabled) {
        this.userEmail = userEmail;
        this.emailNotificationsEnabled = emailNotificationsEnabled;
    }

    // Getters and setters
    @JsonProperty("userEmail")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @JsonProperty("emailNotificationsEnabled")
    public boolean isEmailNotificationsEnabled() {
        return emailNotificationsEnabled;
    }

    public void setEmailNotificationsEnabled(boolean emailNotificationsEnabled) {
        this.emailNotificationsEnabled = emailNotificationsEnabled;
    }

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

    @Override
    public int hashCode() {
        return userEmail.hashCode() + Boolean.hashCode(emailNotificationsEnabled);
    }
}
