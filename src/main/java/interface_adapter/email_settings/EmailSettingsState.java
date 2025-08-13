package interface_adapter.email_settings;

/**
 * State for email settings view model.
 */
public class EmailSettingsState {

    private String email;
    private boolean notificationsEnabled;
    private String emailError;
    private String successMessage;

    /**
     * Copy constructor for EmailSettingsState.
     *
     * @param copy the EmailSettingsState to copy from
     */
    public EmailSettingsState(EmailSettingsState copy) {
        this.email = copy.email;
        this.notificationsEnabled = copy.notificationsEnabled;
        this.emailError = copy.emailError;
        this.successMessage = copy.successMessage;
    }

    /**
     * Default constructor for EmailSettingsState.
     */
    public EmailSettingsState() {
    }

    // Getters and setters

    /**
     * Returns the email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns whether notifications are enabled.
     *
     * @return true if notifications are enabled, false otherwise
     */
    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    /**
     * Sets whether notifications are enabled.
     *
     * @param notificationsEnabled true to enable notifications, false otherwise
     */
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    /**
     * Returns the email error message, if any.
     *
     * @return the email error message
     */
    public String getEmailError() {
        return emailError;
    }

    /**
     * Sets the email error message.
     *
     * @param emailError the error message to set
     */
    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    /**
     * Returns the success message, if any.
     *
     * @return the success message
     */
    public String getSuccessMessage() {
        return successMessage;
    }

    /**
     * Sets the success message.
     *
     * @param successMessage the success message to set
     */
    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
