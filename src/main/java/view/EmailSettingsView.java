package view;

import interface_adapter.email_settings.EmailSettingsController;
import interface_adapter.email_settings.EmailSettingsState;
import interface_adapter.email_settings.EmailSettingsViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for email notification settings.
 */
public class EmailSettingsView extends JPanel implements ActionListener, PropertyChangeListener {

    public final String viewName = "email settings";

    private final EmailSettingsViewModel emailSettingsViewModel;
    private final JTextField emailField = new JTextField(20);
    private final JCheckBox enableNotificationsCheckbox = new JCheckBox("Enable email notifications");
    private final JButton saveButton = new JButton("Save Settings");
    private final JButton testEmailButton = new JButton("Send Test Email");

    private EmailSettingsController emailSettingsController;

    public EmailSettingsView(EmailSettingsViewModel emailSettingsViewModel) {
        this.emailSettingsViewModel = emailSettingsViewModel;
        this.emailSettingsViewModel.addPropertyChangeListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("📧 Email Notification Settings");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel emailPanel = new JPanel();
        emailPanel.add(new JLabel("Email Address:"));
        emailPanel.add(emailField);

        JPanel checkboxPanel = new JPanel();
        checkboxPanel.add(enableNotificationsCheckbox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(testEmailButton);

        saveButton.addActionListener(this);
        testEmailButton.addActionListener(this);

        this.add(Box.createVerticalStrut(10));
        this.add(title);
        this.add(Box.createVerticalStrut(20));
        this.add(emailPanel);
        this.add(Box.createVerticalStrut(10));
        this.add(checkboxPanel);
        this.add(Box.createVerticalStrut(20));
        this.add(buttonPanel);
        this.add(Box.createVerticalStrut(10));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(saveButton)) {
            EmailSettingsState currentState = emailSettingsViewModel.getState();
            currentState.setEmail(emailField.getText());
            currentState.setNotificationsEnabled(enableNotificationsCheckbox.isSelected());
            emailSettingsController.execute(currentState);
        } else if (e.getSource().equals(testEmailButton)) {
            emailSettingsController.sendTestEmail(emailField.getText());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        EmailSettingsState state = (EmailSettingsState) evt.getNewValue();
        if (state.getEmailError() != null) {
            JOptionPane.showMessageDialog(this, state.getEmailError(), "Error", JOptionPane.ERROR_MESSAGE);
        } else if (state.getSuccessMessage() != null) {
            JOptionPane.showMessageDialog(this, state.getSuccessMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void setEmailSettingsController(EmailSettingsController emailSettingsController) {
        this.emailSettingsController = emailSettingsController;
    }
}