package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.ViewManagerModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

/**
 * Polished Signup View matching the Login View style.
 */
public class SignupView extends JPanel implements ActionListener, PropertyChangeListener {
    private static final String viewName = "sign up";

    private final SignupViewModel signupViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SignupController signupController;

    private final JTextField emailInputField = new JTextField(20);
    private final JTextField usernameInputField = new JTextField(20);
    private final JPasswordField passwordInputField = new JPasswordField(20);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(20);

    private final JButton signUp;
    private final JButton cancel;
    private final JButton toLogin;

    public SignupView(SignupController controller, SignupViewModel signupViewModel, ViewManagerModel viewManagerModel) {
        this.signupController = controller;
        this.signupViewModel = signupViewModel;
        this.viewManagerModel = viewManagerModel;
        signupViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel(SignupViewModel.TITLE_LABEL);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel emailLabel = new JLabel(SignupViewModel.EMAIL_LABEL);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(emailInputField, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel usernameLabel = new JLabel(SignupViewModel.USERNAME_LABEL);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(usernameInputField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel passwordLabel = new JLabel(SignupViewModel.PASSWORD_LABEL);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(passwordInputField, gbc);

        // Repeat Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        JLabel repeatPasswordLabel = new JLabel(SignupViewModel.REPEAT_PASSWORD_LABEL);
        repeatPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(repeatPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(repeatPasswordInputField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Buttons Panel (Sign Up + Cancel)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        buttonsPanel.setOpaque(false);

        signUp = new JButton(SignupViewModel.SIGNUP_BUTTON_LABEL);
        signUp.setPreferredSize(new Dimension(120, 35));
        signUp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        signUp.setFocusPainted(false);
        signUp.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
        signUp.setForeground(new Color(34, 139, 34));
        signUp.setContentAreaFilled(false);
        signUp.setOpaque(false);

        cancel = new JButton(SignupViewModel.CANCEL_BUTTON_LABEL);
        cancel.setPreferredSize(new Dimension(120, 35));
        cancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancel.setFocusPainted(false);
        cancel.setBorder(BorderFactory.createLineBorder(new Color(178, 34, 34), 2));
        cancel.setForeground(new Color(178, 34, 34));
        cancel.setContentAreaFilled(false);
        cancel.setOpaque(false);

        buttonsPanel.add(signUp);
        buttonsPanel.add(cancel);

        // Login Panel ("Already a user? Log In")
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setOpaque(false);
        loginPanel.add(new JLabel("Already a user? "));
        toLogin = new JButton(SignupViewModel.TO_LOGIN_BUTTON_LABEL);
        toLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        toLogin.setBorderPainted(false);
        toLogin.setContentAreaFilled(false);
        toLogin.setForeground(new Color(30, 144, 255));  // blue link style
        toLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginPanel.add(toLogin);

        // Combine both panels into one
        JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));
        combinedPanel.setOpaque(false);
        combinedPanel.add(buttonsPanel);
        combinedPanel.add(loginPanel);

        // Add Gmail Setup Instructions button
        JButton gmailButton = GmailSetupInstructionsFactory.createButton(viewManagerModel, getViewName());
        gmailButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        combinedPanel.add(Box.createVerticalStrut(10)); // spacing between login and button
        combinedPanel.add(gmailButton);

        add(combinedPanel, BorderLayout.SOUTH);

        // Listeners
        signUp.addActionListener(evt -> {
            SignupState currentState = signupViewModel.getState();
            signupController.execute(
                    currentState.getUsername(),
                    currentState.getPassword(),
                    currentState.getRepeatPassword(),
                    currentState.getEmail()
            );
        });

        cancel.addActionListener(this);
        toLogin.addActionListener(evt -> signupController.switchToLoginView());

        addEmailListener();
        addUsernameListener();
        addPasswordListener();
        addRepeatPasswordListener();
    }

    private void addEmailListener() {
        emailInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                SignupState currentState = signupViewModel.getState();
                currentState.setEmail(emailInputField.getText());
                signupViewModel.setState(currentState);
            }

            @Override public void insertUpdate(DocumentEvent e) { documentListenerHelper(); }
            @Override public void removeUpdate(DocumentEvent e) { documentListenerHelper(); }
            @Override public void changedUpdate(DocumentEvent e) { documentListenerHelper(); }
        });
    }

    private void addUsernameListener() {
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                SignupState currentState = signupViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                signupViewModel.setState(currentState);
            }

            @Override public void insertUpdate(DocumentEvent e) { documentListenerHelper(); }
            @Override public void removeUpdate(DocumentEvent e) { documentListenerHelper(); }
            @Override public void changedUpdate(DocumentEvent e) { documentListenerHelper(); }
        });
    }

    private void addPasswordListener() {
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                SignupState currentState = signupViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                signupViewModel.setState(currentState);
            }

            @Override public void insertUpdate(DocumentEvent e) { documentListenerHelper(); }
            @Override public void removeUpdate(DocumentEvent e) { documentListenerHelper(); }
            @Override public void changedUpdate(DocumentEvent e) { documentListenerHelper(); }
        });
    }

    private void addRepeatPasswordListener() {
        repeatPasswordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                SignupState currentState = signupViewModel.getState();
                currentState.setRepeatPassword(new String(repeatPasswordInputField.getPassword()));
                signupViewModel.setState(currentState);
            }

            @Override public void insertUpdate(DocumentEvent e) { documentListenerHelper(); }
            @Override public void removeUpdate(DocumentEvent e) { documentListenerHelper(); }
            @Override public void changedUpdate(DocumentEvent e) { documentListenerHelper(); }
        });
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == cancel) {
            System.exit(0);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SignupState state = (SignupState) evt.getNewValue();
        if (state.getUsernameError() != null) {
            JOptionPane.showMessageDialog(this, state.getUsernameError());
        }
    }

    public static String getViewName() {
        return viewName;
    }
}
