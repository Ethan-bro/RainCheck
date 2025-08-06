package view;

import entity.CustomTag;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_customTag.CCTController;
import interface_adapter.create_customTag.CCTViewModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static use_case.createCustomTag.CustomTagIcons.IconList;

public class CCTView extends JPanel implements PropertyChangeListener {

    private static final String viewName = "Create Custom Tag";
    private final ViewManagerModel viewManagerModel;
    private final CCTViewModel createCustomTagViewModel;
    private final CCTController createCustomTagController;

    private final JTextField tagNameTextField = new JTextField(20);
    private final ButtonGroup iconGroup = new ButtonGroup();
    private final JButton createButton = new JButton("Create Tag");
    private final JButton cancelButton = new JButton("Cancel");

    public CCTView(ViewManagerModel viewManagerModel, CCTViewModel model, CCTController controller) {
        this.viewManagerModel = viewManagerModel;
        this.createCustomTagViewModel = model;
        this.createCustomTagController = controller;
        this.createCustomTagViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Title - centered
        JLabel title = new JLabel("Create Custom Tag", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(0x1E90FF)); // DodgerBlue
        add(title, BorderLayout.NORTH);

        // Center panel to hold everything centered vertically and horizontally
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tag name panel centered
        JPanel tagNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tagNamePanel.setBackground(Color.WHITE);
        JLabel tagNameLabel = new JLabel("Tag Name:");
        tagNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tagNamePanel.add(tagNameLabel);

        tagNameTextField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tagNameTextField.setPreferredSize(new Dimension(220, 28));
        tagNamePanel.add(tagNameTextField);

        centerPanel.add(tagNamePanel);

        // Spacer
        centerPanel.add(Box.createVerticalStrut(20));

        // Icon label centered
        JLabel iconLabel = new JLabel("Select Tag Icon:");
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        iconLabel.setForeground(new Color(0x1E90FF)); // DodgerBlue
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(iconLabel);
        centerPanel.add(Box.createVerticalStrut(12));

        // Icon buttons panel - centered grid for uniform size
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setLayout(new GridLayout(2, (IconList.size() + 1) / 2, 15, 15)); // 2 rows, balanced cols
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Font emojiFont = findEmojiFont();

        for (String icon : IconList) {
            JToggleButton iconButton = getJToggleButton(icon, emojiFont);

            iconGroup.add(iconButton);
            iconPanel.add(iconButton);
        }

        centerPanel.add(iconPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom buttons panel centered horizontally
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(Color.WHITE);

        stylePrimaryButton(createButton);
        styleSecondaryButton(cancelButton);

        bottomPanel.add(cancelButton);
        bottomPanel.add(createButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Button listeners
        createButton.addActionListener(this::onCreateClicked);
        cancelButton.addActionListener(e -> {
            resetForm();
            createCustomTagViewModel.setUsername(null);
            viewManagerModel.setState(ManageTagsView.getViewName());  // Switch back to ManageTagsView
            viewManagerModel.firePropertyChanged();
        });

        setPreferredSize(new Dimension(560, 400));
    }

    @NotNull
    private static JToggleButton getJToggleButton(String icon, Font emojiFont) {
        JToggleButton iconButton = new JToggleButton(icon);
        iconButton.setActionCommand(icon);
        iconButton.setFont(emojiFont);
        iconButton.setFocusPainted(false);
        iconButton.setPreferredSize(new Dimension(50, 50));
        iconButton.setBorder(BorderFactory.createEmptyBorder());
        iconButton.setContentAreaFilled(false);
        iconButton.setOpaque(false);

        iconButton.addItemListener(e -> {
            if (iconButton.isSelected()) {
                iconButton.setBorder(BorderFactory.createLineBorder(new Color(0x1E90FF), 3));
                iconButton.setOpaque(true);
                iconButton.setBackground(new Color(0xD0E7FF));
            } else {
                iconButton.setBorder(BorderFactory.createEmptyBorder());
                iconButton.setOpaque(false);
                iconButton.setBackground(null);
            }
        });
        return iconButton;
    }

    // Try to pick a font that supports emojis well
    private Font findEmojiFont() {
        // Try Segoe UI Emoji (Windows), Apple Color Emoji (macOS), Noto Color Emoji (Linux)
        String[] emojiFonts = {"Segoe UI Emoji", "Apple Color Emoji", "Noto Color Emoji"};
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        for (String fontName : emojiFonts) {
            Font font = new Font(fontName, Font.PLAIN, 28);
            if (ge.getAvailableFontFamilyNames() != null) {
                for (String availableFont : ge.getAvailableFontFamilyNames()) {
                    if (availableFont.equalsIgnoreCase(fontName)) {
                        return font;
                    }
                }
            }
        }
        // fallback default font with bigger size
        return new Font("Segoe UI", Font.PLAIN, 28);
    }

    private void onCreateClicked(ActionEvent e) {
        String tagName = tagNameTextField.getText().trim();
        if (tagName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a tag name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ButtonModel selectedIcon = iconGroup.getSelection();
        if (selectedIcon == null) {
            JOptionPane.showMessageDialog(this, "Please select a tag icon.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String icon = selectedIcon.getActionCommand();
        CustomTag tag = new CustomTag(tagName, icon);
        createCustomTagController.execute(tag, createCustomTagViewModel.getUsername());

        createButton.setEnabled(false);
    }

    private void resetForm() {
        tagNameTextField.setText("");
        iconGroup.clearSelection();
        createButton.setEnabled(true);
    }

    private void stylePrimaryButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(new Color(0x1E90FF)); // DodgerBlue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setBackground(new Color(230, 230, 230));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static String getViewName() {
        return viewName;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "Success" -> {
                JOptionPane.showMessageDialog(this,
                        "Custom tag created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                resetForm();

                viewManagerModel.setState(LoggedInView.getViewName());  // Switch back to main logged in view
                viewManagerModel.firePropertyChanged();
            }
            case "Failed" -> {
                String errorMsg = createCustomTagViewModel.getState().getErrorMsg();
                JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
                createButton.setEnabled(true);
            }
        }
    }
}
