package entity;

/**
 * Entity representing email notification configuration for a user.
 */
public class EmailNotificationConfig {
    private final String userEmail;
    private final boolean emailNotificationsEnabled;

    public EmailNotificationConfig(String userEmail, boolean emailNotificationsEnabled) {
        this.userEmail = userEmail;
        this.emailNotificationsEnabled = emailNotificationsEnabled;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean isEmailNotificationsEnabled() {
        return emailNotificationsEnabled;
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
