// TODO: Idk If we need this class, **Kian** do we need it?

package view;

import interface_adapter.email_settings.EmailSettingsController;
import interface_adapter.email_settings.EmailSettingsState;
import interface_adapter.email_settings.EmailSettingsViewModel;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * View for email notification settings.
 */
public class EmailSettingsView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final String VIEW_NAME = "email settings";

    private static final int EMAIL_FIELD_COLUMNS = 20;
    private static final int TITLE_FONT_SIZE = 16;
    private static final int VERTICAL_STRUT_SMALL = 10;
    private static final int VERTICAL_STRUT_LARGE = 20;

    private final EmailSettingsViewModel emailSettingsViewModel;
    private final JTextField emailField = new JTextField(EMAIL_FIELD_COLUMNS);
    private final JCheckBox enableNotificationsCheckbox = new JCheckBox("Enable email notifications");
    private final JButton saveButton = new JButton("Save Settings");
    private final JButton testEmailButton = new JButton("Send Test Email");

    private EmailSettingsController emailSettingsController;

    public EmailSettingsView(EmailSettingsViewModel emailSettingsViewModel) {
        this.emailSettingsViewModel = emailSettingsViewModel;
        this.emailSettingsViewModel.addPropertyChangeListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("📧 Email Notification Settings");
        title.setFont(new Font("Arial", Font.BOLD, TITLE_FONT_SIZE));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel emailPanel = new JPanel();
        emailPanel.add(new JLabel("Email Address:"));
        emailPanel.add(emailField);

        final JPanel checkboxPanel = new JPanel();
        checkboxPanel.add(enableNotificationsCheckbox);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(testEmailButton);

        saveButton.addActionListener(this);
        testEmailButton.addActionListener(this);

        this.add(Box.createVerticalStrut(VERTICAL_STRUT_SMALL));
        this.add(title);
        this.add(Box.createVerticalStrut(VERTICAL_STRUT_LARGE));
        this.add(emailPanel);
        this.add(Box.createVerticalStrut(VERTICAL_STRUT_SMALL));
        this.add(checkboxPanel);
        this.add(Box.createVerticalStrut(VERTICAL_STRUT_LARGE));
        this.add(buttonPanel);
        this.add(Box.createVerticalStrut(VERTICAL_STRUT_SMALL));
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(saveButton)) {
            final EmailSettingsState currentState = emailSettingsViewModel.getState();
            currentState.setEmail(emailField.getText());
            currentState.setNotificationsEnabled(enableNotificationsCheckbox.isSelected());
            emailSettingsController.execute(currentState);
        }
        else if (e.getSource().equals(testEmailButton)) {
            emailSettingsController.sendTestEmail(emailField.getText());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final EmailSettingsState state = (EmailSettingsState) evt.getNewValue();
        if (state.getEmailError() != null) {
            JOptionPane.showMessageDialog(this, state.getEmailError(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (state.getSuccessMessage() != null) {
            JOptionPane.showMessageDialog(this, state.getSuccessMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void setEmailSettingsController(EmailSettingsController emailSettingsController) {
        this.emailSettingsController = emailSettingsController;
    }
}
