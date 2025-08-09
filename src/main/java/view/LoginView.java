package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Polished Login View matching the Signup View style.
 */
public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final String FONT_NAME = "Segoe UI";
    private static final int BORDER_TOP = 30;
    private static final int BORDER_LEFT = 40;
    private static final int BORDER_BOTTOM = 30;
    private static final int BORDER_RIGHT = 40;

    private static final int TITLE_FONT_SIZE = 26;
    private static final int LABEL_FONT_SIZE = 16;
    private static final int ERROR_FONT_SIZE = 12;
    private static final int BUTTON_FONT_SIZE = 14;

    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 35;

    private static final int GRIDBAG_INSET_TOP = 12;
    private static final int GRIDBAG_INSET_LEFT = 12;
    private static final int GRIDBAG_INSET_BOTTOM = 8;
    private static final int GRIDBAG_INSET_RIGHT = 12;

    private static final double LABEL_WEIGHT_X = 0.3;
    private static final double FIELD_WEIGHT_X = 0.7;

    private static final int BUTTON_HGAP = 25;
    private static final int BUTTON_VGAP = 10;

    private static final Color LOGIN_GREEN = new Color(34, 139, 34);
    private static final Color CANCEL_RED = new Color(178, 34, 34);
    private static final Color SIGNUP_BLUE = new Color(30, 144, 255);

    private static final int VERTICAL_SPACING_BETWEEN_SIGNUP_AND_GMAIL = 10;

    private final String viewName = "log in";

    private final LoginController loginController;
    private final ViewManagerModel viewManagerModel;

    private final JTextField usernameInputField = new JTextField(20);
    private final JPasswordField passwordInputField = new JPasswordField(20);

    private final JLabel usernameErrorLabel = new JLabel();

    private JButton logIn;
    private JButton cancel;
    private JButton signUp;

    public LoginView(
            final LoginViewModel loginViewModel,
            final LoginController controller,
            final ViewManagerModel viewManagerModel
    ) {
        loginViewModel.addPropertyChangeListener(this);

        this.loginController = controller;
        this.viewManagerModel = viewManagerModel;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(BORDER_TOP, BORDER_LEFT, BORDER_BOTTOM, BORDER_RIGHT));
        setBackground(Color.WHITE);

        final JLabel title = buildTitle();
        add(title, BorderLayout.NORTH);

        final JPanel formPanel = buildFormPanel();
        add(formPanel, BorderLayout.CENTER);

        final JPanel combinedPanel = buildButtonsAndSignupPanel();
        add(combinedPanel, BorderLayout.SOUTH);

        // Listeners
        logIn.addActionListener(evt -> {
            final Object src = evt.getSource();
            if (src.equals(logIn)) {
                final LoginState currentState = loginViewModel.getState();
                loginController.execute(currentState.getUsername(), currentState.getPassword(), currentState.getEmail()
                );
            }
        });

        cancel.addActionListener(this);

        signUp.addActionListener(event -> loginController.switchToSignupView());

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

    private JLabel buildTitle() {
        final JLabel title = new JLabel("Login");
        title.setFont(new Font(FONT_NAME, Font.BOLD, TITLE_FONT_SIZE));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        return title;
    }

    private JPanel buildFormPanel() {
        final JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(GRIDBAG_INSET_TOP, GRIDBAG_INSET_LEFT, GRIDBAG_INSET_BOTTOM, GRIDBAG_INSET_RIGHT);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = LABEL_WEIGHT_X;
        final JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font(FONT_NAME, Font.PLAIN, LABEL_FONT_SIZE));
        formPanel.add(usernameLabel, gbc);

        // Username Input
        gbc.gridx = 1;
        gbc.weightx = FIELD_WEIGHT_X;
        formPanel.add(usernameInputField, gbc);

        // Username Error Label
        gbc.gridx = 1;
        gbc.gridy = 1;
        usernameErrorLabel.setForeground(Color.RED);
        usernameErrorLabel.setFont(new Font(FONT_NAME, Font.PLAIN, ERROR_FONT_SIZE));
        formPanel.add(usernameErrorLabel, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = LABEL_WEIGHT_X;
        final JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font(FONT_NAME, Font.PLAIN, LABEL_FONT_SIZE));
        formPanel.add(passwordLabel, gbc);

        // Password Input
        gbc.gridx = 1;
        gbc.weightx = FIELD_WEIGHT_X;
        formPanel.add(passwordInputField, gbc);

        return formPanel;
    }

    private JPanel buildButtonsAndSignupPanel() {
        final JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_HGAP, BUTTON_VGAP));
        buttonsPanel.setOpaque(false);

        logIn = new JButton("Log In");
        styleActionButton(logIn, LOGIN_GREEN);

        cancel = new JButton("Cancel");
        styleActionButton(cancel, CANCEL_RED);

        buttonsPanel.add(logIn);
        buttonsPanel.add(cancel);

        final JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signupPanel.setOpaque(false);
        signupPanel.add(new JLabel("Don't have an account? "));
        signUp = new JButton("Sign Up");
        signUp.setFont(new Font(FONT_NAME, Font.PLAIN, BUTTON_FONT_SIZE));
        signUp.setBorderPainted(false);
        signUp.setContentAreaFilled(false);
        signUp.setForeground(SIGNUP_BLUE);
        signUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupPanel.add(signUp);

        final JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));
        combinedPanel.setOpaque(false);
        combinedPanel.add(buttonsPanel);
        combinedPanel.add(signupPanel);

        final JButton gmailButton = GmailSetupInstructionsFactory.createButton(viewManagerModel, getViewName());
        gmailButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        combinedPanel.add(Box.createVerticalStrut(VERTICAL_SPACING_BETWEEN_SIGNUP_AND_GMAIL));
        combinedPanel.add(gmailButton);

        add(combinedPanel, BorderLayout.SOUTH);

        return combinedPanel;
    }

    private void styleActionButton(final JButton button, final Color color) {
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setFont(new Font(FONT_NAME, Font.BOLD, BUTTON_FONT_SIZE));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(color, 2));
        button.setForeground(color);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
        if (evt.getSource() == cancel) {
            System.exit(0);
        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final LoginState state = (LoginState) evt.getNewValue();

            usernameInputField.setText(state.getUsername());
            System.out.println("User wants to log out. username: " + state.getUsername());
            passwordInputField.setText(state.getPassword());
            usernameErrorLabel.setText(state.getLoginError());
        }
    }

    public String getViewName() {
        return viewName;
    }
}
