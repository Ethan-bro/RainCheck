package view;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Objects;

import entity.Priority;
import interface_adapter.ViewManagerModel;
import interface_adapter.deleteTask.DeleteTaskController;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.markTaskComplete.MarkTaskCompleteController;
import interface_adapter.task.TaskViewModel;

public class TaskBox extends JPanel implements PropertyChangeListener {

    private final JLabel tagNameLabel;
    private final JLabel tagEmojiLabel;
    private final JLabel titleLabel;
    private final JLabel weatherDescriptionLabel;
    private final JLabel weatherEmojiLabel;

    private final MarkTaskCompleteController markTaskCompleteController;
    private final DeleteTaskController deleteTaskController;
    private final EditTaskController editTaskController;
    private final TaskViewModel taskViewModel;
    private final ViewManagerModel viewManagerModel;

    public TaskBox(TaskViewModel taskViewModel,
                   MarkTaskCompleteController markTaskCompleteController,
                   DeleteTaskController deleteTaskController,
                   EditTaskController editTaskController,
                   ViewManagerModel viewManagerModel) {
        this.taskViewModel = taskViewModel;
        this.markTaskCompleteController = markTaskCompleteController;
        this.deleteTaskController = deleteTaskController;
        this.editTaskController = editTaskController;
        this.viewManagerModel = viewManagerModel;

        taskViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setPreferredSize(new Dimension(260, 160));
        setOpaque(true);
        updateDisplayColour();

        // TOP: Weather Panel (centered)
        JPanel weatherPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        weatherPanel.setOpaque(false);
        weatherEmojiLabel = new JLabel();
        weatherEmojiLabel.setPreferredSize(new Dimension(30, 30));
        weatherDescriptionLabel = new JLabel();
        weatherDescriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        weatherPanel.add(weatherEmojiLabel);
        weatherPanel.add(weatherDescriptionLabel);
        add(weatherPanel, BorderLayout.NORTH);

        // CENTER: Task Title and Tag (centered vertically)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Add vertical glue to push content to center
        centerPanel.add(Box.createVerticalGlue());

        JPanel titleTagPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        titleTagPanel.setOpaque(false);
        tagEmojiLabel = new JLabel();
        tagNameLabel = new JLabel();
        titleLabel = new JLabel(taskViewModel.getTask().getTaskInfo().getTaskName());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleTagPanel.add(tagEmojiLabel);
        titleTagPanel.add(tagNameLabel);
        titleTagPanel.add(titleLabel);

        centerPanel.add(titleTagPanel);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);

        // BOTTOM: Buttons Panel (centered)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        JButton completeButton = createStyledButton("✔", "Complete", new Color(76, 175, 80), e ->
                markTaskCompleteController.markAsComplete(taskViewModel.getTask().getTaskInfo().getId())
        );

        JButton editButton = createStyledButton("✎", "Edit", new Color(33, 150, 243), e -> {
            editTaskController.setCurrentTask(taskViewModel.getTask());
            editTaskController.switchToEditTaskView(viewManagerModel);
        });

        JButton deleteButton = createStyledButton("🗑", "Delete", new Color(244, 67, 54), e -> {
            try {
                deleteTaskController.deleteTask(taskViewModel.getTask().getTaskInfo().getId());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        buttonPanel.add(completeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        updateContents();
    }

    private JButton createStyledButton(String text, String tooltip, Color bgColor, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker()),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);

        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void updateDisplayColour() {
        String status = taskViewModel.getTask().getTaskInfo().getTaskStatus();
        Priority priority = taskViewModel.getTask().getTaskInfo().getPriority();

        if (Objects.equals(status, "Complete")) {
            setBackground(new Color(240, 240, 240));
        } else if (priority == Priority.HIGH) {
            setBackground(new Color(255, 235, 238)); // Lighter red
        } else if (priority == Priority.MEDIUM) {
            setBackground(new Color(255, 243, 224)); // Lighter orange
        } else if (priority == Priority.LOW) {
            setBackground(new Color(255, 253, 231)); // Lighter yellow
        } else {
            setBackground(Color.WHITE);
        }
    }

    private void updateContents() {
        var taskInfo = taskViewModel.getTask().getTaskInfo();

        if (taskInfo.getTag() != null) {
            tagNameLabel.setText(taskInfo.getTag().getTagName());
            tagEmojiLabel.setText(taskInfo.getTag().getTagEmoji());
            tagNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        } else {
            tagNameLabel.setText("");
            tagEmojiLabel.setText("");
        }

        titleLabel.setText(taskInfo.getTaskName());
        weatherDescriptionLabel.setText(taskInfo.getWeatherDescription());

        String iconName = taskInfo.getWeatherIconName();
        if (iconName != null && !iconName.isEmpty()) {
            ImageIcon icon = loadWeatherIcon(iconName);
            if (icon != null) {
                weatherEmojiLabel.setIcon(icon);
                weatherEmojiLabel.setText("");
            } else {
                weatherEmojiLabel.setIcon(null);
                weatherEmojiLabel.setText(iconName);
            }
        } else {
            weatherEmojiLabel.setIcon(null);
            weatherEmojiLabel.setText("");
        }

        updateDisplayColour();
        revalidate();
        repaint();
    }

    private ImageIcon loadWeatherIcon(String iconName) {
        String iconPath = "/weatherIcons/" + iconName + ".png";
        try {
            java.net.URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) {
                Image img = new ImageIcon(imgURL).getImage();
                Image scaledImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            } else {
                System.err.println("Could not find icon: " + iconPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateContents();
    }
}