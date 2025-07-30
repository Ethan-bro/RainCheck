// TODO: Uncomment all these lines ONLY after both TaskViewModel and TaskController have been created
//TODO:   If they are not done, then uncommenting the following lines of code will cause the program to not compile
// TODO:   Also delete these TODOs

//package view;
//
//import javax.swing.*;
//
//import java.awt.*;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//import java.util.Objects;
//
///**
// * The View for a single task box.
// */
//public class TaskBox extends JPanel implements PropertyChangeListener {
//    private final TaskViewModel taskViewModel;
//    private final TaskController taskController;
//    private final JLabel tagNameLabel;
//    private final JLabel tagEmojiLabel;
//    private final JLabel titleLabel;
//    private final JLabel tempLabel;
//    private final JLabel weatherDescriptionLabel;
//    private final JLabel weatherEmojiLabel;
//
//
//    public TaskBox(TaskViewModel taskViewModel, TaskController taskController) {
//        this.taskViewModel = taskViewModel;
//        taskViewModel.addPropertyChangeListener(this);
//        this.taskController = taskController;
//
//        // Creating main panel
//        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        this.setMaximumSize(new Dimension(260, Integer.MAX_VALUE));
//
//        // Creating the topmost panel with the tag, tag emoji, edit button, and delete button
//        JPanel topPanel = new JPanel();
//        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
//
//        // Creating the tag panel
//        JPanel tagPanel = new JPanel();
//        tagPanel.setLayout(new BoxLayout(tagPanel, BoxLayout.X_AXIS));
//        tagPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//        tagNameLabel = new JLabel(taskViewModel.getTag().getTagName());
//        tagPanel.add(tagNameLabel);
//
//        tagEmojiLabel = new JLabel(taskViewModel.getTag().getTagEmoji());
//        tagPanel.add(tagEmojiLabel);
//
//        // Creating the button panel
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
//        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
//
//        ImageIcon pencilIcon = new ImageIcon(Objects.requireNonNull(getClass()
//                .getResource("/images/pencil.png")));
//        ImageIcon scaledPencilIcon = new ImageIcon(pencilIcon.getImage()
//                .getScaledInstance(20, 20, Image.SCALE_SMOOTH));
//        JButton editButton = new JButton(scaledPencilIcon);
//        editButton.setPreferredSize(new Dimension(24, 24));
//        editButton.setMaximumSize(new Dimension(24, 24));
//        editButton.setMargin(new Insets(0, 0, 0, 0));
//        editButton.addActionListener(
//                evt -> taskController.switchToEditTaskView()
//        );
//        buttonPanel.add(editButton);
//
//        ImageIcon trashIcon = new ImageIcon(Objects.requireNonNull(getClass()
//                .getResource("/images/trash.png")));
//        ImageIcon scaledTrashIcon = new ImageIcon(trashIcon.getImage()
//                .getScaledInstance(20, 20, Image.SCALE_SMOOTH));
//        JButton deleteButton = new JButton(scaledTrashIcon);
//        deleteButton.setPreferredSize(new Dimension(24, 24));
//        deleteButton.setMaximumSize(new Dimension(24, 24));
//        deleteButton.setMargin(new Insets(0, 0, 0, 0));
//        deleteButton.addActionListener(
//                evt -> taskController.deleteTask(taskViewModel.getTaskId())
//        );
//        buttonPanel.add(deleteButton);
//
//        ImageIcon checkmarkIcon = new ImageIcon(Objects.requireNonNull(getClass()
//                .getResource("/images/checkmark.png")));
//        ImageIcon scaledCheckmarkIcon = new ImageIcon(checkmarkIcon.getImage()
//                .getScaledInstance(20, 20, Image.SCALE_SMOOTH));
//        JButton checkmarkButton = new JButton(scaledCheckmarkIcon);
//        checkmarkButton.setPreferredSize(new Dimension(24, 24));
//        checkmarkButton.setMaximumSize(new Dimension(24, 24));
//        checkmarkButton.setMargin(new Insets(0, 0, 0, 0));
//        checkmarkButton.addActionListener(
//                evt -> taskController.markAsComplete(taskViewModel.getTaskId())
//        );
//        buttonPanel.add(checkmarkButton);
//
//        topPanel.add(tagPanel);
//        topPanel.add(Box.createHorizontalGlue());
//        topPanel.add(buttonPanel);
//
//        // Creating the middle panel with title of the task left-aligned
//        JPanel middlePanel = new JPanel();
//        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
//        middlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//        titleLabel = new JLabel(taskViewModel.getTitle());
//        middlePanel.add(titleLabel);
//        middlePanel.add(Box.createHorizontalGlue());
//
//        // Creating the bottom panel with the weather
//        JPanel bottomPanel = new JPanel();
//        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
//
//        // Creating the temperature panel
//        JPanel tempPanel = new JPanel();
//        tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.X_AXIS));
//        tempPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        tempLabel = new JLabel(taskViewModel.getTemperature());
//        tempPanel.add(tempLabel);
//
//        // Creating the weather panel with description and emoji
//        JPanel weatherPanel = new JPanel();
//        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.X_AXIS));
//        weatherPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
//        weatherDescriptionLabel = new JLabel(taskViewModel.getWeatherDescription());
//        weatherPanel.add(weatherDescriptionLabel);
//        weatherEmojiLabel = new JLabel(taskViewModel.getWeatherEmojiLabel());
//        weatherPanel.add(weatherEmojiLabel);
//
//        bottomPanel.add(tempPanel);
//        bottomPanel.add(Box.createHorizontalGlue());
//        bottomPanel.add(weatherPanel);
//
//        // Putting together all the panels
//        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        this.add(topPanel);
//        this.add(middlePanel);
//        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        this.add(bottomPanel);
//
//        updateDisplayColour();
//
//        tagPanel.setOpaque(false);
//        buttonPanel.setOpaque(false);
//        topPanel.setOpaque(false);
//        middlePanel.setOpaque(false);
//        tempPanel.setOpaque(false);
//        weatherPanel.setOpaque(false);
//        bottomPanel.setOpaque(false);
//        this.setOpaque(true);
//        this.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
//    }
//
//
//    /**
//     * Updates the colour of the task box (main panel) based on priority and completion.
//     */
//    private void updateDisplayColour() {
//        if (taskViewModel.isCompleted()) {
//        this.setBackground(Color.LIGHT_GRAY);
//        } else {
//        this.setBackground(taskViewModel.getPriorityColor());
//        }
//    }
//
//    /**
//     * @param evt Updating task box to updated task based on changes in
//     *            TaskViewModel.
//     */
//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        if (taskViewModel.getTag() != null) {
//            tagNameLabel.setText(taskViewModel.getTag().getTagName());
//            tagEmojiLabel.setText(taskViewModel.getTag().getTagEmoji());
//        }
//        titleLabel.setText(taskViewModel.getTitle());
//        tempLabel.setText(taskViewModel.getTemperature());
//
//        weatherDescriptionLabel.setText(taskViewModel.getWeatherDescription());
//        weatherEmojiLabel.setText(taskViewModel.getWeatherEmojiLabel());
//
//        updateDisplayColour();
//
//        revalidate();
//        repaint();
//    }
//}
