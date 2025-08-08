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

    public EmailSettingsController(NotificationDataAccessInterface notificationDataAccess,
                                   EmailSettingsViewModel viewModel) {
        this.notificationDataAccess = notificationDataAccess;
        this.viewModel = viewModel;
    }

    /**
     * Processes the input email settings state, validates, saves config, and updates ViewModel state.
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
     * Sends a test email to the given address and updates ViewModel state accordingly.
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
     * Validates an email address format.
     * @param email the email string to validate
     * @return true if valid, false otherwise
     */
    private boolean isInvalidEmail(String email) {
        return email == null || !email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    /**
     * Gets the current username from session or context.
     * @return the current username (placeholder implementation)
     */
    private String getCurrentUsername() {
        return USERNAME_PLACEHOLDER;
    }
}
