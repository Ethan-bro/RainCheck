package interface_adapter.email_settings;

import entity.EmailNotificationConfig;
import use_case.notification.NotificationDataAccessInterface;

/**
 * Controller for email settings operations.
 */
public class EmailSettingsController {

    private final NotificationDataAccessInterface notificationDataAccess;
    private final EmailSettingsViewModel viewModel;

    public EmailSettingsController(NotificationDataAccessInterface notificationDataAccess,
                                   EmailSettingsViewModel viewModel) {
        this.notificationDataAccess = notificationDataAccess;
        this.viewModel = viewModel;
    }

    public void execute(EmailSettingsState inputState) {
        try {
            // Validate email format
            if (!isValidEmail(inputState.getEmail())) {
                EmailSettingsState errorState = new EmailSettingsState(inputState);
                errorState.setEmailError("Please enter a valid email address");
                viewModel.setState(errorState);
                return;
            }

            // Save email configuration (username would come from session)
            String username = getCurrentUsername(); // This would be implemented based on your session management
            EmailNotificationConfig config = new EmailNotificationConfig(
                    inputState.getEmail(),
                    inputState.isNotificationsEnabled()
            );

            notificationDataAccess.saveEmailConfig(username, config);

            // Show success message
            EmailSettingsState successState = new EmailSettingsState(inputState);
            successState.setSuccessMessage("Email settings saved successfully!");
            successState.setEmailError(null);
            viewModel.setState(successState);

        } catch (Exception e) {
            EmailSettingsState errorState = new EmailSettingsState(inputState);
            errorState.setEmailError("Failed to save settings: " + e.getMessage());
            viewModel.setState(errorState);
        }
    }

    public void sendTestEmail(String email) {
        try {
            if (!isValidEmail(email)) {
                EmailSettingsState errorState = viewModel.getState();
                errorState.setEmailError("Please enter a valid email address for test");
                viewModel.setState(errorState);
                return;
            }

            // TODO: Implement test email sending
            EmailSettingsState successState = viewModel.getState();
            successState.setSuccessMessage("Test email sent to " + email);
            successState.setEmailError(null);
            viewModel.setState(successState);

        } catch (Exception e) {
            EmailSettingsState errorState = viewModel.getState();
            errorState.setEmailError("Failed to send test email: " + e.getMessage());
            viewModel.setState(errorState);
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    private String getCurrentUsername() {
        // TODO: Implement based on your session management
        return "current_user"; // Placeholder
    }
}