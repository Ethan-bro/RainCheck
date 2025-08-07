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
    private final JLabel uvIndexLabel;
    private final TaskViewModel taskViewModel;

    public TaskBox(TaskViewModel taskViewModel,
                   MarkTaskCompleteController markTaskCompleteController,
                   DeleteTaskController deleteTaskController,
                   EditTaskController editTaskController,
                   ViewManagerModel viewManagerModel) {
        this.taskViewModel = taskViewModel;

        taskViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setPreferredSize(new Dimension(260, 160));
        setOpaque(true);
        updateDisplayColour();

        JPanel weatherPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        weatherPanel.setOpaque(false);
        weatherEmojiLabel = new JLabel();
        weatherEmojiLabel.setPreferredSize(new Dimension(30, 30));
        weatherDescriptionLabel = new JLabel();
        weatherDescriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        weatherPanel.add(weatherEmojiLabel);
        weatherPanel.add(weatherDescriptionLabel);
        uvIndexLabel = new JLabel();
        uvIndexLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        weatherPanel.add(uvIndexLabel);
        add(weatherPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        JButton completeButton = createImageButton("complete.png", "Complete", new Color(76, 175, 80), e -> {
            markTaskCompleteController.markAsComplete(taskViewModel.getTask().getTaskInfo().getId());
            closeDialog(e);
        });

        JButton editButton = createImageButton("edit.png", "Edit", new Color(33, 150, 243), e -> {
            editTaskController.setCurrentTask(taskViewModel.getTask());

            // Set the task on the EditTaskView
            EditTaskView editTaskView = (EditTaskView) viewManagerModel.getView(EditTaskView.getViewName());
            if (editTaskView != null) {
                editTaskView.setExistingTask(taskViewModel.getTask());
            }

            System.out.println("EDIT TASK IS CALLED. Task id " + editTaskView.getExistingTask().getTaskInfo().getId());

            closeDialog(e);
            editTaskController.switchToEditTaskView(viewManagerModel);
        });

        JButton deleteButton = createImageButton("delete.png", "Delete", new Color(244, 67, 54), e -> {
            try {
                deleteTaskController.deleteTask(taskViewModel.getTask().getTaskInfo().getId());
                closeDialog(e);
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

    private JButton createImageButton(String iconName, String tooltip, Color bgColor, java.awt.event.ActionListener action) {
        JButton button = new JButton();
        button.setToolTipText(tooltip);

        ImageIcon icon = loadIcon("/TaskBoxIcons/" + iconName);
        if (icon != null) {
            button.setIcon(icon);
        } else {
            button.setText(tooltip);
        }

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

    private ImageIcon loadIcon(String path) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                Image img = new ImageIcon(imgURL).getImage();
                Image scaledImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateDisplayColour() {
        String status = taskViewModel.getTask().getTaskInfo().getTaskStatus();
        Priority priority = taskViewModel.getTask().getTaskInfo().getPriority();

        if (Objects.equals(status, "Complete")) {
            setBackground(new Color(240, 240, 240));
        } else if (priority == Priority.HIGH) {
            setBackground(new Color(255, 235, 238));
        } else if (priority == Priority.MEDIUM) {
            setBackground(new Color(255, 243, 224));
        } else if (priority == Priority.LOW) {
            setBackground(new Color(255, 253, 231));
        } else {
            setBackground(Color.WHITE);
        }
    }

    private void updateContents() {
        var taskInfo = taskViewModel.getTask().getTaskInfo();

        if (taskInfo.getTag() != null) {
            tagNameLabel.setText(taskInfo.getTag().getTagName());
            tagEmojiLabel.setText(taskInfo.getTag().getTagIcon());
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

        String uv = taskInfo.getUvIndex();
        if (uv != null && !uv.isEmpty()) {
            uvIndexLabel.setText("UV: " + uv);
            uvIndexLabel.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Set UV index label font to bold

            try {
                int uvVal = Integer.parseInt(uv);
                if (uvVal <= 2) {
                    uvIndexLabel.setForeground(new Color(56, 142, 60)); // green
                } else if (uvVal <= 5) {
                    uvIndexLabel.setForeground(new Color(255, 193, 7)); // amber
                } else {
                    uvIndexLabel.setForeground(new Color(211, 47, 47)); // red
                }
            } catch (NumberFormatException ignored) {
                uvIndexLabel.setForeground(Color.DARK_GRAY);
            }
        } else {
            uvIndexLabel.setText("");
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void closeDialog(java.awt.event.ActionEvent e) {
        Component source = (Component) e.getSource();
        Window window = SwingUtilities.getWindowAncestor(source);
        if (window != null) {
            window.dispose();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateContents();
    }
}