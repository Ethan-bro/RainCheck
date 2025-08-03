package view;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;
import entity.Task;
import entity.TaskInfo;
import interface_adapter.ViewManagerModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskState;
import interface_adapter.editTask.EditTaskViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class EditTaskView extends JPanel implements PropertyChangeListener {

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
        this.viewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;
        this.mainViewKey = mainViewKey;
        this.existingTask = controller.getCurrentTask();

        nameField = new JTextField(existingTask.getTaskInfo().getTaskName(), 30);
        startSpinner = makeDateTimeSpinner(existingTask.getTaskInfo().getStartDateTime());
        endSpinner = makeDateTimeSpinner(existingTask.getTaskInfo().getEndDateTime());
        priorityCombo = new JComboBox<>(Priority.values());
        priorityCombo.setSelectedItem(existingTask.getTaskInfo().getPriority());

        // setting up tags
        java.util.List<Object> tagOptions = viewModel.getTagOptions();
        customTagCombo = new JComboBox<>(tagOptions.toArray());

        if (existingTask.getTaskInfo().getTag() != null) {
            customTagCombo.setSelectedItem(existingTask.getTaskInfo().getTag());
        }

        customTagCombo.addActionListener(e -> {
            Object selectedItem = customTagCombo.getSelectedItem();
            if (selectedItem instanceof String && selectedItem.equals("Create New Tag...")) {
                controller.createCustomTag();
                java.util.List<Object> updatedTags = viewModel.getTagOptions();
                customTagCombo.setModel(new DefaultComboBoxModel<>(updatedTags.toArray()));
            }
        });

        // setting up reminders
        Reminder[] reminderOptions = {
                Reminder.NONE,
                new Reminder(0),
                new Reminder(10),
                new Reminder(30),
                new Reminder(60),
                new Reminder(1440)
        };
        reminderCombo = new JComboBox<>(reminderOptions);

        if (existingTask.getTaskInfo().getReminder() != null) {
            reminderCombo.setSelectedItem(existingTask.getTaskInfo().getReminder());
        }

        saveButton = new JButton("Save Changes");
        saveButton.addActionListener(evt -> {
            Task updatedTask = buildUpdatedTask();
            controller.editTask(updatedTask);
                });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(evt -> {

            Window w = SwingUtilities.getWindowAncestor(EditTaskView.this);
            if (w instanceof JDialog) {
                w.dispose();
            }

            viewManagerModel.setState(mainViewKey);
            viewManagerModel.firePropertyChanged("state");
                });
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);

        // Layout with GridBagLayout
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
        String name = nameField.getText().trim();
        LocalDateTime start = toLocalDateTime((Date) startSpinner.getValue());
        LocalDateTime end = toLocalDateTime((Date) endSpinner.getValue());
        Priority priority = (Priority) priorityCombo.getSelectedItem();

        Object rawTag = customTagCombo.getSelectedItem();
        CustomTag tag;
        if (rawTag instanceof CustomTag) {
            tag = (CustomTag) rawTag;
        } else {
            tag = null;
        }

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
                oldInfo.getWeatherDescription(),
                oldInfo.getWeatherIconName(),
                oldInfo.getTemperature()
        );

        updatedInfo.setTaskStatus(oldInfo.getTaskStatus());

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

    /**
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        EditTaskState state = viewModel.getState();

        if (state.isSuccess()) {
            viewManagerModel.setState(mainViewKey);
            viewManagerModel.firePropertyChanged("state");
        } else if (state.getError() != null) {
            errorLabel.setText(state.getError());
            errorLabel.setVisible(true);
        }
    }
}
