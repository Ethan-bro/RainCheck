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
import java.util.List;

public class EditTaskView extends JPanel implements PropertyChangeListener {

    private Task existingTask;
    private final EditTaskController controller;
    private final EditTaskViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final String mainViewKey;
    private static final String viewName = "Edit Task";

    // Form fields
    private JTextField nameField = null;
    private JSpinner startSpinner = null;
    private JSpinner endSpinner = null;
    private JComboBox<Priority> priorityCombo = null;
    private JComboBox<Object> customTagCombo = null;
    private JComboBox<Reminder> reminderCombo = null;
    private JButton saveButton = null;
    private JButton cancelButton = null;
    private JLabel errorLabel = null;

    public EditTaskView(EditTaskController controller,
                        EditTaskViewModel viewModel,
                        ViewManagerModel viewManagerModel,
                        String mainViewKey) {

        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;
        this.mainViewKey = mainViewKey;
        this.existingTask = null; // we set later in setExistingTask

        initView();
    }

    public void setExistingTask(Task existingTask) {
        this.existingTask = existingTask;
        if (existingTask == null) return;

        TaskInfo info = existingTask.getTaskInfo();
        nameField.setText(info.getTaskName());
        startSpinner.setValue(Date.from(info.getStartDateTime().atZone(ZoneId.systemDefault()).toInstant()));
        endSpinner.setValue(Date.from(info.getEndDateTime().atZone(ZoneId.systemDefault()).toInstant()));
        priorityCombo.setSelectedItem(info.getPriority());

        if (info.getTag() != null) {
            customTagCombo.setSelectedItem(info.getTag());
        }

        if (info.getReminder() != null) {
            reminderCombo.setSelectedItem(info.getReminder());
        }
    }

    private void initView() {
        nameField = new JTextField(30);
        startSpinner = makeDateTimeSpinner(LocalDateTime.now());
        endSpinner = makeDateTimeSpinner(LocalDateTime.now().plusHours(1));
        priorityCombo = new JComboBox<>(Priority.values());

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

        Reminder[] reminderOptions = {
                Reminder.NONE,
                new Reminder(0), new Reminder(10),
                new Reminder(30), new Reminder(60),
                new Reminder(1440)
        };
        reminderCombo = new JComboBox<>(reminderOptions);

        saveButton = new JButton("Save Changes");
        saveButton.addActionListener(evt -> {
            Task updatedTask = buildUpdatedTask();
            controller.editTask(updatedTask);
        });

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(evt -> {
            viewManagerModel.setState(mainViewKey);
            viewManagerModel.firePropertyChanged();
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
        String prop = evt.getPropertyName();

        if ("refreshTagOptions".equals(prop)) {
            Object newValue = evt.getNewValue();
            if (newValue instanceof java.util.List<?> updatedTagsRaw) {
                customTagCombo.setModel(new DefaultComboBoxModel<>(updatedTagsRaw.toArray()));
            }
            return;
        }

        if ("task updated".equals(prop)) {
            viewManagerModel.setState(mainViewKey);
            viewManagerModel.firePropertyChanged();
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
}
