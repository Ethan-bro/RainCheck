package view;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;
import entity.Task;
import entity.TaskInfo;
import interface_adapter.ViewManagerModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskViewModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class EditTaskView extends JPanel {

    private final Task existingTask;
    private final EditTaskController controller;
    private final EditTaskViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final String mainViewKey;
    private static final String viewName = "Edit Task";

    // Form fields
    private final JTextField nameField;
    private final JSpinner startSpinner;
    private final JSpinner endSpinner;
    private final JComboBox<Priority> priorityCombo;
    private final JComboBox<CustomTag> customTagCombo;
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
        this.existingTask = controller.getCurrentTask();

        nameField = new JTextField(existingTask.getTaskInfo().getTaskName(), 30);
        startSpinner = makeDateTimeSpinner(existingTask.getTaskInfo().getStartDateTime());
        endSpinner = makeDateTimeSpinner(existingTask.getTaskInfo().getEndDateTime());

        priorityCombo = new JComboBox<>(Priority.values());
        priorityCombo.setSelectedItem(existingTask.getTaskInfo().getPriority());

        // TODO: Populate customTagCombo with user's tags loaded from database or some defaults.
        // Example:
        // CustomTag[] defaultTags = {
        //     new CustomTag("Work", "💼"),
        //     new CustomTag("Study", "📚"),
        //     new CustomTag("Chill", "😎")
        // };
        // customTagCombo = new JComboBox<>(defaultTags);
        customTagCombo = new JComboBox<>();

        // TODO: Populate reminderCombo with typical reminder intervals or user preferences.
        // Example:
        // Reminder[] defaultReminders = {
        //     new Reminder(5),    // 5 minutes before
        //     new Reminder(10),   // 10 minutes before
        //     new Reminder(30)    // 30 minutes before
        // };
        // reminderCombo = new JComboBox<>(defaultReminders);
        reminderCombo = new JComboBox<>();

        saveButton = new JButton("Save Changes");
        saveButton.addActionListener(evt -> {
            Task updatedTask = buildUpdatedTask();
            controller.editTask(updatedTask);
                });
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(evt -> {
                    viewManagerModel.setState(mainViewKey);
                });
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);

        // Layout
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Edit Task"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        add(new JLabel("Task Name:"), gbc); gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Start Date/Time:"), gbc); gbc.gridx = 1;
        add(startSpinner, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("End Date/Time:"), gbc); gbc.gridx = 1;
        add(endSpinner, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Priority:"), gbc); gbc.gridx = 1;
        add(priorityCombo, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Category:"), gbc); gbc.gridx = 1;
        add(customTagCombo, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Reminder (min before):"), gbc); gbc.gridx = 1;
        add(reminderCombo, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        add(errorLabel, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 1;
        add(saveButton, gbc); gbc.gridx = 1;
        add(cancelButton, gbc);
    }

    private JSpinner makeDateTimeSpinner(LocalDateTime dateTime) {
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        SpinnerDateModel model = new SpinnerDateModel(date, null, null, java.util.Calendar.MINUTE);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm"));
        return spinner;
    }

    private LocalDateTime toLocalDateTime(Date d) {
        return LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
    }

    private Task buildUpdatedTask() {
        String name;
        name = nameField.getText().trim();
        LocalDateTime start = toLocalDateTime((Date) startSpinner.getValue());
        LocalDateTime end = toLocalDateTime((Date) endSpinner.getValue());
        Priority priority = (Priority) priorityCombo.getSelectedItem();
        CustomTag tag = (CustomTag) customTagCombo.getSelectedItem();
        Reminder reminder = (Reminder) reminderCombo.getSelectedItem();

        TaskInfo updatedInfo = new TaskInfo(
                existingTask.getTaskInfo().getId(),
                name, start, end, priority, tag, reminder
        );
        updatedInfo.setTaskStatus(existingTask.getTaskInfo().getTaskStatus());

        return new Task(updatedInfo);
    }

    public static String getViewName() {
        return viewName;
    }

    public EditTaskViewModel getViewModel() {
        return viewModel;
    }

    public EditTaskController getController() {
        return controller;
    }

    public ViewManagerModel getViewManagerModel() {
        return viewManagerModel;
    }

    public String getMainViewKey() {
        return mainViewKey;
    }
}
