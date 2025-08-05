
package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Entity representing email notification configuration for a user.
 */
public class EmailNotificationConfig {
    private String userEmail;
    private boolean emailNotificationsEnabled;

    public EmailNotificationConfig() {
    }

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailNotificationConfig that = (EmailNotificationConfig) o;
        return emailNotificationsEnabled == that.emailNotificationsEnabled &&
                userEmail.equals(that.userEmail);
    }

    @Override
    public int hashCode() {
        return userEmail.hashCode() + Boolean.hashCode(emailNotificationsEnabled);
    }
}