package view;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractTaskFormView extends JPanel {
    protected JTextField nameField;
    protected JSpinner startSpinner;
    protected JSpinner endSpinner;
    protected JComboBox<Priority> priorityCombo;
    protected JComboBox<Object> customTagCombo;
    protected JComboBox<Reminder> reminderCombo;

    protected JButton saveButton;
    protected JButton cancelButton;
    protected JLabel errorLabel;

    public AbstractTaskFormView(String title, String saveButtonText, List<CustomTag> tagOptions) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(45, 45, 45));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color labelColor = new Color(60, 60, 60);

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

        customTagCombo = new JComboBox<>(tagOptions.isEmpty() ? new Object[]{} : tagOptions.toArray());
        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 16);
        customTagCombo.setFont(emojiFont);

        if (tagOptions.isEmpty()) {
            customTagCombo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                              boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setFont(emojiFont);
                    if (value == null) {
                        label.setText("No tags available - create one first");
                        label.setForeground(Color.GRAY);
                    } else {
                        label.setText(value.toString());  // this uses your CustomTag.toString()
                    }
                    return label;
                }
            });
        }
        addLabeledField(formPanel, gbc, row++, "Category:", labelFont, labelColor, customTagCombo);

        Reminder[] reminderOptions = {
                Reminder.NONE,
                new Reminder(0),
                new Reminder(10),
                new Reminder(30),
                new Reminder(60),
                new Reminder(1440)
        };
        reminderCombo = new JComboBox<>(reminderOptions);
        reminderCombo.setFont(labelFont);
        addLabeledField(formPanel, gbc, row++, "Reminder (min before):", labelFont, labelColor, reminderCombo);

        add(formPanel, BorderLayout.CENTER);

        errorLabel = new JLabel();
        errorLabel.setForeground(new Color(220, 20, 60));
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        errorLabel.setVisible(false);

        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setBackground(Color.WHITE);
        errorPanel.add(errorLabel);

        saveButton = new JButton(saveButtonText);
        styleButton(saveButton, new Color(34, 139, 34));
        cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(178, 34, 34));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBackground(Color.WHITE);
        southPanel.add(errorPanel);
        southPanel.add(buttonsPanel);

        add(southPanel, BorderLayout.SOUTH);
    }

    private void addLabeledField(JPanel panel, GridBagConstraints gbc, int row,
                                 String labelText, Font font, Color color, JComponent field) {
        gbc.gridy = row;

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(color);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    protected void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
    }

    protected JSpinner makeDateTimeSpinner() {
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm"));
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return spinner;
    }
}
