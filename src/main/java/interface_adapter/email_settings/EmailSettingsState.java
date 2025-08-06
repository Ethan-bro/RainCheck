package interface_adapter.email_settings;

/**
 * State for email settings view model.
 */
public class EmailSettingsState {
    private String email = "";
    private boolean notificationsEnabled = false;
    private String emailError = null;
    private String successMessage = null;

    public EmailSettingsState(EmailSettingsState copy) {
        this.email = copy.email;
        this.notificationsEnabled = copy.notificationsEnabled;
        this.emailError = copy.emailError;
        this.successMessage = copy.successMessage;
    }

    public EmailSettingsState() {}

    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public String getEmailError() { return emailError; }
    public void setEmailError(String emailError) { this.emailError = emailError; }

    public String getSuccessMessage() { return successMessage; }
    public void setSuccessMessage(String successMessage) { this.successMessage = successMessage; }
}