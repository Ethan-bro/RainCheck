package view;

import entity.Priority;
import entity.TaskInfo;

import interface_adapter.ViewManagerModel;
import interface_adapter.deleteTask.DeleteTaskController;
import interface_adapter.editTask.EditTaskController;
import interface_adapter.markTaskComplete.MarkTaskCompleteController;
import interface_adapter.task.TaskViewModel;

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
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TaskBox extends JPanel implements PropertyChangeListener {

    private static final String FONT_NAME = "Segoe UI";

    private static final int PANEL_GAP = 5;
    private static final int BORDER_LINE_RGB = 200;
    private static final int BORDER_PADDING = 15;
    private static final int PREFERRED_WIDTH = 260;
    private static final int PREFERRED_HEIGHT = 160;

    private static final int ICON_SIZE = 30;
    private static final int BUTTON_ICON_SIZE = 20;

    private static final int FONT_SIZE_SMALL = 12;
    private static final int FONT_SIZE_NORMAL = 13;
    private static final int FONT_SIZE_TITLE = 16;

    private static final int BUTTON_PADDING_VERTICAL = 5;
    private static final int BUTTON_PADDING_HORIZONTAL = 10;

    private static final Color COMPLETE_BG = new Color(76, 175, 80);
    private static final Color EDIT_BG = new Color(33, 150, 243);
    private static final Color DELETE_BG = new Color(244, 67, 54);

    private static final Color BG_COMPLETE = new Color(240, 240, 240);
    private static final Color BG_HIGH_PRIORITY = new Color(255, 235, 238);
    private static final Color BG_MEDIUM_PRIORITY = new Color(255, 243, 224);
    private static final Color BG_LOW_PRIORITY = new Color(255, 253, 231);

    private static final int UV_LOW_THRESHOLD = 2;
    private static final int UV_MEDIUM_THRESHOLD = 5;

    private static final Color UV_LOW_COLOR = new Color(56, 142, 60);
    private static final Color UV_MEDIUM_COLOR = new Color(255, 193, 7);
    private static final Color UV_HIGH_COLOR = new Color(211, 47, 47);

    private JLabel tagNameLabel;
    private JLabel tagEmojiLabel;
    private JLabel titleLabel;
    private JLabel weatherDescriptionLabel;
    private JLabel weatherEmojiLabel;
    private JLabel uvIndexLabel;
    private final TaskViewModel taskViewModel;

    public TaskBox(final TaskViewModel taskViewModel,
                   final MarkTaskCompleteController markTaskCompleteController,
                   final DeleteTaskController deleteTaskController,
                   final EditTaskController editTaskController,
                   final ViewManagerModel viewManagerModel) {
        this.taskViewModel = taskViewModel;
        taskViewModel.addPropertyChangeListener(this);

        setupPanel();
        addWeatherPanel();
        addCenterPanel();
        addButtonPanel(markTaskCompleteController, deleteTaskController, editTaskController, viewManagerModel);

        updateContents();
    }

    private void setupPanel() {
        setLayout(new BorderLayout(0, PANEL_GAP));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(BORDER_LINE_RGB, BORDER_LINE_RGB, BORDER_LINE_RGB)),
                BorderFactory.createEmptyBorder(BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING)
        ));
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        setOpaque(true);
        updateDisplayColour();
    }

    private void addWeatherPanel() {
        final JPanel weatherPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, PANEL_GAP, 0));
        weatherPanel.setOpaque(false);

        weatherEmojiLabel = new JLabel();
        weatherEmojiLabel.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
        weatherDescriptionLabel = new JLabel();
        weatherDescriptionLabel.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE_NORMAL));
        uvIndexLabel = new JLabel();
        uvIndexLabel.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE_NORMAL));

        weatherPanel.add(weatherEmojiLabel);
        weatherPanel.add(weatherDescriptionLabel);
        weatherPanel.add(uvIndexLabel);

        add(weatherPanel, BorderLayout.NORTH);
    }

    private void addCenterPanel() {
        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createVerticalGlue());

        final JPanel titleTagPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, PANEL_GAP, 0));
        titleTagPanel.setOpaque(false);

        tagEmojiLabel = new JLabel();
        tagNameLabel = new JLabel();
        titleLabel = new JLabel(taskViewModel.getTask().getTaskInfo().getTaskName());
        titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE_TITLE));

        titleTagPanel.add(tagEmojiLabel);
        titleTagPanel.add(tagNameLabel);
        titleTagPanel.add(titleLabel);

        centerPanel.add(titleTagPanel);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);
    }

    private void addButtonPanel(final MarkTaskCompleteController markTaskCompleteController,
                                final DeleteTaskController deleteTaskController,
                                final EditTaskController editTaskController,
                                final ViewManagerModel viewManagerModel) {
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        final JButton completeButton = createCompleteButton(markTaskCompleteController);
        final JButton editButton = createEditButton(editTaskController, viewManagerModel);
        final JButton deleteButton = createDeleteButton(deleteTaskController);

        buttonPanel.add(completeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createCompleteButton(final MarkTaskCompleteController markTaskCompleteController) {
        return createImageButton("complete.png", "Complete", COMPLETE_BG, evt -> {
            markTaskCompleteController.markAsComplete(taskViewModel.getTask().getTaskInfo().getId());
            closeDialog(evt);
        });
    }

    private JButton createEditButton(final EditTaskController editTaskController,
                                     final ViewManagerModel viewManagerModel) {
        return createImageButton("edit.png", "Edit", EDIT_BG, evt -> {
            handleEditAction(editTaskController, viewManagerModel);
            closeDialog(evt);
        });
    }

    private void handleEditAction(final EditTaskController editTaskController,
                                  final ViewManagerModel viewManagerModel) {
        editTaskController.setCurrentTask(taskViewModel.getTask());

        final EditTaskView editTaskView = (EditTaskView) viewManagerModel.getView(EditTaskView.getViewName());
        if (editTaskView != null) {
            editTaskView.setExistingTask(taskViewModel.getTask());
            System.out.println("EDIT TASK IS CALLED. Task id " + editTaskView.getExistingTask().getTaskInfo().getId());
        }

        editTaskController.switchToEditTaskView(viewManagerModel);
    }

    private JButton createDeleteButton(final DeleteTaskController deleteTaskController) {
        return createImageButton("delete.png", "Delete", DELETE_BG, evt -> {
            try {
                deleteTaskController.deleteTask(taskViewModel.getTask().getTaskInfo().getId());
                closeDialog(evt);
            }
            catch (final IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    private JButton createImageButton(final String iconName, final String tooltip,
                                      final Color bgColor,
                                      final java.awt.event.ActionListener action) {
        final JButton button = new JButton();
        button.setToolTipText(tooltip);

        final ImageIcon icon = loadIcon("/TaskBoxIcons/" + iconName);
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
                BorderFactory.createEmptyBorder(BUTTON_PADDING_VERTICAL, BUTTON_PADDING_HORIZONTAL,
                        BUTTON_PADDING_VERTICAL, BUTTON_PADDING_HORIZONTAL)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(final java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(final java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private ImageIcon loadIcon(final String path) {
        ImageIcon icon = null;

        final URL imgUrl = getClass().getResource(path);
        if (imgUrl != null) {
            final Image image = new ImageIcon(imgUrl).getImage();
            final Image scaledImage = image.getScaledInstance(BUTTON_ICON_SIZE, BUTTON_ICON_SIZE, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImage);
        }

        return icon;
    }

    private void updateDisplayColour() {
        final String status = taskViewModel.getTask().getTaskInfo().getTaskStatus();
        final Priority priority = taskViewModel.getTask().getTaskInfo().getPriority();

        if ("Complete".equals(status)) {
            setBackground(BG_COMPLETE);
        }
        else if (priority == Priority.HIGH) {
            setBackground(BG_HIGH_PRIORITY);
        }
        else if (priority == Priority.MEDIUM) {
            setBackground(BG_MEDIUM_PRIORITY);
        }
        else if (priority == Priority.LOW) {
            setBackground(BG_LOW_PRIORITY);
        }
        else {
            setBackground(Color.WHITE);
        }
    }

    private void updateContents() {
        final TaskInfo taskInfo = taskViewModel.getTask().getTaskInfo();

        updateTagLabels(taskInfo);
        updateTitleLabel(taskInfo);
        updateWeatherLabels(taskInfo);
        updateUvLabel(taskInfo);

        updateDisplayColour();

        revalidate();
        repaint();
    }

    private void updateTagLabels(final TaskInfo taskInfo) {
        if (taskInfo.getTag() != null) {
            tagNameLabel.setText(taskInfo.getTag().getTagName());
            tagEmojiLabel.setText(taskInfo.getTag().getTagIcon());
            tagNameLabel.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE_SMALL));
        }
        else {
            tagNameLabel.setText("");
            tagEmojiLabel.setText("");
        }
    }

    private void updateTitleLabel(final TaskInfo taskInfo) {
        titleLabel.setText(taskInfo.getTaskName());
    }

    private void updateWeatherLabels(final TaskInfo taskInfo) {
        weatherDescriptionLabel.setText(taskInfo.getWeatherDescription());

        final String iconName = taskInfo.getWeatherIconName();
        if (iconName != null && !iconName.isEmpty()) {
            final ImageIcon icon = loadWeatherIcon(iconName);
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
    }

    private void updateUvLabel(final TaskInfo taskInfo) {
        final String uvIndex = taskInfo.getUvIndex();
        if (uvIndex != null && !uvIndex.isEmpty()) {
            uvIndexLabel.setText("UV: " + uvIndex);
            uvIndexLabel.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE_NORMAL));

            setUvLabelColor(uvIndex);
        }
        else {
            uvIndexLabel.setText("");
        }
    }

    private void setUvLabelColor(final String uvIndex) {
        Color color;
        try {
            final int uvVal = Integer.parseInt(uvIndex);
            if (uvVal <= UV_LOW_THRESHOLD) {
                color = UV_LOW_COLOR;
            }
            else if (uvVal <= UV_MEDIUM_THRESHOLD) {
                color = UV_MEDIUM_COLOR;
            }
            else {
                color = UV_HIGH_COLOR;
            }
        }
        catch (final NumberFormatException ex) {
            color = Color.DARK_GRAY;
        }
        uvIndexLabel.setForeground(color);
    }

    private ImageIcon loadWeatherIcon(final String iconName) {
        ImageIcon icon = null;

        final String iconPath = "/weatherIcons/" + iconName + ".png";
        final URL imgUrl = getClass().getResource(iconPath);

        if (imgUrl != null) {
            final Image image = new ImageIcon(imgUrl).getImage();
            final Image scaledImage = image.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImage);
        }

        return icon;
    }

    private void closeDialog(final java.awt.event.ActionEvent event) {
        final Component source = (Component) event.getSource();
        final Window window = SwingUtilities.getWindowAncestor(source);
        if (window != null) {
            window.dispose();
        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        updateContents();
    }
}
