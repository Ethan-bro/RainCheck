package view;

import entity.CustomTag;

import interface_adapter.ViewManagerModel;
import interface_adapter.editCustomTag.EditTagController;
import interface_adapter.editCustomTag.EditTagViewModel;
import interface_adapter.manageTags.ManageTagsViewModel;

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
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serial;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.jetbrains.annotations.NotNull;

public class EditTagView extends JPanel implements PropertyChangeListener, ActionListener {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String VIEW_NAME = "Edit Custom Tag";

    private static final String FONT_SANS_SERIF = "SansSerif";

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

    private static final int BUTTON_BORDER_TOP_BOTTOM = 10;
    private static final int BUTTON_BORDER_LEFT_RIGHT = 25;

    private static final int VERTICAL_STRUT_SMALL = 12;
    private static final int ICON_BUTTON_BORDER_THICKNESS = 3;

    private static final Dimension PREFERRED_SIZE = new Dimension(560, 400);

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

        setPreferredSize(PREFERRED_SIZE);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(COLOR_WHITE);
        setBorder(BorderFactory.createEmptyBorder(
                PADDING_TOP_BOTTOM,
                PADDING_LEFT_RIGHT,
                PADDING_TOP_BOTTOM,
                PADDING_LEFT_RIGHT
        ));
    }

    private void setupTitle() {
        final JLabel titleLabel = new JLabel(VIEW_NAME, SwingConstants.CENTER);
        titleLabel.setFont(new Font(FONT_SANS_SERIF, Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setForeground(COLOR_DODGER_BLUE);
        add(titleLabel, BorderLayout.NORTH);
    }

    private void setupCenterPanel() {
        final JPanel centerPanel = new JPanel();
        centerPanel.setBackground(COLOR_WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(createTagNamePanel());
        centerPanel.add(Box.createVerticalStrut(PADDING_TOP_BOTTOM));
        centerPanel.add(createIconLabel());
        centerPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_SMALL));
        centerPanel.add(createIconPanel());

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createTagNamePanel() {
        final JPanel tagNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tagNamePanel.setBackground(COLOR_WHITE);

        final JLabel tagNameLabel = new JLabel("Tag Name:");
        tagNameLabel.setFont(new Font(FONT_SANS_SERIF, Font.PLAIN, LABEL_FONT_SIZE));
        tagNamePanel.add(tagNameLabel);

        tagNameTextField.setFont(new Font(FONT_SANS_SERIF, Font.PLAIN, LABEL_FONT_SIZE));
        tagNameTextField.setPreferredSize(new Dimension(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT));
        tagNamePanel.add(tagNameTextField);

        return tagNamePanel;
    }

    private JLabel createIconLabel() {
        final JLabel iconLabel = new JLabel("Select Tag Icon:");
        iconLabel.setFont(new Font(FONT_SANS_SERIF, Font.BOLD, ICON_LABEL_FONT_SIZE));
        iconLabel.setForeground(COLOR_DODGER_BLUE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return iconLabel;
    }

    private JPanel createIconPanel() {
        final int iconCount = CustomTagIcons.getIconList().size();
        final int rows = 2;
        final int cols = (iconCount + 1) / 2;

        final JPanel iconPanel = new JPanel(new GridLayout(rows, cols, 15, 15));
        iconPanel.setBackground(COLOR_WHITE);
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final Font emojiFont = findEmojiFont();

        for (final String icon : CustomTagIcons.getIconList()) {
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
            toggleIconButtonStyle(iconButton, event.getStateChange() == ItemEvent.SELECTED);
            }
        );

        return iconButton;
    }

    private void toggleIconButtonStyle(final JToggleButton iconButton, final boolean selected) {
        if (selected) {
            iconButton.setBorder(BorderFactory.createLineBorder(COLOR_DODGER_BLUE, ICON_BUTTON_BORDER_THICKNESS));
            iconButton.setOpaque(true);
            iconButton.setBackground(COLOR_LIGHT_BLUE);
        }
        else {
            iconButton.setBorder(BorderFactory.createEmptyBorder());
            iconButton.setOpaque(false);
            iconButton.setBackground(null);
        }
    }

    private Font findEmojiFont() {
        final String[] emojiFonts = {"Segoe UI Emoji", "Apple Color Emoji", "Noto Color Emoji"};
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font emojiFont = null;
        boolean found = false;

        for (final String fontName : emojiFonts) {
            if (found) {
                break;
            }
            for (final String availableFont : ge.getAvailableFontFamilyNames()) {
                if (availableFont.equalsIgnoreCase(fontName)) {
                    emojiFont = new Font(fontName, Font.PLAIN, ICON_BUTTON_FONT_SIZE);
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            emojiFont = new Font("Segoe UI", Font.PLAIN, ICON_BUTTON_FONT_SIZE);
        }

        return emojiFont;
    }

    private void resetForm() {
        tagNameTextField.setText("");
        iconGroup.clearSelection();
        confirmButton.setEnabled(true);
    }

    private void stylePrimaryButton(final JButton button) {
        button.setFont(new Font(FONT_SANS_SERIF, Font.BOLD, LABEL_FONT_SIZE));
        button.setBackground(COLOR_DODGER_BLUE);
        button.setForeground(COLOR_WHITE);
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
        button.setFont(new Font(FONT_SANS_SERIF, Font.PLAIN, LABEL_FONT_SIZE));
        button.setBackground(COLOR_LIGHT_GRAY);
        button.setForeground(COLOR_BLACK);
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
    public void actionPerformed(final ActionEvent event) {
        final Object source = event.getSource();

        if (source == confirmButton) {
            handleConfirmAction();
        }
        else if (source == cancelButton) {
            handleCancelAction();
        }
    }

    private void handleConfirmAction() {
        final String tagName = tagNameTextField.getText().trim();
        final AbstractButton selectedButton = getSelectedButton(iconGroup);

        if (validateForm(tagName, selectedButton)) {
            final String selectedIcon = selectedButton.getActionCommand();

            final CustomTag supposedTag = new CustomTag(tagName, selectedIcon);
            final CustomTag currentTag = manageTagsViewModel.getState().getCurrTag();

            editTagController.execute(currentTag, supposedTag);

            resetForm();
        }
    }

    private boolean validateForm(final String tagName, final AbstractButton selectedButton) {
        boolean result = true;
        if (tagName.isEmpty()) {
            showValidationError("Please enter a tag name.");
            result = false;
        }
        else if (selectedButton == null) {
            showValidationError("Please select a tag icon.");
            result = false;
        }

        return result;
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
    }

    private void handleCancelAction() {
        resetForm();
        editTagViewModel.setUsername(null);
        viewManagerModel.setState(ManageTagsView.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    private AbstractButton getSelectedButton(final ButtonGroup group) {
        AbstractButton selected = null;
        for (final Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            final AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                selected = button;
                break;
            }
        }
        return selected;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        final String propertyName = event.getPropertyName();

        switch (propertyName) {
            case "Success":
                handleSuccessProperty();
                break;
            case "Failed":
                handleFailedProperty();
                break;
            case "LoadTag":
                handleLoadTagProperty();
                break;
            default:
                // no action required
                break;
        }
    }

    private void handleSuccessProperty() {
        JOptionPane.showMessageDialog(this,
                "Custom tag edited successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        resetForm();
        viewManagerModel.setState(ManageTagsView.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    private void handleFailedProperty() {
        final String errorMsg = editTagViewModel.getState().getErrorMsg();
        JOptionPane.showMessageDialog(this,
                errorMsg,
                "Error",
                JOptionPane.ERROR_MESSAGE);
        confirmButton.setEnabled(true);
    }

    private void handleLoadTagProperty() {
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
}
