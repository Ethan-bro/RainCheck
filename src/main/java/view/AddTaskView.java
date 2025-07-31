package view;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskController;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.addTask.AddTaskState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class AddTaskView extends JPanel
        implements ActionListener, PropertyChangeListener {

    private final AddTaskController controller;
    private final AddTaskViewModel  viewModel;
    private final ViewManagerModel viewManagerModel;
    private final String mainViewKey;
    private static final String viewName = "Add Task";

    // Form fields
    private final JTextField            nameField;
    private final JSpinner              startSpinner;
    private final JSpinner              endSpinner;
    private final JComboBox<Priority>     priorityCombo;
    private final JComboBox<Object> customTagCombo;
    private final JComboBox<Reminder>    reminderCombo;
    private final JButton               saveButton;
    private final JButton               cancelButton;
    private final JLabel                errorLabel;

    public AddTaskView(AddTaskController controller,
                       AddTaskViewModel viewModel,
                       ViewManagerModel viewManagerModel) {
        this.controller = controller;
        this.viewModel  = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;
        this.mainViewKey = LoggedInView.getViewName();
        this.viewModel.addPropertyChangeListener(this);

        // Initialize components
        nameField       = new JTextField(30);
        startSpinner    = makeDateTimeSpinner();
        endSpinner      = makeDateTimeSpinner();
        priorityCombo   = new JComboBox<>(Priority.values());

        // TODO: Populate customTagCombo with user's tags loaded from database or some defaults.
        // Example:
        // CustomTag[] defaultTags = {
        //     new CustomTag("Work", "💼"),
        //     new CustomTag("Study", "📚"),
        //     new CustomTag("Chill", "😎")
        // };
        // customTagCombo = new JComboBox<>(defaultTags);
        List<Object> options = viewModel.getTagOptions();
        customTagCombo = new JComboBox<>(options.toArray());
        customTagCombo.addActionListener(e -> {
            Object selectedItem = customTagCombo.getSelectedItem();
            if ("Create New Tag...".equals(selectedItem)) {
                controller.createCustomTag();
                customTagCombo.setModel(
                        new DefaultComboBoxModel<>(viewModel.getTagOptions().toArray())
                );
            }
        });



        // TODO: Populate reminderCombo with typical reminder intervals or user preferences.
        // Example:
        // Reminder[] defaultReminders = {
        //     new Reminder(5),    // 5 minutes before
        //     new Reminder(10),   // 10 minutes before
        //     new Reminder(30)    // 30 minutes before
        // };
        // reminderCombo = new JComboBox<>(defaultReminders);
        reminderCombo = new JComboBox<>();

        saveButton      = new JButton("Save");
        cancelButton = new JButton("Cancel");
        errorLabel      = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);

        // Layout with GridBagLayout
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Add New Task"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.gridx  = 0; gbc.gridy = 0;

        add(new JLabel("Task Name:"), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Start Date/Time:"), gbc);
        gbc.gridx = 1;
        add(startSpinner, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("End Date/Time:"), gbc);
        gbc.gridx = 1;
        add(endSpinner, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Priority:"), gbc);
        gbc.gridx = 1;
        add(priorityCombo, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        add(customTagCombo, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Reminder (min before):"), gbc);
        gbc.gridx = 1;
        add(reminderCombo, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        add(errorLabel, gbc);

        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 1;
        add(saveButton, gbc);
        gbc.gridx = 1;
        add(cancelButton, gbc);
    }

    private JSpinner makeDateTimeSpinner() {
        SpinnerDateModel model =
                new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm"));
        return spinner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String name     = nameField.getText().trim();
            LocalDateTime start = toLocalDateTime((Date) startSpinner.getValue());
            LocalDateTime end   = toLocalDateTime((Date) endSpinner.getValue());
            Priority priority = (Priority) priorityCombo.getSelectedItem();
            CustomTag customTag = (CustomTag) customTagCombo.getSelectedItem();
            Reminder reminder = (Reminder) reminderCombo.getSelectedItem();

            controller.execute(name, start, end, priority, customTag, reminder);
        } else if (e.getSource() == cancelButton) {
            goBackToCalendarView();
        }
    }

    private LocalDateTime toLocalDateTime(Date d) {
        return LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        AddTaskState st = viewModel.getState();

        // Show or hide validation error
        if ("errorMessage".equals(propertyName)) {         // ← only update on error changes
            String msg = st.getErrorMessage();
            errorLabel.setText(msg);
            errorLabel.setVisible(msg != null && !msg.isBlank());
        }
        if ("taskAdded".equals(propertyName)) {            // ← only respond on success
            if (st.isTaskAdded()) {
                JOptionPane.showMessageDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "Task added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                resetForm();
                goBackToCalendarView();
            }
        }
    }
    private void resetForm() {
        nameField.setText("");
        startSpinner.setValue(new Date());
        endSpinner.setValue(new Date());
        priorityCombo.setSelectedIndex(0);
        customTagCombo.setSelectedIndex(0);
        reminderCombo.setSelectedIndex(0);
        errorLabel.setVisible(false);
    }

    private void goBackToCalendarView() {
        viewManagerModel.setState(mainViewKey);
        viewManagerModel.firePropertyChanged();
    }

    public static String getViewName() {return viewName;}
}
