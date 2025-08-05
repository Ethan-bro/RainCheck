package view;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskController;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.addTask.AddTaskState;
import interface_adapter.logged_in.LoggedInViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * AddTaskView uses AbstractTaskFormView for UI and handles interactions.
 */
public class AddTaskView extends AbstractTaskFormView implements ActionListener, PropertyChangeListener {

    private static final String viewName = "Add Task";

    private final AddTaskController controller;
    private final AddTaskViewModel viewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final String mainViewKey;

    public AddTaskView(AddTaskController controller,
                       AddTaskViewModel viewModel,
                       LoggedInViewModel loggedInViewModel,
                       ViewManagerModel viewManagerModel) {
        super(viewName, "Save", viewModel.getTagOptions());

        this.controller = controller;
        this.viewModel = viewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
        this.mainViewKey = LoggedInView.getViewName();

        // Add listeners for buttons inherited from AbstractTaskFormView
        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);

        // Listen for ViewModel property changes
        viewModel.addPropertyChangeListener(this);
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String name = nameField.getText().trim();
            LocalDateTime start = toLocalDateTime((Date) startSpinner.getValue());
            LocalDateTime end = toLocalDateTime((Date) endSpinner.getValue());
            Priority priority = (Priority) priorityCombo.getSelectedItem();
            CustomTag customTag = (CustomTag) customTagCombo.getSelectedItem();
            Reminder reminder = (Reminder) reminderCombo.getSelectedItem();

            controller.execute(name, start, end, priority, customTag, reminder);

        } else if (e.getSource() == cancelButton) {
            goBackToCalendarView();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        AddTaskState state = viewModel.getState();

        switch (propertyName) {
            case "errorMessage" -> {
                String msg = state.getErrorMessage();
                errorLabel.setText(msg);
                errorLabel.setVisible(msg != null && !msg.isBlank());
            }
            case "taskAdded" -> {
                if (state.isTaskAdded()) {
                    JOptionPane.showMessageDialog(
                            SwingUtilities.getWindowAncestor(this),
                            "Task added successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    resetForm();
                    goBackToCalendarView();

                    LocalDate today = LocalDate.now();
                    LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() % 7);
                    LocalDate endOfWeek = startOfWeek.plusDays(6);
                    loggedInViewModel.loadTasksForWeek(startOfWeek, endOfWeek);
                }
            }
            case "refreshTagOptions" -> {
                Object newValue = evt.getNewValue();
                if (newValue instanceof List<?> updatedTagsRaw) {
                    customTagCombo.setModel(new DefaultComboBoxModel<>(updatedTagsRaw.toArray()));
                    customTagCombo.setEnabled(!updatedTagsRaw.isEmpty());
                }
            }
        }
    }

    private void resetForm() {
        nameField.setText("");
        startSpinner.setValue(new Date());
        endSpinner.setValue(new Date());
        priorityCombo.setSelectedIndex(0);
        if (customTagCombo.getItemCount() > 0) {
            customTagCombo.setSelectedIndex(0);
        }
        reminderCombo.setSelectedIndex(0);
        errorLabel.setVisible(false);
    }

    private void goBackToCalendarView() {
        viewManagerModel.setState(mainViewKey);
        viewManagerModel.firePropertyChanged();
    }

    public static String getViewName() {
        return viewName;
    }
}
