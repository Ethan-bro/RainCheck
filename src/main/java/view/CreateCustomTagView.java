package view;

import entity.CustomTag;

import interface_adapter.ViewManagerModel;
import interface_adapter.createTag.CreateCustomTagController;
import interface_adapter.createTag.CreateCustomTagViewModel;

import use_case.createCustomTag.CustomTagIcons;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.jetbrains.annotations.NotNull;

public class CreateCustomTagView extends JPanel implements PropertyChangeListener {

    private static final String VIEW_NAME = "Create Custom Tag";

    private static final String FONT_SANS_SERIF = "SansSerif";

    private static final int FONT_SIZE_TITLE = 24;
    private static final int FONT_SIZE_LABEL = 16;
    private static final int FONT_SIZE_ICON_LABEL = 18;
    private static final int FONT_SIZE_ICON = 28;
    private static final int FONT_SIZE_BUTTON_PRIMARY = 16;
    private static final int FONT_SIZE_BUTTON_SECONDARY = 16;

    private static final Color COLOR_DODGER_BLUE = new Color(0x1E90FF);
    private static final Color COLOR_DODGER_BLUE_LIGHT = new Color(0xD0E7FF);
    private static final Color COLOR_SECONDARY_BG = new Color(230, 230, 230);

    private static final int PADDING_TOP_BOTTOM = 20;
    private static final int PADDING_LEFT_RIGHT = 40;

    private static final int BUTTON_BORDER_TOP_BOTTOM = 10;
    private static final int BUTTON_BORDER_LEFT_RIGHT = 25;

    private static final int ICON_BUTTON_SIZE = 50;
    private static final int TAG_NAME_FIELD_WIDTH = 220;
    private static final int TAG_NAME_FIELD_HEIGHT = 28;

    private static final int VERTICAL_STRUT_TAG_NAME_TO_ICON_LABEL = 20;
    private static final int VERTICAL_STRUT_ICON_LABEL_TO_ICONS = 12;

    private static final int ICON_BUTTON_BORDER_THICKNESS = 3;

    private static final Dimension PREFERRED_SIZE = new Dimension(560, 400);

    private final ViewManagerModel viewManagerModel;
    private final CreateCustomTagViewModel createCustomTagViewModel;
    private final CreateCustomTagController createCustomTagController;

    private final JTextField tagNameTextField = new JTextField(20);
    private final ButtonGroup iconGroup = new ButtonGroup();
    private final JButton createButton = new JButton("Create Tag");
    private final JButton cancelButton = new JButton("Cancel");

    public CreateCustomTagView(
            final ViewManagerModel viewManagerModel,
            final CreateCustomTagViewModel model,
            final CreateCustomTagController controller
    ) {
        this.viewManagerModel = viewManagerModel;
        this.createCustomTagViewModel = model;
        this.createCustomTagController = controller;

        this.createCustomTagViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(
                PADDING_TOP_BOTTOM,
                PADDING_LEFT_RIGHT,
                PADDING_TOP_BOTTOM,
                PADDING_LEFT_RIGHT
        ));

        initTitlePanel();
        initCenterPanel();
        initBottomPanel();
        attachListeners();

        setPreferredSize(PREFERRED_SIZE);
    }

    private void initTitlePanel() {
        final JLabel titleLabel = new JLabel("Create Custom Tag", SwingConstants.CENTER);
        titleLabel.setFont(new Font(FONT_SANS_SERIF, Font.BOLD, FONT_SIZE_TITLE));
        titleLabel.setForeground(COLOR_DODGER_BLUE);
        add(titleLabel, BorderLayout.NORTH);
    }

    private void initCenterPanel() {
        final JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(createTagNamePanel());
        centerPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_TAG_NAME_TO_ICON_LABEL));
        centerPanel.add(createIconLabel());
        centerPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_ICON_LABEL_TO_ICONS));
        centerPanel.add(createIconButtonsPanel());

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createTagNamePanel() {
        final JPanel tagNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tagNamePanel.setBackground(Color.WHITE);

        final JLabel tagNameLabel = new JLabel("Tag Name:");
        tagNameLabel.setFont(new Font(FONT_SANS_SERIF, Font.PLAIN, FONT_SIZE_LABEL));
        tagNamePanel.add(tagNameLabel);

        tagNameTextField.setFont(new Font(FONT_SANS_SERIF, Font.PLAIN, FONT_SIZE_LABEL));
        tagNameTextField.setPreferredSize(new Dimension(TAG_NAME_FIELD_WIDTH, TAG_NAME_FIELD_HEIGHT));
        tagNamePanel.add(tagNameTextField);

        return tagNamePanel;
    }

    private JLabel createIconLabel() {
        final JLabel iconLabel = new JLabel("Select Tag Icon:");
        iconLabel.setFont(new Font(FONT_SANS_SERIF, Font.BOLD, FONT_SIZE_ICON_LABEL));
        iconLabel.setForeground(COLOR_DODGER_BLUE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return iconLabel;
    }

    private JPanel createIconButtonsPanel() {
        final int iconCount = CustomTagIcons.getIconList().size();
        final int cols = (iconCount + 1) / 2;

        final JPanel iconPanel = new JPanel(new GridLayout(2, cols, 15, 15));
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final Font emojiFont = findEmojiFont();

        for (final String icon : CustomTagIcons.getIconList()) {
            final JToggleButton iconButton = createToggleButton(icon, emojiFont);
            iconGroup.add(iconButton);
            iconPanel.add(iconButton);
        }

        return iconPanel;
    }

    @NotNull
    private static JToggleButton createToggleButton(final String icon, final Font emojiFont) {
        final JToggleButton iconButton = new JToggleButton(icon);
        iconButton.setActionCommand(icon);
        iconButton.setFont(emojiFont);
        iconButton.setFocusPainted(false);
        iconButton.setPreferredSize(new Dimension(ICON_BUTTON_SIZE, ICON_BUTTON_SIZE));
        iconButton.setBorder(BorderFactory.createEmptyBorder());
        iconButton.setContentAreaFilled(false);
        iconButton.setOpaque(false);

        iconButton.addItemListener(event -> handleIconSelection(iconButton, event));

        return iconButton;
    }

    private static void handleIconSelection(final JToggleButton iconButton, final ItemEvent event) {
        if (iconButton.isSelected()) {
            iconButton.setBorder(BorderFactory.createLineBorder(COLOR_DODGER_BLUE, ICON_BUTTON_BORDER_THICKNESS));
            iconButton.setOpaque(true);
            iconButton.setBackground(COLOR_DODGER_BLUE_LIGHT);
        }
        else {
            iconButton.setBorder(BorderFactory.createEmptyBorder());
            iconButton.setOpaque(false);
            iconButton.setBackground(null);
        }
    }

    private void initBottomPanel() {
        final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(Color.WHITE);

        stylePrimaryButton(createButton);
        styleSecondaryButton(cancelButton);

        bottomPanel.add(cancelButton);
        bottomPanel.add(createButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void attachListeners() {
        createButton.addActionListener(this::onCreateButtonClicked);
        cancelButton.addActionListener(event -> onCancelButtonClicked());
    }

    private void onCreateButtonClicked(final ActionEvent actionEvent) {
        if (validateForm()) {
            final ButtonModel selectedIcon = iconGroup.getSelection();
            final String icon = selectedIcon.getActionCommand();

            final String tagName = tagNameTextField.getText().trim();
            final CustomTag tag = new CustomTag(tagName, icon);

            createCustomTagController.execute(tag, createCustomTagViewModel.getUsername());

            createButton.setEnabled(false);
        }
    }

    private boolean validateForm() {
        final String tagName = tagNameTextField.getText().trim();
        boolean isValid = true;

        if (tagName.isEmpty()) {
            showValidationMessage("Please enter a tag name.");
            isValid = false;
        }
        else if (iconGroup.getSelection() == null) {
            showValidationMessage("Please select a tag icon.");
            isValid = false;
        }

        return isValid;
    }

    private void showValidationMessage(final String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void onCancelButtonClicked() {
        resetForm();
        createCustomTagViewModel.setUsername(null);
        viewManagerModel.setState(ManageTagsView.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    private void resetForm() {
        tagNameTextField.setText("");
        iconGroup.clearSelection();
        createButton.setEnabled(true);
    }

    private void stylePrimaryButton(final JButton button) {
        button.setFont(new Font(FONT_SANS_SERIF, Font.BOLD, FONT_SIZE_BUTTON_PRIMARY));
        button.setBackground(COLOR_DODGER_BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(
                BUTTON_BORDER_TOP_BOTTOM,
                BUTTON_BORDER_LEFT_RIGHT,
                BUTTON_BORDER_TOP_BOTTOM,
                BUTTON_BORDER_LEFT_RIGHT
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(final JButton button) {
        button.setFont(new Font(FONT_SANS_SERIF, Font.PLAIN, FONT_SIZE_BUTTON_SECONDARY));
        button.setBackground(COLOR_SECONDARY_BG);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(
                BUTTON_BORDER_TOP_BOTTOM,
                BUTTON_BORDER_LEFT_RIGHT,
                BUTTON_BORDER_TOP_BOTTOM,
                BUTTON_BORDER_LEFT_RIGHT
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final String propertyName = evt.getPropertyName();

        switch (propertyName) {
            case "Success" -> handleSuccess();
            case "Failed" -> handleFailure();
            default -> {
                // do nothing
            }
        }
    }

    private void handleSuccess() {
        JOptionPane.showMessageDialog(
                this,
                "Custom tag created successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        resetForm();

        viewManagerModel.setState(LoggedInView.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    private void handleFailure() {
        final String errorMsg = createCustomTagViewModel.getState().getErrorMsg();
        JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        createButton.setEnabled(true);
    }

    private Font findEmojiFont() {
        final String[] emojiFonts = {"Segoe UI Emoji", "Apple Color Emoji", "Noto Color Emoji"};
        final GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font foundFont = null;

        for (final String fontName : emojiFonts) {
            for (final String availableFont : graphicsEnv.getAvailableFontFamilyNames()) {
                if (availableFont.equalsIgnoreCase(fontName)) {
                    foundFont = new Font(fontName, Font.PLAIN, FONT_SIZE_ICON);
                    break;
                }
            }
            if (foundFont != null) {
                break;
            }
        }

        if (foundFont == null) {
            foundFont = new Font("Segoe UI", Font.PLAIN, FONT_SIZE_ICON);
        }
        return foundFont;
    }
}
