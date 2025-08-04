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
import javax.swing.border.EmptyBorder;
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

/**
 * Polished AddTaskView with modern UI design.
 */
public class AddTaskView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final String viewName = "Add Task";

    private final AddTaskController controller;
    private final AddTaskViewModel viewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final CCTViewModel cctViewModel;
    private final CustomTagDataAccessInterface tagDao;
    private final ViewManagerModel viewManagerModel;
    private final String mainViewKey;

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

    public AddTaskView(AddTaskController controller,
                       AddTaskViewModel viewModel,
                       LoggedInViewModel loggedInViewModel,
                       CustomTagDataAccessInterface tagDao,
                       ViewManagerModel viewManagerModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.tagDao = tagDao;
        this.viewManagerModel = viewManagerModel;
        this.cctViewModel = new CCTViewModel();
        this.mainViewKey = LoggedInView.getViewName();

        this.viewModel.addPropertyChangeListener(this);
        this.cctViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Title
        JLabel titleLabel = new JLabel(viewName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(45, 45, 45));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Main Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels font & color
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color labelColor = new Color(60, 60, 60);

        // Task Name
        JLabel nameLabel = new JLabel("Task Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(labelColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(nameLabel, gbc);

        nameField = new JTextField(30);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(nameField, gbc);

        // Start Date/Time
        JLabel startLabel = new JLabel("Start Date/Time:");
        startLabel.setFont(labelFont);
        startLabel.setForeground(labelColor);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        formPanel.add(startLabel, gbc);

        startSpinner = makeDateTimeSpinner();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(startSpinner, gbc);

        // End Date/Time
        JLabel endLabel = new JLabel("End Date/Time:");
        endLabel.setFont(labelFont);
        endLabel.setForeground(labelColor);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        formPanel.add(endLabel, gbc);

        endSpinner = makeDateTimeSpinner();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(endSpinner, gbc);

        // Priority
        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setFont(labelFont);
        priorityLabel.setForeground(labelColor);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        formPanel.add(priorityLabel, gbc);

        priorityCombo = new JComboBox<>(Priority.values());
        priorityCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(priorityCombo, gbc);

        // Category (Custom Tag)
        JLabel tagLabel = new JLabel("Category:");
        tagLabel.setFont(labelFont);
        tagLabel.setForeground(labelColor);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        formPanel.add(tagLabel, gbc);

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

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(customTagCombo, gbc);

        // Reminder
        JLabel reminderLabel = new JLabel("Reminder (min before):");
        reminderLabel.setFont(labelFont);
        reminderLabel.setForeground(labelColor);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        formPanel.add(reminderLabel, gbc);

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
        reminderCombo.setSelectedItem(Reminder.NONE);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(reminderCombo, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Error label
        errorLabel = new JLabel();
        errorLabel.setForeground(new Color(220, 20, 60)); // Crimson red
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        errorLabel.setVisible(false);

        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.setBackground(Color.WHITE);
        errorPanel.add(errorLabel);

        add(errorPanel, BorderLayout.SOUTH);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonsPanel.setBackground(Color.WHITE);

        saveButton = new JButton("Save");
        styleButton(saveButton, new Color(34, 139, 34)); // Forest Green

        cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(178, 34, 34)); // Firebrick Red

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        // Listeners
        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(130, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    private JSpinner makeDateTimeSpinner() {
        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm"));
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return spinner;
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

    private LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
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
