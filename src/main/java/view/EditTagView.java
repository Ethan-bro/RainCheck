package view;

import entity.CustomTag;
import interface_adapter.ViewManagerModel;
import interface_adapter.editTag.EditTagController;
import interface_adapter.editTag.EditTagViewModel;
import interface_adapter.manageTags.ManageTagsViewModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JTextField;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;

import use_case.createCustomTag.customTagIcons;

public class EditTagView extends JPanel implements PropertyChangeListener, ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String VIEW_NAME = "Edit Custom Tag";

    private static final int PADDING_TOP_BOTTOM = 20;
    private static final int PADDING_LEFT_RIGHT = 40;
    private static final int TEXTFIELD_COLUMNS = 20;
    private static final int TEXTFIELD_WIDTH = 220;
    private static final int TEXTFIELD_HEIGHT = 28;
    private static final int ICON_BUTTON_SIZE = 50;
    private static final int ICON_BUTTON_FONT_SIZE = 28;
    private static final int TITLE_FONT_SIZE = 24;
    private static final int LABEL_FONT_SIZE = 16;
    private static final int ICON_LABEL_FONT_SIZE = 18;

    private static final Color COLOR_DODGER_BLUE = new Color(0x1E90FF);
    private static final Color COLOR_LIGHT_BLUE = new Color(0xD0E7FF);
    private static final Color COLOR_LIGHT_GRAY = new Color(230, 230, 230);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_BLACK = Color.BLACK;

    private final ViewManagerModel viewManagerModel;
    private final EditTagViewModel editTagViewModel;
    private final ManageTagsViewModel manageTagsViewModel;
    private final EditTagController editTagController;

    private CustomTag oldTag;

    private final JTextField tagNameTextField = new JTextField(TEXTFIELD_COLUMNS);
    private final ButtonGroup iconGroup = new ButtonGroup();
    private final JButton confirmButton = new JButton("Confirm Edit");
    private final JButton cancelButton = new JButton("Cancel");

    public EditTagView(final ViewManagerModel viewManagerModel,
                       final ManageTagsViewModel manageTagsViewModel,
                       final EditTagViewModel model,
                       final EditTagController controller) {

        this.viewManagerModel = viewManagerModel;
        this.manageTagsViewModel = manageTagsViewModel;
        this.editTagViewModel = model;
        this.editTagViewModel.addPropertyChangeListener(this);
        this.editTagController = controller;

        this.oldTag = manageTagsViewModel.getState().getCurrTag();

        setupLayout();
        setupTitle();
        setupCenterPanel();
        setupBottomPanel();

        registerListeners();

        setPreferredSize(new Dimension(560, 400));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(COLOR_WHITE);
        setBorder(BorderFactory.createEmptyBorder(PADDING_TOP_BOTTOM, PADDING_LEFT_RIGHT,
                PADDING_TOP_BOTTOM, PADDING_LEFT_RIGHT));
    }

    private void setupTitle() {
        final JLabel titleLabel = new JLabel(VIEW_NAME, JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(COLOR_DODGER_BLUE);
        add(titleLabel, BorderLayout.NORTH);
    }

    private void setupCenterPanel() {
        final JPanel centerPanel = new JPanel();
        centerPanel.setBackground(COLOR_WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(createTagNamePanel());
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(createIconLabel());
        centerPanel.add(Box.createVerticalStrut(12));
        centerPanel.add(createIconPanel());

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createTagNamePanel() {
        final JPanel tagNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tagNamePanel.setBackground(COLOR_WHITE);

        final JLabel tagNameLabel = new JLabel("Tag Name:");
        tagNameLabel.setFont(new Font("SansSerif", Font.PLAIN, LABEL_FONT_SIZE));
        tagNamePanel.add(tagNameLabel);

        tagNameTextField.setFont(new Font("SansSerif", Font.PLAIN, LABEL_FONT_SIZE));
        tagNameTextField.setPreferredSize(new Dimension(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT));
        tagNamePanel.add(tagNameTextField);

        return tagNamePanel;
    }

    private JLabel createIconLabel() {
        final JLabel iconLabel = new JLabel("Select Tag Icon:");
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, ICON_LABEL_FONT_SIZE));
        iconLabel.setForeground(COLOR_DODGER_BLUE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return iconLabel;
    }

    private JPanel createIconPanel() {
        final int iconCount = customTagIcons.IconList.size();
        final int rows = 2;
        final int cols = (iconCount + 1) / 2;

        final JPanel iconPanel = new JPanel(new GridLayout(rows, cols, 15, 15));
        iconPanel.setBackground(COLOR_WHITE);
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final Font emojiFont = findEmojiFont();

        for (final String icon : customTagIcons.IconList) {
            final JToggleButton iconButton = createToggleButton(icon, emojiFont);
            iconGroup.add(iconButton);
            iconPanel.add(iconButton);
        }

        return iconPanel;
    }

    private void setupBottomPanel() {
        final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(COLOR_WHITE);

        stylePrimaryButton(confirmButton);
        styleSecondaryButton(cancelButton);

        bottomPanel.add(cancelButton);
        bottomPanel.add(confirmButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void registerListeners() {
        confirmButton.addActionListener(this);
        cancelButton.addActionListener(this);

        cancelButton.addActionListener(event -> {
            resetForm();
            editTagViewModel.setUsername(null);
            viewManagerModel.setState(ManageTagsView.getViewName());
            viewManagerModel.firePropertyChanged();
        });
    }

    @NotNull
    private JToggleButton createToggleButton(final String icon, final Font emojiFont) {
        final JToggleButton iconButton = new JToggleButton(icon);
        iconButton.setActionCommand(icon);
        iconButton.setFont(emojiFont);
        iconButton.setFocusPainted(false);
        iconButton.setPreferredSize(new Dimension(ICON_BUTTON_SIZE, ICON_BUTTON_SIZE));
        iconButton.setBorder(BorderFactory.createEmptyBorder());
        iconButton.setContentAreaFilled(false);
        iconButton.setOpaque(false);

        iconButton.addItemListener(event -> {
            if (iconButton.isSelected()) {
                iconButton.setBorder(BorderFactory.createLineBorder(COLOR_DODGER_BLUE, 3));
                iconButton.setOpaque(true);
                iconButton.setBackground(COLOR_LIGHT_BLUE);
            } else {
                iconButton.setBorder(BorderFactory.createEmptyBorder());
                iconButton.setOpaque(false);
                iconButton.setBackground(null);
            }
        });

        return iconButton;
    }

    private Font findEmojiFont() {
        final String[] emojiFonts = {"Segoe UI Emoji", "Apple Color Emoji", "Noto Color Emoji"};
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        for (final String fontName : emojiFonts) {
            for (final String availableFont : ge.getAvailableFontFamilyNames()) {
                if (availableFont.equalsIgnoreCase(fontName)) {
                    return new Font(fontName, Font.PLAIN, ICON_BUTTON_FONT_SIZE);
                }
            }
        }
        return new Font("Segoe UI", Font.PLAIN, ICON_BUTTON_FONT_SIZE);
    }

    private void resetForm() {
        tagNameTextField.setText("");
        iconGroup.clearSelection();
        confirmButton.setEnabled(true);
    }

    private void stylePrimaryButton(final JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, LABEL_FONT_SIZE));
        button.setBackground(COLOR_DODGER_BLUE);
        button.setForeground(COLOR_WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(final JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, LABEL_FONT_SIZE));
        button.setBackground(COLOR_LIGHT_GRAY);
        button.setForeground(COLOR_BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {

        if (event.getSource() == confirmButton) {
            final String tagName = tagNameTextField.getText().trim();
            final AbstractButton selectedButton = getSelectedButton(iconGroup);
            if (tagName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a tag name.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedButton == null) {
                JOptionPane.showMessageDialog(this,
                        "Please select a tag icon.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            final String selectedIcon = selectedButton.getActionCommand();

            final CustomTag supposedTag = new CustomTag(tagName, selectedIcon);
            final CustomTag currentTag = manageTagsViewModel.getState().getCurrTag();

            editTagController.execute(currentTag, supposedTag);

            resetForm();
            return;
        }

        if (event.getSource() == cancelButton) {
            resetForm();
            editTagViewModel.setUsername(null);
            viewManagerModel.setState(ManageTagsView.getViewName());
            viewManagerModel.firePropertyChanged();
        }
    }

    private AbstractButton getSelectedButton(final ButtonGroup group) {
        for (final Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            final AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button;
            }
        }
        return null;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        final String propertyName = event.getPropertyName();

        switch (propertyName) {
            case "Success" -> {
                JOptionPane.showMessageDialog(this,
                        "Custom tag edited successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                resetForm();
                viewManagerModel.setState(ManageTagsView.getViewName());
                viewManagerModel.firePropertyChanged();
            }
            case "Failed" -> {
                final String errorMsg = editTagViewModel.getState().getErrorMsg();
                JOptionPane.showMessageDialog(this,
                        errorMsg,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                confirmButton.setEnabled(true);
            }
            case "LoadTag" -> {
                oldTag = manageTagsViewModel.getState().getCurrTag();
                if (oldTag != null) {
                    final String oldName = oldTag.getTagName();
                    if (oldName != null) {
                        tagNameTextField.setText(oldName);
                    }
                    final String oldIcon = oldTag.getTagIcon();
                    if (oldIcon != null) {
                        for (final Enumeration<AbstractButton> buttons = iconGroup.getElements(); buttons.hasMoreElements();) {
                            final AbstractButton btn = buttons.nextElement();
                            if (oldIcon.equals(btn.getActionCommand())) {
                                btn.setSelected(true);
                                break;
                            }
                        }
                    }
                }
            }
            default -> {
                // no action required for other properties
            }
        }
    }
}
