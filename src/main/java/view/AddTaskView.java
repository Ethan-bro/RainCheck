package view;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;

import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskController;
import interface_adapter.addTask.AddTaskState;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.logged_in.LoggedInViewModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * AddTaskView uses AbstractTaskFormView for UI and handles interactions.
 */
public class AddTaskView extends AbstractTaskFormView implements ActionListener, PropertyChangeListener {

    private static final String VIEW_NAME = "Add Task";
    private static final int DAYS_IN_WEEK = 7;
    private static final int END_OF_WEEK_OFFSET = 6;

    private final AddTaskController controller;
    private final AddTaskViewModel viewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final String mainViewKey;

    /**
     * Creates an AddTaskView instance.
     *
     * @param controller       the Add Task controller
     * @param viewModel        the Add Task view model
     * @param loggedInViewModel the logged-in view model
     * @param viewManagerModel  the view manager model
     */
    public AddTaskView(AddTaskController controller,
                       AddTaskViewModel viewModel,
                       LoggedInViewModel loggedInViewModel,
                       ViewManagerModel viewManagerModel) {
        super(VIEW_NAME, "Save", viewModel.getTagOptions());

        this.controller = controller;
        this.viewModel = viewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
        this.mainViewKey = LoggedInView.getViewName();

        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);

        viewModel.addPropertyChangeListener(this);
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            final String name = nameField.getText().trim();
            final LocalDateTime start = toLocalDateTime((Date) startSpinner.getValue());
            final LocalDateTime end = toLocalDateTime((Date) endSpinner.getValue());
            final Priority priority = (Priority) priorityCombo.getSelectedItem();
            final CustomTag customTag = (CustomTag) customTagCombo.getSelectedItem();
            final Reminder reminder = (Reminder) reminderCombo.getSelectedItem();

            controller.execute(name, start, end, priority, customTag, reminder);

        }
        else if (e.getSource() == cancelButton) {
            goBackToCalendarView();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String propertyName = evt.getPropertyName();
        final AddTaskState state = viewModel.getState();

        switch (propertyName) {
            case "errorMessage" -> {
                final String msg = state.getErrorMessage();
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

                    final LocalDate today = LocalDate.now();
                    final LocalDate startOfWeek =
                            today.minusDays(today.getDayOfWeek().getValue() % DAYS_IN_WEEK);
                    final LocalDate endOfWeek = startOfWeek.plusDays(END_OF_WEEK_OFFSET);
                    loggedInViewModel.loadTasksForWeek(startOfWeek, endOfWeek);
                }
            }
            case "refreshTagOptions" -> {
                final Object newValue = evt.getNewValue();
                if (newValue instanceof List<?> updatedTagsRaw) {
                    customTagCombo.setModel(new DefaultComboBoxModel<>(updatedTagsRaw.toArray()));
                    customTagCombo.setEnabled(!updatedTagsRaw.isEmpty());
                }
            }
            default -> {
                // No action needed for other property changes
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

    /**
     * Gets the view name.
     *
     * @return the view name
     */
    public static String getViewName() {
        return VIEW_NAME;
    }
}
