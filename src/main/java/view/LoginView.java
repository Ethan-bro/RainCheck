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
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

/**
 * Polished Login View matching the Signup View style.
 */
public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "log in";

    private final LoginController loginController;
    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    private final JTextField usernameInputField = new JTextField(20);
    private final JPasswordField passwordInputField = new JPasswordField(20);

    private final JLabel usernameErrorLabel = new JLabel();

    private final JButton logIn;
    private final JButton cancel;
    private final JButton signUp;

    public LoginView(LoginController controller, LoginViewModel loginViewModel, ViewManagerModel viewManagerModel) {
        this.loginController = controller;
        this.loginViewModel = loginViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(usernameLabel, gbc);

        // Username Input
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(usernameInputField, gbc);

        // Username Error
        gbc.gridx = 1;
        gbc.gridy = 1;
        usernameErrorLabel.setForeground(Color.RED);
        usernameErrorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(usernameErrorLabel, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        formPanel.add(passwordLabel, gbc);

        // Password Input
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(passwordInputField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Buttons panel (Log In + Cancel)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        buttonsPanel.setOpaque(false);

        logIn = new JButton("Log In");
        logIn.setPreferredSize(new Dimension(120, 35));
        logIn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logIn.setFocusPainted(false);
        logIn.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
        logIn.setForeground(new Color(34, 139, 34));
        logIn.setContentAreaFilled(false);
        logIn.setOpaque(false);

        cancel = new JButton("Cancel");
        cancel.setPreferredSize(new Dimension(120, 35));
        cancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancel.setFocusPainted(false);
        cancel.setBorder(BorderFactory.createLineBorder(new Color(178, 34, 34), 2));
        cancel.setForeground(new Color(178, 34, 34));
        cancel.setContentAreaFilled(false);
        cancel.setOpaque(false);

        buttonsPanel.add(logIn);
        buttonsPanel.add(cancel);

        // Signup panel ("Don't have an account?" + Sign Up)
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signupPanel.setOpaque(false);
        signupPanel.add(new JLabel("Don't have an account? "));
        signUp = new JButton("Sign Up");
        signUp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        signUp.setBorderPainted(false);
        signUp.setContentAreaFilled(false);
        signUp.setForeground(new Color(30, 144, 255));  // blue link style
        signUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupPanel.add(signUp);

        // Combine buttonsPanel and signupPanel vertically
        JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));
        combinedPanel.setOpaque(false);
        combinedPanel.add(buttonsPanel);
        combinedPanel.add(signupPanel);

        // Add Gmail Setup Instructions button below signupPanel
        JButton gmailButton = GmailSetupInstructionsFactory.createButton(viewManagerModel, getViewName());
        gmailButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        combinedPanel.add(Box.createVerticalStrut(10)); // spacing between signupPanel and button
        combinedPanel.add(gmailButton);

        add(combinedPanel, BorderLayout.SOUTH);

        // Listeners
        logIn.addActionListener(evt -> {
            if (evt.getSource().equals(logIn)) {
                LoginState currentState = loginViewModel.getState();
                loginController.execute(currentState.getUsername(), currentState.getPassword(), currentState.getEmail());
            }
        });

        cancel.addActionListener(this);

        signUp.addActionListener(e -> loginController.switchToSignupView());

        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                loginViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
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
        if ("state".equals(evt.getPropertyName())) {
            LoginState state = (LoginState) evt.getNewValue();
            usernameInputField.setText(state.getUsername());
            passwordInputField.setText(state.getPassword() != null ? state.getPassword() : "");
            usernameErrorLabel.setText(state.getLoginError() != null ? state.getLoginError() : "");
        }
    }

    public String getViewName() {
        return viewName;
    }

}
