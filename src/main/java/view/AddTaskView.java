package view;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;
import interface_adapter.ViewManagerModel;
import interface_adapter.addTask.AddTaskController;
import interface_adapter.addTask.AddTaskViewModel;
import interface_adapter.addTask.AddTaskState;
import interface_adapter.create_customTag.CCTViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.createCustomTag.CustomTagDataAccessInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AddTaskView extends JPanel
        implements ActionListener, PropertyChangeListener {

    private final AddTaskController controller;
    private final AddTaskViewModel  viewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final CCTViewModel cctViewModel;
    private final CustomTagDataAccessInterface tagDao;
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
                       LoggedInViewModel loggedInViewModel,
                       CustomTagDataAccessInterface tagDao,
                       ViewManagerModel viewManagerModel) {
        this.controller = controller;
        this.viewModel  = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
        this.cctViewModel = new CCTViewModel();
        this.cctViewModel.addPropertyChangeListener(this);
        this.tagDao = tagDao;
        this.mainViewKey = LoggedInView.getViewName();
        this.viewModel.addPropertyChangeListener(this);

        // Initialize components
        nameField       = new JTextField(30);
        startSpinner    = makeDateTimeSpinner();
        endSpinner      = makeDateTimeSpinner();
        priorityCombo   = new JComboBox<>(Priority.values());

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


        Reminder[] reminderOptions = {
                Reminder.NONE,
                new Reminder(0),
                new Reminder(10),
                new Reminder(30),
                new Reminder(60),
                new Reminder(1440)
        };

        reminderCombo = new JComboBox<>(reminderOptions);
        reminderCombo.setSelectedItem(Reminder.NONE);

        saveButton      = new JButton("Save");
        cancelButton = new JButton("Cancel");
        errorLabel      = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
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
        if ("taskAdded".equals(propertyName) && st.isTaskAdded()) {
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
        if ("refreshTagOptions".equals(propertyName)) {
            Object newValue = evt.getNewValue();
            if (newValue instanceof java.util.List<?> updatedTagsRaw) {
                customTagCombo.setModel(new DefaultComboBoxModel<>(updatedTagsRaw.toArray()));
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
