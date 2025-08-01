package view;

import view.components.StyledButton;
import view.components.TaskHeaderPanel;

import javax.swing.*;
import java.awt.*;

public class TaskContextPopup extends JDialog {

    public TaskContextPopup(JFrame parent, Point location) {
        super(parent, true);
        setupBasicPopup(location);
        createTestContent();
    }

    private void setupBasicPopup(Point location) {
        setUndecorated(true);
        setSize(300, 400);
        setLocation(location);
        getContentPane().setBackground(new Color(100, 100, 255, 150));
    }

    private void createTestContent() {
        setLayout(new BorderLayout());

        // Test header with dummy data
        TaskHeaderPanel header = new TaskHeaderPanel(
                "Sample Task", "📚", "high"
        );

        // Test button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(new StyledButton("Edit", "✏️"));
        buttonPanel.add(new StyledButton("Delete", "🗑️"));

        add(header, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Test method - you can call this from your main view
    public static void showTestPopup(JFrame parent, Point location) {
        TaskContextPopup popup = new TaskContextPopup(parent, location);
        popup.setVisible(true);
    }
}
