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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

public class EditTaskView extends AbstractTaskFormView implements ActionListener, PropertyChangeListener {

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
        final LocalDateTime now = LocalDateTime.now();
        final Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        startSpinner.setValue(nowDate);
        endSpinner.setValue(nowDate);
    }

    /**
     * Sets the existing task data to be edited.
     *
     * @param task The task to edit.
     */
    public void setExistingTask(Task task) {
        this.existingTask = task;
        if (task != null) {
            final TaskInfo info = task.getTaskInfo();

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
     * Returns the existing task being edited.
     *
     * @return The existing Task object.
     */
    public Task getExistingTask() {
        return existingTask;
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private Task buildUpdatedTask() {
        final String name = nameField.getText().trim();
        final LocalDateTime start = toLocalDateTime((Date) startSpinner.getValue());
        final LocalDateTime end = toLocalDateTime((Date) endSpinner.getValue());
        final Priority priority = (Priority) priorityCombo.getSelectedItem();
        final CustomTag tag = (CustomTag) customTagCombo.getSelectedItem();
        final Reminder reminder = (Reminder) reminderCombo.getSelectedItem();

        final TaskInfo oldInfo = existingTask.getTaskInfo();

        final TaskInfo updatedInfo = new TaskInfo();

        // Step 1: Core details
        updatedInfo.setCoreDetails(
                oldInfo.getId(),
                name,
                start,
                end
        );

        // Step 2: Additional details
        updatedInfo.setAdditionalDetails(
                priority,
                tag,
                reminder,
                oldInfo.getIsDeleted()
        );

        // Step 3: Weather details
        updatedInfo.setWeatherInfo(
                oldInfo.getWeatherDescription(),
                oldInfo.getWeatherIconName(),
                oldInfo.getTemperature(),
                oldInfo.getUvIndex()
        );

        updatedInfo.setTaskStatus(oldInfo.getTaskStatus());

        return new Task(updatedInfo);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == saveButton) {
            controller.editTask(buildUpdatedTask());
        }
        else if (actionEvent.getSource() == cancelButton) {
            viewManagerModel.setState(mainViewKey);
            viewManagerModel.firePropertyChanged();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String prop = evt.getPropertyName();

        if ("refreshTagOptions".equals(prop)) {
            final Object newValue = evt.getNewValue();
            if (newValue instanceof List<?> updatedTags) {
                customTagCombo.setModel(new DefaultComboBoxModel<>(updatedTags.toArray()));
                customTagCombo.setEnabled(!updatedTags.isEmpty());
            }
        }
        else {
            final EditTaskState state = viewModel.getState();

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
