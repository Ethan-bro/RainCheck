package view;

import javax.swing.*;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import entity.Priority;
import entity.Task;
import entity.TaskInfo;
import interface_adapter.ViewManagerModel;
import interface_adapter.deleteTask.DeleteTaskController;
import interface_adapter.markTaskComplete.MarkTaskCompleteController;
import interface_adapter.editTask.EditTaskController;

//*
/* The View for a single task box.
*/
public class TaskBox extends JPanel implements PropertyChangeListener {
    private final JLabel tagNameLabel;
    private final JLabel tagEmojiLabel;
    private final JLabel titleLabel;
    private final JLabel tempLabel;
    private final JLabel weatherDescriptionLabel;
    private final JLabel weatherEmojiLabel;
    private final MarkTaskCompleteController markTaskCompleteController;
    private final DeleteTaskController deleteTaskController;
    private final EditTaskController editTaskController;
    private final TaskInfo taskInfo;
    private final ViewManagerModel viewManagerModel;


    public TaskBox(TaskInfo taskInfo,
                   MarkTaskCompleteController markTaskCompleteController,
                   DeleteTaskController deleteTaskController,
                   EditTaskController editTaskController,
                   ViewManagerModel viewManagerModel) {
        this.taskInfo = taskInfo;
        this.editTaskController = editTaskController;
        this.markTaskCompleteController = markTaskCompleteController;
        this.deleteTaskController = deleteTaskController;
        this.viewManagerModel = viewManagerModel;
        taskInfo.addPropertyChangeListener(this);

        // Creating main panel
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setMaximumSize(new Dimension(260, Integer.MAX_VALUE));

        // Creating the topmost panel with the tag, tag emoji, edit button, and delete button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        // Creating the tag panel
        JPanel tagPanel = new JPanel();
        tagPanel.setLayout(new BoxLayout(tagPanel, BoxLayout.X_AXIS));
        tagPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        tagNameLabel = new JLabel(taskInfo.getCategory().getTagName());
        tagPanel.add(tagNameLabel);

        tagEmojiLabel = new JLabel(taskInfo.getCategory().getTagEmoji());
        tagPanel.add(tagEmojiLabel);

        // Creating the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        ImageIcon pencilIcon = new ImageIcon(Objects.requireNonNull(getClass()
                .getResource("/images/pencil.png")));
        ImageIcon scaledPencilIcon = new ImageIcon(pencilIcon.getImage()
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        JButton editButton = new JButton(scaledPencilIcon);
        editButton.setPreferredSize(new Dimension(24, 24));
        editButton.setMaximumSize(new Dimension(24, 24));
        editButton.setMargin(new Insets(0, 0, 0, 0));
        editButton.addActionListener(evt -> {
                    editTaskController.setCurrentTask(new Task(taskInfo));
                    editTaskController.switchToEditTaskView(viewManagerModel);
                });
        buttonPanel.add(editButton);

        ImageIcon trashIcon = new ImageIcon(Objects.requireNonNull(getClass()
                .getResource("/images/trash.png")));
        ImageIcon scaledTrashIcon = new ImageIcon(trashIcon.getImage()
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        JButton deleteButton = new JButton(scaledTrashIcon);
        deleteButton.setPreferredSize(new Dimension(24, 24));
        deleteButton.setMaximumSize(new Dimension(24, 24));
        deleteButton.setMargin(new Insets(0, 0, 0, 0));
        deleteButton.addActionListener(
                evt -> deleteTaskController.deleteTask(taskInfo.getId())
        );
        buttonPanel.add(deleteButton);

        ImageIcon checkmarkIcon = new ImageIcon(Objects.requireNonNull(getClass()
                .getResource("/images/checkmark.png")));
        ImageIcon scaledCheckmarkIcon = new ImageIcon(checkmarkIcon.getImage()
                .getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        JButton checkmarkButton = new JButton(scaledCheckmarkIcon);
        checkmarkButton.setPreferredSize(new Dimension(24, 24));
        checkmarkButton.setMaximumSize(new Dimension(24, 24));
        checkmarkButton.setMargin(new Insets(0, 0, 0, 0));
        checkmarkButton.addActionListener(
                evt -> {
                        markTaskCompleteController.markAsComplete(taskInfo.getId());
                    }
        );
        buttonPanel.add(checkmarkButton);

        topPanel.add(tagPanel);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(buttonPanel);

        // Creating the middle panel with title of the task left-aligned
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        middlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleLabel = new JLabel(taskInfo.getTaskName());
        middlePanel.add(titleLabel);
        middlePanel.add(Box.createHorizontalGlue());

        // Creating the bottom panel with the weather
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        // Creating the temperature panel
        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.X_AXIS));
        tempPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tempLabel = new JLabel(taskInfo.getTemp);
        tempPanel.add(tempLabel);

        // Creating the weather panel with description and emoji
        JPanel weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.X_AXIS));
        weatherPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        weatherDescriptionLabel = new JLabel(taskInfo.getWeatherDescription());
        weatherPanel.add(weatherDescriptionLabel);
        weatherEmojiLabel = new JLabel(taskInfo.getWeatherEmojiLabel());
        weatherPanel.add(weatherEmojiLabel);

        bottomPanel.add(tempPanel);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(weatherPanel);

        // Putting together all the panels
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(topPanel);
        this.add(middlePanel);
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(bottomPanel);

        updateDisplayColour();

        tagPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        topPanel.setOpaque(false);
        middlePanel.setOpaque(false);
        tempPanel.setOpaque(false);
        weatherPanel.setOpaque(false);
        bottomPanel.setOpaque(false);
        this.setOpaque(true);
        this.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    /**
     * Updates the colour of the task box (main panel) based on priority and completion.
     */
    private void updateDisplayColour() {
        if (Objects.equals(taskInfo.getTaskStatus(), "Complete")) {
        this.setBackground(Color.LIGHT_GRAY);
        } else {
            if (taskInfo.getPriority().equals(Priority.HIGH)) {
                this.setBackground(Color.RED);
            }
            if (taskInfo.getPriority().equals(Priority.MEDIUM)) {
                this.setBackground(Color.ORANGE);
            }
            if (taskInfo.getPriority().equals(Priority.LOW)) {
                this.setBackground(Color.YELLOW);
            }
        }
    }

    /**
     * @param evt Updating task box to updated task based on changes in
     *            TaskViewModel.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (taskInfo.getCategory() != null) {
            tagNameLabel.setText(taskInfo.getCategory().getTagName());
            tagEmojiLabel.setText(taskInfo.getCategory().getTagEmoji());
        }
        titleLabel.setText(taskInfo.getTaskName());
        tempLabel.setText(taskInfo.getTemperature());

        weatherDescriptionLabel.setText(taskInfo.getWeatherDescription());
        weatherEmojiLabel.setText(taskInfo.getWeatherEmojiLabel());

        updateDisplayColour();

        revalidate();
        repaint();
    }

    public DeleteTaskController getDeleteTaskController() {
        return deleteTaskController;
    }

    public MarkTaskCompleteController getMarkTaskCompleteController() {
        return markTaskCompleteController;
    }

    public EditTaskController getEditTaskController() {
        return editTaskController;
    }

    public ViewManagerModel getViewManagerModel() {
        return viewManagerModel;
    }
}
