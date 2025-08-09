package view;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;

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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Abstract base class for task form views, providing reusable UI components
 * such as labeled fields, styled buttons, and layout configurations.
 * This class serves as a template for task creation and editing views.
 */
public abstract class AbstractTaskFormView extends JPanel {

    // ---- Constants ----
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

    private static final int TEXTFIELD_COLUMNS = 30;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;
    private static final int BUTTON_GAP = 30;
    private static final int ERROR_LABEL_TOP_PADDING = 15;

    private static final int BUTTON_BORDER_TOP_BOTTOM = 5;
    private static final int BUTTON_BORDER_LEFT_RIGHT = 15;

    private static final float LABEL_WEIGHT_X_SMALL = 0.3f;
    private static final float LABEL_WEIGHT_X_LARGE = 0.7f;

    private static final Color SAVE_BUTTON_COLOR = new Color(34, 139, 34);
    private static final Color CANCEL_BUTTON_COLOR = new Color(178, 34, 34);

    private static final Reminder R_0 = new Reminder(0);
    private static final Reminder R_10 = new Reminder(10);
    private static final Reminder R_30 = new Reminder(30);
    private static final Reminder R_60 = new Reminder(60);
    private static final Reminder R_1440 = new Reminder(1440);

    // ---- Instance fields ----
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
     * Constructs the base task form view with a given title, save button text, and tag options.
     *
     * @param title the title of the form
     * @param saveButtonText the label for the save button
     * @param tagOptions the list of custom tags to display
     */
    public AbstractTaskFormView(final String title, final String saveButtonText,
                                final List<CustomTag> tagOptions) {
        setLayout(new java.awt.BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(BORDER_TOP, BORDER_LEFT, BORDER_TOP, BORDER_LEFT));

        add(initTitleLabel(title), java.awt.BorderLayout.NORTH);
        add(initFormPanel(tagOptions), java.awt.BorderLayout.CENTER);
        add(initSouthPanel(saveButtonText), java.awt.BorderLayout.SOUTH);
    }

    /**
     * Creates and styles the title label.
     *
     * @param title the text for the title
     * @return the JLabel for the title
     */
    private JLabel initTitleLabel(final String title) {
        final JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(
                new Color(TITLE_COLOR_VALUE, TITLE_COLOR_VALUE, TITLE_COLOR_VALUE)
        );
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return titleLabel;
    }

    /**
     * Initializes the form panel with all input fields.
     *
     * @param tagOptions the available custom tags
     * @return the populated JPanel
     */
    private JPanel initFormPanel(final List<CustomTag> tagOptions) {
        final JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(FORM_TOP_BORDER, 0, 0, 0));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSET_VALUE, INSET_VALUE, INSET_VALUE, INSET_VALUE);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        final Font labelFont = new Font(FONT_NAME, Font.PLAIN, LABEL_FONT_SIZE);
        final Color labelColor = new Color(LABEL_COLOR_VALUE, LABEL_COLOR_VALUE, LABEL_COLOR_VALUE);

        int row = 0;
        nameField = new JTextField(TEXTFIELD_COLUMNS);
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
        addLabeledField(formPanel, gbc, row++, "Reminder (min before):",
                labelFont, labelColor, reminderCombo);

        return formPanel;
    }

    /**
     * Creates a combo box for custom tags.
     *
     * @param tagOptions available tags
     * @return the JComboBox for custom tags
     */
    private JComboBox<Object> initCustomTagCombo(final List<CustomTag> tagOptions) {
        final JComboBox<Object> comboBox;
        if (tagOptions.isEmpty()) {
            comboBox = new JComboBox<>();
            comboBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        final JList<?> list,
                        final Object value,
                        final int index,
                        final boolean isSelected,
                        final boolean cellHasFocus) {
                    final JLabel label = (JLabel) super.getListCellRendererComponent(
                            list, value, index, isSelected, cellHasFocus
                    );
                    label.setFont(new Font(FONT_NAME_EMOJI, Font.PLAIN, LABEL_FONT_SIZE));
                    if (value == null) {
                        label.setText("No tags available - create one first");
                        label.setForeground(Color.GRAY);
                    }
                    else {
                        label.setText(value.toString());
                    }
                    return label;
                }
            });
        }
        else {
            comboBox = new JComboBox<>(tagOptions.toArray());
            comboBox.setFont(new Font(FONT_NAME_EMOJI, Font.PLAIN, LABEL_FONT_SIZE));
        }
        return comboBox;
    }

    /**
     * Creates a combo box for reminder options.
     *
     * @param labelFont the font to use for combo box items
     * @return the JComboBox for reminders
     */
    private JComboBox<Reminder> initReminderCombo(final Font labelFont) {
        final Reminder[] reminderOptions = {Reminder.NONE, R_0, R_10, R_30, R_60, R_1440};
        final JComboBox<Reminder> comboBox = new JComboBox<>(reminderOptions);
        comboBox.setFont(labelFont);
        return comboBox;
    }

    /**
     * Initializes the panel containing the save/cancel buttons and error label.
     *
     * @param saveButtonText the label for the save button
     * @return the JPanel for the south section
     */
    private JPanel initSouthPanel(final String saveButtonText) {
        errorLabel = new JLabel();
        errorLabel.setForeground(new Color(ERROR_COLOR_R, ERROR_COLOR_G, ERROR_COLOR_B));
        errorLabel.setFont(new Font(FONT_NAME, Font.BOLD, ERROR_FONT_SIZE));
        errorLabel.setVisible(false);

        final JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setBackground(Color.WHITE);
        errorPanel.add(errorLabel);

        saveButton = new JButton(saveButtonText);
        styleButton(saveButton, SAVE_BUTTON_COLOR);
        cancelButton = new JButton("Cancel");
        styleButton(cancelButton, CANCEL_BUTTON_COLOR);

        final JPanel buttonsPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, BUTTON_GAP, ERROR_LABEL_TOP_PADDING));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        final JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBackground(Color.WHITE);
        southPanel.add(errorPanel);
        southPanel.add(buttonsPanel);

        return southPanel;
    }

    /**
     * Adds a labeled field to the form panel.
     *
     * @param panel the panel to add the field to
     * @param gbc the grid bag constraints
     * @param row the row index
     * @param labelText the text for the label
     * @param font the font for the label
     * @param color the color of the label text
     * @param field the input field to add
     */
    private void addLabeledField(final JPanel panel, final GridBagConstraints gbc, final int row,
                                 final String labelText, final Font font, final Color color,
                                 final JComponent field) {
        gbc.gridy = row;

        gbc.gridx = 0;
        gbc.weightx = LABEL_WEIGHT_X_SMALL;
        final JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(color);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = LABEL_WEIGHT_X_LARGE;
        panel.add(field, gbc);
    }

    /**
     * Styles a given button with specified background color and UI settings.
     *
     * @param button the button to style
     * @param bgColor the background color for the button
     */
    protected void styleButton(final JButton button, final Color bgColor) {
        button.setFont(new Font(FONT_NAME, Font.BOLD, LABEL_FONT_SIZE));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(
                BUTTON_BORDER_TOP_BOTTOM, BUTTON_BORDER_LEFT_RIGHT,
                BUTTON_BORDER_TOP_BOTTOM, BUTTON_BORDER_LEFT_RIGHT));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
    }

    /**
     * Creates a date/time spinner with "yyyy-MM-dd HH:mm" format.
     *
     * @return the configured JSpinner
     */
    protected JSpinner makeDateTimeSpinner() {
        final SpinnerDateModel model =
                new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
        final JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm"));
        spinner.setFont(new Font(FONT_NAME, Font.PLAIN, LABEL_FONT_SIZE));
        return spinner;
    }

    /**
     * Gets the task name text field.
     *
     * @return the task name text field
     */
    public final JTextField getNameField() {
        return nameField;
    }

    /**
     * Sets the task name text field.
     *
     * @param nameField the task name text field to set
     */
    public final void setNameField(final JTextField nameField) {
        this.nameField = nameField;
    }

    /**
     * Gets the start date/time spinner.
     *
     * @return the start date/time spinner
     */
    public final JSpinner getStartSpinner() {
        return startSpinner;
    }

    /**
     * Sets the start date/time spinner.
     *
     * @param startSpinner the start date/time spinner to set
     */
    public final void setStartSpinner(final JSpinner startSpinner) {
        this.startSpinner = startSpinner;
    }

    /**
     * Gets the end date/time spinner.
     *
     * @return the end date/time spinner
     */
    public final JSpinner getEndSpinner() {
        return endSpinner;
    }

    /**
     * Sets the end date/time spinner.
     *
     * @param endSpinner the end date/time spinner to set
     */
    public final void setEndSpinner(final JSpinner endSpinner) {
        this.endSpinner = endSpinner;
    }

    /**
     * Gets the priority combo box.
     *
     * @return the priority combo box
     */
    public final JComboBox<Priority> getPriorityCombo() {
        return priorityCombo;
    }

    /**
     * Sets the priority combo box.
     *
     * @param priorityCombo the priority combo box to set
     */
    public final void setPriorityCombo(final JComboBox<Priority> priorityCombo) {
        this.priorityCombo = priorityCombo;
    }

    /**
     * Gets the custom tag combo box.
     *
     * @return the custom tag combo box
     */
    public final JComboBox<Object> getCustomTagCombo() {
        return customTagCombo;
    }

    /**
     * Sets the custom tag combo box.
     *
     * @param customTagCombo the custom tag combo box to set
     */
    public final void setCustomTagCombo(final JComboBox<Object> customTagCombo) {
        this.customTagCombo = customTagCombo;
    }

    /**
     * Gets the reminder combo box.
     *
     * @return the reminder combo box
     */
    public final JComboBox<Reminder> getReminderCombo() {
        return reminderCombo;
    }

    /**
     * Sets the reminder combo box.
     *
     * @param reminderCombo the reminder combo box to set
     */
    public final void setReminderCombo(final JComboBox<Reminder> reminderCombo) {
        this.reminderCombo = reminderCombo;
    }

    /**
     * Gets the save button.
     *
     * @return the save button
     */
    public final JButton getSaveButton() {
        return saveButton;
    }

    /**
     * Sets the save button.
     *
     * @param saveButton the save button to set
     */
    public final void setSaveButton(final JButton saveButton) {
        this.saveButton = saveButton;
    }

    /**
     * Gets the cancel button.
     *
     * @return the cancel button
     */
    public final JButton getCancelButton() {
        return cancelButton;
    }

    /**
     * Sets the cancel button.
     *
     * @param cancelButton the cancel button to set
     */
    public final void setCancelButton(final JButton cancelButton) {
        this.cancelButton = cancelButton;
    }

    /**
     * Gets the error label.
     *
     * @return the error label
     */
    public final JLabel getErrorLabel() {
        return errorLabel;
    }

    /**
     * Sets the error label.
     *
     * @param errorLabel the error label to set
     */
    public final void setErrorLabel(final JLabel errorLabel) {
        this.errorLabel = errorLabel;
    }
}
