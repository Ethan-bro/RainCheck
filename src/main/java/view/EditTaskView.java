package view;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;
import entity.Task;
import entity.TaskInfo;
import interface_adapter.ViewManagerModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskViewModel;
import interface_adapter.editTask.EditTaskState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class EditTaskView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final String viewName = "Edit Task";

    private final EditTaskController controller;
    private final EditTaskViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final String mainViewKey;

    private Task existingTask;

    // Components
    private final JTextField nameField;
    private final JSpinner startSpinner;
    private final JSpinner endSpinner;
    private final JComboBox<Priority> priorityCombo;
    private final JComboBox<Object> customTagCombo;
    private final JComboBox<Reminder> reminderCombo;

    private final JButton saveButton;
    private final JButton cancelButton;
    private final JLabel errorLabel;

    public EditTaskView(EditTaskController controller,
                        EditTaskViewModel viewModel,
                        ViewManagerModel viewManagerModel,
                        String mainViewKey) {

        this.controller = controller;
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.mainViewKey = mainViewKey;

        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // ===== Title =====
        JLabel title = new JLabel(viewName);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(45, 45, 45));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // ===== Form Panel =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color labelColor = new Color(60, 60, 60);

        // Task Name
        nameField = new JTextField(30);
        addLabeledField(formPanel, gbc, 0, "Task Name:", labelFont, labelColor, nameField);

        // Start Date/Time
        startSpinner = makeDateTimeSpinner(LocalDateTime.now());
        addLabeledField(formPanel, gbc, 1, "Start Date/Time:", labelFont, labelColor, startSpinner);

        // End Date/Time
        endSpinner = makeDateTimeSpinner(LocalDateTime.now().plusHours(1));
        addLabeledField(formPanel, gbc, 2, "End Date/Time:", labelFont, labelColor, endSpinner);

        // Priority
        priorityCombo = new JComboBox<>(Priority.values());
        priorityCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addLabeledField(formPanel, gbc, 3, "Priority:", labelFont, labelColor, priorityCombo);

        // Dynamically load user's tags from ViewModel
        List<Object> options = viewModel.getTagOptions();
        customTagCombo = new JComboBox<>(options.isEmpty() ? new Object[]{} : options.toArray());

        if (options.isEmpty()) {
            customTagCombo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                                                              int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value == null) {
                        setText("No tags available - create one first");
                        setForeground(Color.GRAY);
                    }
                    return this;
                }
            });
        }

        addLabeledField(formPanel, gbc, 4, "Category:", labelFont, labelColor, customTagCombo);

        // Reminder
        Reminder[] reminderOptions = {
                Reminder.NONE,
                new Reminder(0),
                new Reminder(10),
                new Reminder(30),
                new Reminder(60),
                new Reminder(1440)
        };
        reminderCombo = new JComboBox<>(reminderOptions);
        reminderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addLabeledField(formPanel, gbc, 5, "Reminder (min before):", labelFont, labelColor, reminderCombo);

        add(formPanel, BorderLayout.CENTER);

        // ===== Error Label =====
        errorLabel = new JLabel();
        errorLabel.setForeground(new Color(220, 20, 60));
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        errorLabel.setVisible(false);

        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setBackground(Color.WHITE);
        errorPanel.add(errorLabel);
        add(errorPanel, BorderLayout.SOUTH);

        // ===== Buttons =====
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonsPanel.setBackground(Color.WHITE);

        saveButton = new JButton("Save Changes");
        styleButton(saveButton, new Color(34, 139, 34));
        cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(178, 34, 34));

        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        add(buttonsPanel, BorderLayout.SOUTH);
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

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    private JSpinner makeDateTimeSpinner(LocalDateTime time) {
        Date date = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
        SpinnerDateModel model = new SpinnerDateModel(date, null, null, java.util.Calendar.MINUTE);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm"));
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return spinner;
    }

    private LocalDateTime toLocalDateTime(Date d) {
        return LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
    }

    private Task buildUpdatedTask() {
        String name = nameField.getText().trim();
        LocalDateTime start = toLocalDateTime((Date) startSpinner.getValue());
        LocalDateTime end = toLocalDateTime((Date) endSpinner.getValue());
        Priority priority = (Priority) priorityCombo.getSelectedItem();
        CustomTag tag = (CustomTag) customTagCombo.getSelectedItem();
        Reminder reminder = (Reminder) reminderCombo.getSelectedItem();

        TaskInfo oldInfo = existingTask.getTaskInfo();

        TaskInfo updatedInfo = new TaskInfo(
                oldInfo.getId(),
                name,
                start,
                end,
                priority,
                tag,
                reminder,
                oldInfo.getIsDeleted(),
                oldInfo.getWeatherDescription(),
                oldInfo.getWeatherIconName(),
                oldInfo.getTemperature()
        );
        updatedInfo.setTaskStatus(oldInfo.getTaskStatus());
        return new Task(updatedInfo);
    }

    public void setExistingTask(Task task) {
        this.existingTask = task;
        if (task == null) return;

        TaskInfo info = task.getTaskInfo();
        nameField.setText(info.getTaskName());
        startSpinner.setValue(Date.from(info.getStartDateTime().atZone(ZoneId.systemDefault()).toInstant()));
        endSpinner.setValue(Date.from(info.getEndDateTime().atZone(ZoneId.systemDefault()).toInstant()));
        priorityCombo.setSelectedItem(info.getPriority());
        if (info.getTag() != null) customTagCombo.setSelectedItem(info.getTag());
        if (info.getReminder() != null) reminderCombo.setSelectedItem(info.getReminder());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            controller.editTask(buildUpdatedTask());
        } else if (e.getSource() == cancelButton) {
            viewManagerModel.setState(mainViewKey);
            viewManagerModel.firePropertyChanged();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();

        if ("refreshTagOptions".equals(prop)) {
            Object newValue = evt.getNewValue();
            if (newValue instanceof List<?> updatedTags) {
                customTagCombo.setModel(new DefaultComboBoxModel<>(updatedTags.toArray()));
                customTagCombo.setEnabled(!updatedTags.isEmpty());
            }
            return;
        }

        EditTaskState state = viewModel.getState();
        if (state.isSuccess()) {
            viewManagerModel.setState(mainViewKey);
            viewManagerModel.firePropertyChanged();
        } else if (state.getError() != null) {
            errorLabel.setText(state.getError());
            errorLabel.setVisible(true);
        }
    }

    public static String getViewName() {
        return viewName;
    }
}
