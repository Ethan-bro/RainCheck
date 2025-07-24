package view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * The View for a single task box.
 */
public class TaskBox extends JPanel implements PropertyChangeListener {
    private final String viewName = "task card";
    private final TaskViewModel taskViewModel;
    private final TaskController taskController;
    private final JLabel tagNameLabel;
    private final JLabel tagEmojiLabel;
    private final JLabel titleLabel;
    private final JLabel tempLabel;
    private final JLabel weatherDescriptionLabel;
    private final JLabel weatherEmojiLabel;


    public TaskBox(TaskViewModel taskViewModel, TaskController taskController) {
        this.taskViewModel = taskViewModel;
        taskViewModel.addPropertyChangeListener(this);
        this.taskController = taskController;

        // Creating main panel
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));

        // Creating the topmost panel with the tag, tag emoji, edit button, and delete button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        // Creating the tag panel
        JPanel tagPanel = new JPanel();
        tagPanel.setLayout(new BoxLayout(tagPanel, BoxLayout.X_AXIS));
        tagPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        tagNameLabel = new JLabel(taskViewModel.getTag().getTagName());
        tagPanel.add(tagNameLabel);

        tagEmojiLabel = new JLabel(taskViewModel.getTag().getTagEmoji());
        tagPanel.add(tagEmojiLabel);

        // Creating the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        ImageIcon pencilIcon = new ImageIcon(Objects.requireNonNull(getClass()
                .getResource("/images/pencil.png")));
        JButton editButton = new JButton(pencilIcon);
        editButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        taskController.switchToEditTaskView();
                    }
                }
        );
        buttonPanel.add(editButton);

        ImageIcon checkmarkIcon = new ImageIcon(Objects.requireNonNull(getClass()
                .getResource("/images/checkmark.png")));
        JButton checkmarkButton = new JButton(checkmarkIcon);
        checkmarkButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        taskController.markAsComplete(taskViewModel.getTaskId());
                    }
                }
        );
        buttonPanel.add(checkmarkButton);

        ImageIcon trashIcon = new ImageIcon(Objects.requireNonNull(getClass()
                .getResource("/images/trash.png")));
        JButton deleteButton = new JButton(trashIcon);
        deleteButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        taskController.deleteTask(taskViewModel.getTaskId());
                    }
                }
        );
        buttonPanel.add(deleteButton);

        topPanel.add(tagPanel);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(buttonPanel);

        // Creating the middle panel with title of the task left-aligned
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        middlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleLabel = new JLabel(taskViewModel.getTitle());
        middlePanel.add(titleLabel);
        middlePanel.add(Box.createHorizontalGlue());

        // Creating the bottom panel with the weather
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        // Creating the temperature panel
        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.X_AXIS));
        tempPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tempLabel = new JLabel(taskViewModel.getTemperature());
        tempPanel.add(tempLabel);

        // Creating the weather panel with description and emoji
        JPanel weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.X_AXIS));
        weatherPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        weatherDescriptionLabel = new JLabel(taskViewModel.getWeatherDescription());
        weatherPanel.add(weatherDescriptionLabel);
        weatherEmojiLabel = new JLabel(taskViewModel.getWeatherEmojiLabel());
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
     * Updates the colour of the task card (main panel) based on priority and completion.
     */
    private void updateDisplayColour() {
        if (taskViewModel.isCompleted()) {
        this.setBackground(Color.LIGHT_GRAY);
        } else {
        this.setBackground(taskViewModel.getPriorityColor());
        }
    }

    /**
     * @param evt Updating task card to updated task based on changes in
     *            TaskViewModel.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (taskViewModel.getTag() != null) {
            tagNameLabel.setText(taskViewModel.getTag().getTagName());
            tagEmojiLabel.setText(taskViewModel.getTag().getTagEmoji());
        }
        titleLabel.setText(taskViewModel.getTitle());
        tempLabel.setText(taskViewModel.getTemperature());

        weatherDescriptionLabel.setText(taskViewModel.getWeatherDescription());
        weatherEmojiLabel.setText(taskViewModel.getWeatherEmojiLabel());

        updateDisplayColour();

        revalidate();
        repaint();
    }
}


