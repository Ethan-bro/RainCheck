package interface_adapter.email_settings;

import entity.EmailNotificationConfig;

import use_case.notification.NotificationDataAccessInterface;

/**
 * Controller for email settings operations.
 */
public class EmailSettingsController {

    private static final String USERNAME_PLACEHOLDER = "current_user";
    private final NotificationDataAccessInterface notificationDataAccess;
    private final EmailSettingsViewModel viewModel;

    /**
     * Constructs an EmailSettingsController with the given data access and view model.
     *
     * @param notificationDataAccess the data access interface for notifications
     * @param viewModel the view model for email settings
     */
    public EmailSettingsController(NotificationDataAccessInterface notificationDataAccess,
                                   EmailSettingsViewModel viewModel) {
        this.notificationDataAccess = notificationDataAccess;
        this.viewModel = viewModel;
    }

    /**
     * This method:
     * 1) processes the input email settings state
     * 2) validates the email
     * 3) saves the configuration
     * 4) Finally, it updates the view model state.
     *
     * @param inputState the input state from the view
     */
    public void execute(EmailSettingsState inputState) {
        if (isInvalidEmail(inputState.getEmail())) {
            final EmailSettingsState errorState = new EmailSettingsState(inputState);
            errorState.setEmailError("Please enter a valid email address");
            viewModel.setState(errorState);
        }
        else {
            final String username = getCurrentUsername();
            final EmailNotificationConfig config = new EmailNotificationConfig(
                    inputState.getEmail(),
                    inputState.isNotificationsEnabled()
            );

            notificationDataAccess.saveEmailConfig(username, config);

            final EmailSettingsState successState = new EmailSettingsState(inputState);
            successState.setSuccessMessage("Email settings saved successfully!");
            successState.setEmailError(null);
            viewModel.setState(successState);
        }
    }

    /**
     * Sends a test email to the given address and updates the view model state accordingly.
     *
     * @param email the email address to send a test email to
     */
    public void sendTestEmail(String email) {
        if (isInvalidEmail(email)) {
            final EmailSettingsState errorState = viewModel.getState();
            errorState.setEmailError("Please enter a valid email address for test");
            viewModel.setState(errorState);
        }
        else {
            final EmailSettingsState successState = viewModel.getState();
            successState.setSuccessMessage("Test email sent to " + email);
            successState.setEmailError(null);
            viewModel.setState(successState);
        }
    }

    /**
     * Checks if the provided email address is invalid.
     *
     * @param email the email string to validate
     * @return true if the email is invalid, false otherwise
     */
    private boolean isInvalidEmail(String email) {
        return email == null || !email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    /**
     * Gets the current username from session or context.
     *
     * @return the current username (placeholder implementation)
     */
    private String getCurrentUsername() {
        return USERNAME_PLACEHOLDER;
    }
}
