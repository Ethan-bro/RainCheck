package view.components;

import javax.swing.*;
import java.awt.*;

public class TaskHeaderPanel extends JPanel {
    // For now, use placeholder data until Task entity is available
    private String taskTitle;
    private String tagEmoji;
    private String priority;

    public TaskHeaderPanel(String title, String emoji, String priorityLevel) {
        this.taskTitle = title;
        this.tagEmoji = emoji;
        this.priority = priorityLevel;

        setupLayout();
        setOpaque(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left: Tag emoji
        JLabel tagLabel = new JLabel(tagEmoji);
        tagLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        // Center: Task title
        JLabel titleLabel = new JLabel(taskTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Right: Priority indicator
        JLabel priorityLabel = new JLabel("●");
        priorityLabel.setFont(new Font("Arial", Font.BOLD, 20));
        priorityLabel.setForeground(getPriorityColor(priority));

        add(tagLabel, BorderLayout.WEST);
        add(titleLabel, BorderLayout.CENTER);
        add(priorityLabel, BorderLayout.EAST);
    }

    private Color getPriorityColor(String priority) {
        switch (priority.toLowerCase()) {
            case "high": return new Color(255, 82, 82);
            case "medium": return new Color(255, 193, 7);
            case "low": return new Color(40, 167, 69);
            default: return Color.GRAY;
        }
    }
}