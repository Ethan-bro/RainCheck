package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import entity.Priority;
import interface_adapter.ViewManagerModel;
import interface_adapter.deleteTask.DeleteTaskController;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.markTaskComplete.MarkTaskCompleteController;
import interface_adapter.task.TaskViewModel;

public class TaskBox extends JPanel implements PropertyChangeListener {

    private static final String FONT = "Segoe UI";

    private static final int GAP_SMALL = 5;
    private static final int PADDING = 15;
    private static final int PREF_W = 260;
    private static final int PREF_H = 160;
    private static final int WEATHER_ICON = 30;
    private static final int TASK_ICON = 20;
    private static final int FONT_10 = 10;
    private static final int FONT_12 = 12;
    private static final int FONT_13 = 13;
    private static final int FONT_16 = 16;

    private static final Color COLOR_BORDER = new Color(200, 200, 200);
    private static final Color COLOR_COMPLETE = new Color(240, 240, 240);
    private static final Color COLOR_PRI_HIGH = new Color(255, 235, 238);
    private static final Color COLOR_PRI_MED = new Color(255, 243, 224);
    private static final Color COLOR_PRI_LOW = new Color(255, 253, 231);

    private static final Color BTN_COMPLETE = new Color(76, 175, 80);
    private static final Color BTN_EDIT = new Color(33, 150, 243);
    private static final Color BTN_DELETE = new Color(244, 67, 54);

    private static final Color UV_LOW = new Color(56, 142, 60);
    private static final Color UV_MED = new Color(255, 193, 7);
    private static final Color UV_HIGH = new Color(211, 47, 47);

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

        setLayout(new BorderLayout(0, GAP_SMALL));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)
        ));
        setPreferredSize(new Dimension(PREF_W, PREF_H));
        setOpaque(true);
        updateDisplayColour();

        JPanel weatherPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP_SMALL, 0));
        weatherPanel.setOpaque(false);
        weatherEmojiLabel = new JLabel();
        weatherEmojiLabel.setPreferredSize(new Dimension(WEATHER_ICON, WEATHER_ICON));
        weatherDescriptionLabel = new JLabel();
        weatherDescriptionLabel.setFont(new Font(FONT, Font.PLAIN, FONT_13));
        weatherPanel.add(weatherEmojiLabel);
        weatherPanel.add(weatherDescriptionLabel);
        uvIndexLabel = new JLabel();
        uvIndexLabel.setFont(new Font(FONT, Font.PLAIN, FONT_13));
        weatherPanel.add(uvIndexLabel);
        add(weatherPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createVerticalGlue());

        JPanel titleTagPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP_SMALL, 0));
        titleTagPanel.setOpaque(false);
        tagEmojiLabel = new JLabel();
        tagNameLabel = new JLabel();
        titleLabel = new JLabel(taskViewModel.getTask().getTaskInfo().getTaskName());
        titleLabel.setFont(new Font(FONT, Font.BOLD, FONT_16));
        titleTagPanel.add(tagEmojiLabel);
        titleTagPanel.add(tagNameLabel);
        titleTagPanel.add(titleLabel);

        centerPanel.add(titleTagPanel);
        centerPanel.add(Box.createVerticalGlue());
        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, FONT_10, 0));
        buttonPanel.setOpaque(false);

        JButton completeButton = createImageButton(
                "complete.png", "Complete", BTN_COMPLETE, event -> {
                markTaskCompleteController.markAsComplete(taskViewModel.getTask().getTaskInfo().getId());
                closeDialog(event);
            });

        JButton editButton = createImageButton("edit.png", "Edit", BTN_EDIT, event -> {
            editTaskController.setCurrentTask(taskViewModel.getTask());
            EditTaskView editTaskView = (EditTaskView) viewManagerModel.getView(EditTaskView.getViewName());
            if (editTaskView != null) {
                editTaskView.setExistingTask(taskViewModel.getTask());
            }
            System.out.println("EDIT TASK IS CALLED. Task id " + editTaskView.getExistingTask().getTaskInfo().getId());
            closeDialog(event);
            editTaskController.switchToEditTaskView(viewManagerModel);
        });

        JButton deleteButton = createImageButton("delete.png", "Delete", BTN_DELETE, event -> {
            try {
                deleteTaskController.deleteTask(taskViewModel.getTask().getTaskInfo().getId());
            }
            catch (IOException eventHere) {
                throw new RuntimeException(eventHere);
            }
            closeDialog(event);
        });

        buttonPanel.add(completeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        updateContents();
    }

    private JButton createImageButton(
            String iconName, String tooltip, Color bgColor, java.awt.event.ActionListener action) {
        JButton button = new JButton();
        button.setToolTipText(tooltip);

        ImageIcon icon = loadIcon("/TaskBoxIcons/" + iconName);
        if (icon != null) {
            button.setIcon(icon);
        }
        else {
            button.setText(tooltip);
        }

        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker()),
                BorderFactory.createEmptyBorder(GAP_SMALL, FONT_10, GAP_SMALL, FONT_10)
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
        java.net.URL imgUrl = getClass().getResource(path);
        if (imgUrl != null) {
            Image img = new ImageIcon(imgUrl).getImage();
            Image scaledImg = img.getScaledInstance(TASK_ICON, TASK_ICON, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        }
        return null;
    }

    private void updateDisplayColour() {
        String status = taskViewModel.getTask().getTaskInfo().getTaskStatus();
        Priority priority = taskViewModel.getTask().getTaskInfo().getPriority();

        if (Objects.equals(status, "Complete")) {
            setBackground(COLOR_COMPLETE);
        }
        else if (priority == Priority.HIGH) {
            setBackground(COLOR_PRI_HIGH);
        }
        else if (priority == Priority.MEDIUM) {
            setBackground(COLOR_PRI_MED);
        }
        else if (priority == Priority.LOW) {
            setBackground(COLOR_PRI_LOW);
        }
        else {
            setBackground(Color.WHITE);
        }
    }

    private void updateContents() {
        var taskInfo = taskViewModel.getTask().getTaskInfo();

        if (taskInfo.getTag() != null) {
            tagNameLabel.setText(taskInfo.getTag().getTagName());
            tagEmojiLabel.setText(taskInfo.getTag().getTagIcon());
            tagNameLabel.setFont(new Font(FONT, Font.PLAIN, FONT_12));
        }
        else {
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
            }
            else {
                weatherEmojiLabel.setIcon(null);
                weatherEmojiLabel.setText(iconName);
            }
        }
        else {
            weatherEmojiLabel.setIcon(null);
            weatherEmojiLabel.setText("");
        }

        String uv = taskInfo.getUvIndex();
        if (uv != null && !uv.isEmpty()) {
            uvIndexLabel.setText("UV: " + uv);
            uvIndexLabel.setFont(new Font(FONT, Font.BOLD, FONT_13));

            try {
                int uvVal = Integer.parseInt(uv);
                if (uvVal <= 2) {
                    uvIndexLabel.setForeground(UV_LOW);
                }
                else if (uvVal <= GAP_SMALL) {
                    uvIndexLabel.setForeground(UV_MED);
                }
                else {
                    uvIndexLabel.setForeground(UV_HIGH);
                }
            }
            catch (NumberFormatException ignored) {
                uvIndexLabel.setForeground(Color.DARK_GRAY);
            }
        }
        else {
            uvIndexLabel.setText("");
        }

        updateDisplayColour();
        revalidate();
        repaint();
    }

    private ImageIcon loadWeatherIcon(String iconName) {
        String iconPath = "/weatherIcons/" + iconName + ".png";
        java.net.URL imgUrl = getClass().getResource(iconPath);
        if (imgUrl != null) {
            Image img = new ImageIcon(imgUrl).getImage();
            Image scaledImg = img.getScaledInstance(WEATHER_ICON, WEATHER_ICON, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        }
        return null;
    }

    private void closeDialog(java.awt.event.ActionEvent event) {
        Component source = (Component) event.getSource();
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
