package view;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

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

import java.util.Date;
import java.util.List;

/**
 * Abstract base class for task form views with common UI elements and styles.
 */
public abstract class AbstractTaskFormView extends JPanel {

    // UI constants
    private static final String FONT_NAME = "Segoe UI";
    private static final String FONT_NAME_EMOJI = "Segoe UI Emoji";
    private static final int TITLE_FONT_SIZE = 28;
    private static final int LABEL_FONT_SIZE = 16;
    private static final int ERROR_FONT_SIZE = 14;
    private static final int BORDER_TOP = 30;
    private static final int BORDER_LEFT = 40;
    private static final int FORM_TOP_BORDER = 20;
    private static final int INSET_VALUE = 15;
    private static final int TITLE_COLOR_VALUE = 45;
    private static final int LABEL_COLOR_VALUE = 60;
    private static final int ERROR_COLOR_R = 220;
    private static final int ERROR_COLOR_G = 20;
    private static final int ERROR_COLOR_B = 60;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;
    private static final int BUTTON_GAP = 30;
    private static final int ERROR_LABEL_TOP_PADDING = 15;

    private static final float LABEL_WEIGHT_X_SMALL = 0.3f;
    private static final float LABEL_WEIGHT_X_LARGE = 0.7f;

    private JTextField nameField;
    private JSpinner startSpinner;
    private JSpinner endSpinner;
    private JComboBox<Priority> priorityCombo;
    private JComboBox<Object> customTagCombo;
    private JComboBox<Reminder> reminderCombo;

    private JButton saveButton;
    private JButton cancelButton;
    private JLabel errorLabel;

    /**
     * Constructs the abstract task form view with a title, save button text, and available tags.
     *
     * @param title          the form title
     * @param saveButtonText the text for the save button
     * @param tagOptions     the list of available custom tags
     */
    public AbstractTaskFormView(String title, String saveButtonText, List<CustomTag> tagOptions) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(BORDER_TOP, BORDER_LEFT, BORDER_TOP, BORDER_LEFT));

        add(initTitleLabel(title), BorderLayout.NORTH);
        add(initFormPanel(tagOptions), BorderLayout.CENTER);
        add(initSouthPanel(saveButtonText), BorderLayout.SOUTH);
    }

    /**
     * Creates and returns the title label.
     *
     * @param title the title text
     * @return configured JLabel for the title
     */
    private JLabel initTitleLabel(String title) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(new Color(TITLE_COLOR_VALUE, TITLE_COLOR_VALUE, TITLE_COLOR_VALUE));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return titleLabel;
    }

    /**
     * Builds and returns the form panel containing all task input fields.
     *
     * @param tagOptions list of available custom tags
     * @return configured JPanel with form fields
     */
    private JPanel initFormPanel(List<CustomTag> tagOptions) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(FORM_TOP_BORDER, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_VALUE, INSET_VALUE, INSET_VALUE, INSET_VALUE);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font(FONT_NAME, Font.PLAIN, LABEL_FONT_SIZE);
        Color labelColor = new Color(LABEL_COLOR_VALUE, LABEL_COLOR_VALUE, LABEL_COLOR_VALUE);

        int row = 0;
        nameField = new JTextField(30);
        addLabeledField(formPanel, gbc, row++, "Task Name:", labelFont, labelColor, nameField);

        startSpinner = makeDateTimeSpinner();
        addLabeledField(formPanel, gbc, row++, "Start Date/Time:", labelFont, labelColor, startSpinner);

        endSpinner = makeDateTimeSpinner();
        addLabeledField(formPanel, gbc, row++, "End Date/Time:", labelFont, labelColor, endSpinner);

        priorityCombo = new JComboBox<>(Priority.values());
        priorityCombo.setFont(labelFont);
        addLabeledField(formPanel, gbc, row++, "Priority:", labelFont, labelColor, priorityCombo);

        customTagCombo = initCustomTagCombo(tagOptions);
        addLabeledField(formPanel, gbc, row++, "Category:", labelFont, labelColor, customTagCombo);

        reminderCombo = initReminderCombo(labelFont);
        addLabeledField(formPanel, gbc, row++, "Reminder (min before):", labelFont, labelColor, reminderCombo);

        return formPanel;
    }

    /**
     * Creates the custom tag selection combo box.
     *
     * @param tagOptions available tags
     * @return JComboBox for tag selection
     */
    private JComboBox<Object> initCustomTagCombo(List<CustomTag> tagOptions) {
        JComboBox<Object> comboBox;
        if (tagOptions.isEmpty()) {
            comboBox = new JComboBox<>();
            comboBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        javax.swing.JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(
                            list, value, index, isSelected, cellHasFocus);
                    label.setFont(new Font(FONT_NAME_EMOJI, Font.PLAIN, LABEL_FONT_SIZE));
                    if (value == null) {
                        label.setText("No tags available - create one first");
                        label.setForeground(Color.GRAY);
                    } else {
                        label.setText(value.toString());
                    }
                    return label;
                }
            });
        } else {
            comboBox = new JComboBox<>(tagOptions.toArray());
            comboBox.setFont(new Font(FONT_NAME_EMOJI, Font.PLAIN, LABEL_FONT_SIZE));
        }
        return comboBox;
    }

    /**
     * Creates the reminder selection combo box.
     *
     * @param labelFont font for the combo box
     * @return JComboBox for reminder options
     */
    private JComboBox<Reminder> initReminderCombo(Font labelFont) {
        Reminder[] reminderOptions = {
                Reminder.NONE,
                new Reminder(0),
                new Reminder(10),
                new Reminder(30),
                new Reminder(60),
                new Reminder(1440),
        };
        JComboBox<Reminder> comboBox = new JComboBox<>(reminderOptions);
        comboBox.setFont(labelFont);
        return comboBox;
    }

    /**
     * Builds and returns the south panel containing error label and buttons.
     *
     * @param saveButtonText text for the save button
     * @return JPanel containing the bottom section
     */
    private JPanel initSouthPanel(String saveButtonText) {
        errorLabel = new JLabel();
        errorLabel.setForeground(new Color(ERROR_COLOR_R, ERROR_COLOR_G, ERROR_COLOR_B));
        errorLabel.setFont(new Font(FONT_NAME, Font.BOLD, ERROR_FONT_SIZE));
        errorLabel.setVisible(false);

        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setBackground(Color.WHITE);
        errorPanel.add(errorLabel);

        saveButton = new JButton(saveButtonText);
        styleButton(saveButton, new Color(34, 139, 34));
        cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(178, 34, 34));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_GAP, ERROR_LABEL_TOP_PADDING));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBackground(Color.WHITE);
        southPanel.add(errorPanel);
        southPanel.add(buttonsPanel);

        return southPanel;
    }

    /**
     * Adds a labeled field to the provided panel.
     *
     * @param panel     the container panel to add components to
     * @param gbc       the GridBagConstraints object for layout control
     * @param row       the row position in the grid
     * @param labelText the text for the label
     * @param font      the font used for the label
     * @param color     the color used for the label text
     * @param field     the input component to add next to the label
     */
    private void addLabeledField(JPanel panel, GridBagConstraints gbc, int row,
                                 String labelText, Font font, Color color, javax.swing.JComponent field) {
        gbc.gridy = row;

        gbc.gridx = 0;
        gbc.weightx = LABEL_WEIGHT_X_SMALL;
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(color);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = LABEL_WEIGHT_X_LARGE;
        panel.add(field, gbc);
    }

    /**
     * Styles the given button with font, background color, and padding.
     * <p>
     * This method is protected and can be overridden safely by subclasses to customize button styling.
     *
     * @param button  the JButton to style
     * @param bgColor the background color to apply
     */
    protected void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font(FONT_NAME, Font.BOLD, LABEL_FONT_SIZE));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
    }

    /**
     * Creates a date-time spinner with the default format and font.
     * <p>
     * This method is protected and can be overridden safely by subclasses to customize spinner creation.
     *
     * @return configured JSpinner for date/time input
     */
    protected JSpinner makeDateTimeSpinner() {
        SpinnerDateModel model =
                new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm"));
        spinner.setFont(new Font(FONT_NAME, Font.PLAIN, LABEL_FONT_SIZE));
        return spinner;
    }

    /**
     * Gets the text field used for entering the task name.
     *
     * @return the task name text field
     */
    public final JTextField getNameField() {
        return nameField;
    }

    /**
     * Gets the spinner for selecting the task start date and time.
     *
     * @return the start date/time spinner
     */
    public final JSpinner getStartSpinner() {
        return startSpinner;
    }

    /**
     * Gets the spinner for selecting the task end date and time.
     *
     * @return the end date/time spinner
     */
    public final JSpinner getEndSpinner() {
        return endSpinner;
    }

    /**
     * Gets the combo box for selecting the task priority.
     *
     * @return the priority selection combo box
     */
    public final JComboBox<Priority> getPriorityCombo() {
        return priorityCombo;
    }

    /**
     * Gets the combo box for selecting a custom tag for the task.
     *
     * @return the custom tag selection combo box
     */
    public final JComboBox<Object> getCustomTagCombo() {
        return customTagCombo;
    }

    /**
     * Gets the combo box for selecting a reminder time before the task.
     *
     * @return the reminder selection combo box
     */
    public final JComboBox<Reminder> getReminderCombo() {
        return reminderCombo;
    }

    /**
     * Gets the button used to save the task.
     *
     * @return the save button
     */
    public final JButton getSaveButton() {
        return saveButton;
    }

    /**
     * Gets the button used to cancel the form.
     *
     * @return the cancel button
     */
    public final JButton getCancelButton() {
        return cancelButton;
    }

    /**
     * Gets the label used to display error messages.
     *
     * @return the error message label
     */
    public final JLabel getErrorLabel() {
        return errorLabel;
    }
}
