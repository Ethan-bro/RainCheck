package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
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

    private static final int VERTICAL_SPACING_BETWEEN_LOGIN_AND_GMAIL = 10;

    private final SignupViewModel signupViewModel;
    private final SignupController signupController;
    private final ViewManagerModel viewManagerModel;

    private final JTextField emailInputField = new JTextField(FIELD_COLUMNS);
    private final JTextField usernameInputField = new JTextField(FIELD_COLUMNS);
    private final JPasswordField passwordInputField = new JPasswordField(FIELD_COLUMNS);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(FIELD_COLUMNS);

    private final JButton signUp;
    private final JButton cancel;
    private JButton toLogin;

    /**
     * Constructs the SignupView and initializes the UI components.
     *
     * @param controller the controller for signup actions
     * @param model the view model for signup
     * @param viewManagerModel the model managing view state transitions
     */
    public SignupView(
            final SignupController controller,
            final SignupViewModel model,
            final ViewManagerModel viewManagerModel
    ) {
        this.signupViewModel = model;
        signupViewModel.addPropertyChangeListener(this);

        this.signupController = controller;
        this.viewManagerModel = viewManagerModel;

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

        final JButton gmailButton = GmailSetupInstructionsFactory.createButton(viewManagerModel, getViewName());
        gmailButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        combinedPanel.add(Box.createVerticalStrut(VERTICAL_SPACING_BETWEEN_LOGIN_AND_GMAIL));
        combinedPanel.add(gmailButton);

        add(combinedPanel, BorderLayout.SOUTH);

        attachListeners();
    }

    /**
     * Creates the title label for the signup view.
     *
     * @return the JLabel for the title
     */
    private JLabel createTitleLabel() {
        final JLabel title = new JLabel(SignupViewModel.TITLE_LABEL);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, TITLE_FONT_SIZE));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        return title;
    }

    /**
     * Creates the form panel containing all input fields.
     *
     * @return the JPanel for the form
     */
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

    /**
     * Adds a label and field to the form panel at the specified row.
     *
     * @param panel the panel to add to
     * @param gbc the grid bag constraints
     * @param row the row index
     * @param labelText the label text
     * @param field the input field
     */
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

    /**
     * Creates a styled JButton with the given text and border color.
     *
     * @param text the button text
     * @param borderColor the border color
     * @return the styled JButton
     */
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

    /**
     * Creates the panel containing the signup and cancel buttons.
     *
     * @return the JPanel for the buttons
     */
    private JPanel createButtonsPanel() {
        final JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_HGAP, BUTTON_VGAP));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(signUp);
        buttonsPanel.add(cancel);
        return buttonsPanel;
    }

    /**
     * Creates the panel with the login link/button.
     *
     * @return the JPanel for the login link
     */
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

    /**
     * Attaches listeners to the buttons and input fields.
     */
    private void attachListeners() {
        signUp.addActionListener(this::onSignUpClicked);
        cancel.addActionListener(evt -> System.exit(0));
        toLogin.addActionListener(evt -> signupController.switchToLoginView());

        addDocumentListener(emailInputField, this::updateEmail);
        addDocumentListener(usernameInputField, this::updateUsername);
        addDocumentListener(passwordInputField, this::updatePassword);
        addDocumentListener(repeatPasswordInputField, this::updateRepeatPassword);
    }

    /**
     * Handles the signup button click event.
     *
     * @param evt the action event
     */
    private void onSignUpClicked(final ActionEvent evt) {
        final SignupState currentState = signupViewModel.getState();
        signupController.execute(
                currentState.getUsername(),
                currentState.getPassword(),
                currentState.getRepeatPassword(),
                currentState.getEmail()
        );
    }

    /**
     * Adds a document listener to a text field to update the view model.
     *
     * @param field the text field
     * @param consumer the consumer to handle text changes
     */
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

    /**
     * Gets the text from a text field, handling password fields appropriately.
     *
     * @param field the text field
     * @return the field text
     */
    private String getFieldText(final JTextField field) {
        String result = field.getText();
        if (field instanceof JPasswordField) {
            result = new String(((JPasswordField) field).getPassword());
        }
        return result;
    }

    /**
     * Updates the email in the signup view model.
     *
     * @param email the email to set
     */
    private void updateEmail(final String email) {
        final SignupState currentState = signupViewModel.getState();
        currentState.setEmail(email);
        signupViewModel.setState(currentState);
    }

    /**
     * Updates the username in the signup view model.
     *
     * @param username the username to set
     */
    private void updateUsername(final String username) {
        final SignupState currentState = signupViewModel.getState();
        currentState.setUsername(username);
        signupViewModel.setState(currentState);
    }

    /**
     * Updates the password in the signup view model.
     *
     * @param password the password to set
     */
    private void updatePassword(final String password) {
        final SignupState currentState = signupViewModel.getState();
        currentState.setPassword(password);
        signupViewModel.setState(currentState);
    }

    /**
     * Updates the repeat password in the signup view model.
     *
     * @param repeatPassword the repeat password to set
     */
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

    /**
     * Returns the unique view name identifier for this view.
     *
     * @return the view name string
     */
    public static String getViewName() {
        return VIEW_NAME;
    }

    /**
     * Functional interface used to abstract text-consuming operations in the signup form.
     *
     * <p>This interface allows us to pass lambda expressions or method references that accept a
     * single String argument, enabling flexible and reusable handling of text input fields
     * (such as username, password, etc.) without tightly coupling the logic to specific UI components.
     * This is especially useful for wiring up listeners and callbacks in a clean, concise way.
     *
     * <p>The {@code @FunctionalInterface} annotation enforces that this interface has exactly one abstract method,
     * making it compatible with lambda expressions and method references in Java 8 and above.
     * This is a best practice for functional-style programming and helps ensure code clarity and maintainability.
     *
     * <p>This pattern is used to keep the codebase modular and to adhere to the
     * Single Responsibility and Open/Closed principles from SOLID,
     * as it decouples input handling from UI logic.
     */
    @FunctionalInterface
    private interface TextConsumer {
        /**
         * Consumes a string of text, typically from a user input field.
         *
         * @param text the text to consume
         */
        void consume(String text);
    }
}
