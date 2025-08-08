package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;
import entity.Task;
import entity.TaskInfo;
import interface_adapter.ViewManagerModel;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.editTask.EditTaskState;
import interface_adapter.editTask.EditTaskViewModel;

/**
 * View for editing an existing task.
 */
public final class EditTaskView extends AbstractTaskFormView implements ActionListener, PropertyChangeListener {

    private static final String VIEW_NAME = "Edit Task";

    private final EditTaskController controller;
    private final EditTaskViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final String mainViewKey;

    private Task existingTask;

    public EditTaskView(EditTaskController controller,
                        EditTaskViewModel viewModel,
                        ViewManagerModel viewManagerModel,
                        String mainViewKey) {

        super(VIEW_NAME, "Save Changes", viewModel.getTagOptions());

        this.controller = controller;
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.mainViewKey = mainViewKey;

        viewModel.addPropertyChangeListener(this);

        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);

        initializeSpinnersWithDefaults();
    }

    private void initializeSpinnersWithDefaults() {
        LocalDateTime now = LocalDateTime.now();
        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        startSpinner.setValue(nowDate);
        endSpinner.setValue(nowDate);
    }

    /**
     * Sets the task to populate the form fields.
     * @param task the existing task to edit
     */
    public void setExistingTask(Task task) {
        this.existingTask = task;
        if (!(task == null)) {

            TaskInfo info = task.getTaskInfo();
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
    }

    /**
     * For debugging print purposes.
     * @return Task the task already existing in the database
     */
    public Task getExistingTask() {
        return existingTask;
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
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
                oldInfo.getTemperature(),
                oldInfo.getUvIndex()
        );
        updatedInfo.setTaskStatus(oldInfo.getTaskStatus());
        return new Task(updatedInfo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            controller.editTask(buildUpdatedTask());
        }
        else if (e.getSource() == cancelButton) {
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
        }
        else {
            EditTaskState state = viewModel.getState();
            if (state.isSuccess()) {
                viewManagerModel.setState(mainViewKey);
                viewManagerModel.firePropertyChanged();
            }
            else if (state.getError() != null) {
                errorLabel.setText(state.getError());
                errorLabel.setVisible(true);
            }
        }
    }

    public static String getViewName() {
        return VIEW_NAME;
    }
}
