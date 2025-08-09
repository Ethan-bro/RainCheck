package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Polished Signup View matching the Login View style.
 */
public class SignupView extends JPanel implements PropertyChangeListener {
    private static final String VIEW_NAME = "SIGN_UP";

    private static final String FONT_FAMILY = "Segoe UI";

    private static final int TITLE_FONT_SIZE = 26;
    private static final int LABEL_FONT_SIZE = 16;
    private static final int BUTTON_FONT_SIZE = 14;

    private static final int FIELD_COLUMNS = 20;
    private static final int REPEAT_PASSWORD_ROW = 3;

    private static final int BORDER_TOP = 30;
    private static final int BORDER_LEFT = 40;
    private static final int BORDER_BOTTOM = 30;
    private static final int BORDER_RIGHT = 40;

    private static final int GRIDBAG_INSET = 12;

    private static final float GRIDBAG_WEIGHT_LABEL = 0.3f;
    private static final float GRIDBAG_WEIGHT_FIELD = 0.7f;

    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 35;
    private static final int BUTTON_HGAP = 25;
    private static final int BUTTON_VGAP = 10;

    private static final Color COLOR_SIGNUP_BORDER = new Color(34, 139, 34);
    private static final Color COLOR_CANCEL_BORDER = new Color(178, 34, 34);
    private static final Color COLOR_LINK = new Color(30, 144, 255);

    private final SignupViewModel signupViewModel;
    private final SignupController signupController;

    private final JTextField emailInputField = new JTextField(FIELD_COLUMNS);
    private final JTextField usernameInputField = new JTextField(FIELD_COLUMNS);
    private final JPasswordField passwordInputField = new JPasswordField(FIELD_COLUMNS);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(FIELD_COLUMNS);

    private final JButton signUp;
    private final JButton cancel;
    private JButton toLogin;

    public SignupView(final SignupController controller, final SignupViewModel model) {
        this.signupController = controller;
        this.signupViewModel = model;
        signupViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(BORDER_TOP, BORDER_LEFT, BORDER_BOTTOM, BORDER_RIGHT));
        setBackground(Color.WHITE);

        final JLabel title = createTitleLabel();
        add(title, BorderLayout.NORTH);

        final JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);

        signUp = createStyledButton(SignupViewModel.SIGNUP_BUTTON_LABEL, COLOR_SIGNUP_BORDER);
        cancel = createStyledButton(SignupViewModel.CANCEL_BUTTON_LABEL, COLOR_CANCEL_BORDER);

        final JPanel buttonsPanel = createButtonsPanel();
        final JPanel loginPanel = createLoginPanel();

        final JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));
        combinedPanel.setOpaque(false);
        combinedPanel.add(buttonsPanel);
        combinedPanel.add(loginPanel);

        add(combinedPanel, BorderLayout.SOUTH);

        attachListeners();
    }

    private JLabel createTitleLabel() {
        final JLabel title = new JLabel(SignupViewModel.TITLE_LABEL);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, TITLE_FONT_SIZE));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        return title;
    }

    private JPanel createFormPanel() {
        final JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(GRIDBAG_INSET, GRIDBAG_INSET, GRIDBAG_INSET, GRIDBAG_INSET);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addLabelAndField(formPanel, gbc, 0, SignupViewModel.EMAIL_LABEL, emailInputField);
        addLabelAndField(formPanel, gbc, 1, SignupViewModel.USERNAME_LABEL, usernameInputField);
        addLabelAndField(formPanel, gbc, 2, SignupViewModel.PASSWORD_LABEL, passwordInputField);
        addLabelAndField(
                formPanel,
                gbc,
                REPEAT_PASSWORD_ROW,
                SignupViewModel.REPEAT_PASSWORD_LABEL,
                repeatPasswordInputField
        );

        return formPanel;
    }

    private void addLabelAndField(final JPanel panel, final GridBagConstraints gbc, final int row,
                                  final String labelText, final JTextField field) {
        final JLabel label = new JLabel(labelText);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, LABEL_FONT_SIZE));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = GRIDBAG_WEIGHT_LABEL;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = GRIDBAG_WEIGHT_FIELD;
        panel.add(field, gbc);
    }

    private JButton createStyledButton(final String text, final Color borderColor) {
        final JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setFont(new Font(FONT_FAMILY, Font.BOLD, BUTTON_FONT_SIZE));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        button.setForeground(borderColor);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        return button;
    }

    private JPanel createButtonsPanel() {
        final JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_HGAP, BUTTON_VGAP));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(signUp);
        buttonsPanel.add(cancel);
        return buttonsPanel;
    }

    private JPanel createLoginPanel() {
        final JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setOpaque(false);
        loginPanel.add(new JLabel("Already a user? "));

        toLogin = new JButton(SignupViewModel.TO_LOGIN_BUTTON_LABEL);
        toLogin.setFont(new Font(FONT_FAMILY, Font.PLAIN, BUTTON_FONT_SIZE));
        toLogin.setBorderPainted(false);
        toLogin.setContentAreaFilled(false);
        toLogin.setForeground(COLOR_LINK);
        toLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginPanel.add(toLogin);

        return loginPanel;
    }

    private void attachListeners() {
        signUp.addActionListener(this::onSignUpClicked);
        cancel.addActionListener(evt -> System.exit(0));
        toLogin.addActionListener(evt -> signupController.switchToLoginView());

        addDocumentListener(emailInputField, this::updateEmail);
        addDocumentListener(usernameInputField, this::updateUsername);
        addDocumentListener(passwordInputField, this::updatePassword);
        addDocumentListener(repeatPasswordInputField, this::updateRepeatPassword);
    }

    private void onSignUpClicked(final ActionEvent evt) {
        final SignupState currentState = signupViewModel.getState();
        signupController.execute(
                currentState.getUsername(),
                currentState.getPassword(),
                currentState.getRepeatPassword(),
                currentState.getEmail()
        );
    }

    private void addDocumentListener(final JTextField field, final TextConsumer consumer) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                consumer.consume(getFieldText(field));
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                update();
            }
        });
    }

    private String getFieldText(final JTextField field) {
        String result = field.getText();
        if (field instanceof JPasswordField) {
            result = new String(((JPasswordField) field).getPassword());
        }
        return result;
    }

    private void updateEmail(final String email) {
        final SignupState currentState = signupViewModel.getState();
        currentState.setEmail(email);
        signupViewModel.setState(currentState);
    }

    private void updateUsername(final String username) {
        final SignupState currentState = signupViewModel.getState();
        currentState.setUsername(username);
        signupViewModel.setState(currentState);
    }

    private void updatePassword(final String password) {
        final SignupState currentState = signupViewModel.getState();
        currentState.setPassword(password);
        signupViewModel.setState(currentState);
    }

    private void updateRepeatPassword(final String repeatPassword) {
        final SignupState currentState = signupViewModel.getState();
        currentState.setRepeatPassword(repeatPassword);
        signupViewModel.setState(currentState);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final SignupState state = (SignupState) evt.getNewValue();
        if (state.getUsernameError() != null) {
            JOptionPane.showMessageDialog(this, state.getUsernameError());
        }
    }

    public static String getViewName() {
        return VIEW_NAME;
    }

    @FunctionalInterface
    private interface TextConsumer {
        void consume(String text);
    }
}
