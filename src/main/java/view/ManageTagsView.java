package view;

import entity.CustomTag;

import interface_adapter.ViewManagerModel;
import interface_adapter.createTag.CreateCustomTagViewModel;
import interface_adapter.editTag.EditTagViewModel;
import interface_adapter.events.TagChangeEventNotifier;
import interface_adapter.manageTags.DeleteTagController;
import interface_adapter.manageTags.ManageTagsState;
import interface_adapter.manageTags.ManageTagsViewModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ManageTagsView extends JPanel implements ActionListener {

    private static final String VIEW_NAME = "Manage Tags";

    private static final String FONT_FAMILY = "SansSerif";
    private static final int TITLE_FONT_SIZE = 22;
    private static final int BUTTON_FONT_SIZE = 14;
    private static final int PANEL_BORDER_TOP = 20;
    private static final int PANEL_BORDER_LEFT = 40;
    private static final int PANEL_BORDER_BOTTOM = 20;
    private static final int PANEL_BORDER_RIGHT = 40;
    private static final int COMBOBOX_WIDTH = 250;
    private static final int COMBOBOX_HEIGHT = 30;
    private static final int VERTICAL_STRUT_SMALL = 20;
    private static final int VERTICAL_STRUT_LARGE = 30;
    private static final int FLOWLAYOUT_GAP = 15;
    private static final int BUTTON_PADDING_VERTICAL = 8;
    private static final int BUTTON_PADDING_HORIZONTAL = 18;
    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 360;
    private static final Color COLOR_BG = new Color(230, 230, 230);
    private static final Color COLOR_FG = Color.BLACK;

    private final ViewManagerModel viewManagerModel;
    private final ManageTagsViewModel manageTagsViewModel;
    private final CreateCustomTagViewModel createCustomTagViewModel;
    private final EditTagViewModel editTagViewModel;
    private final DeleteTagController deleteTagController;

    private JComboBox<CustomTag> customTagCombo;
    private JButton editTagButton;
    private JButton deleteTagButton;
    private JButton createTagButton;
    private JButton doneButton;

    private final PropertyChangeListener tagListener = this::onTagUpdate;

    public ManageTagsView(
            final ViewManagerModel viewManagerModel,
            final ManageTagsViewModel manageTagsViewModel,
            final CreateCustomTagViewModel createCustomTagViewModel,
            final EditTagViewModel editTagViewModel,
            final DeleteTagController deleteTagController
    ) {
        this.viewManagerModel = viewManagerModel;
        this.manageTagsViewModel = manageTagsViewModel;
        this.createCustomTagViewModel = createCustomTagViewModel;
        this.editTagViewModel = editTagViewModel;
        this.deleteTagController = deleteTagController;

        TagChangeEventNotifier.addListener(tagListener);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(
                PANEL_BORDER_TOP, PANEL_BORDER_LEFT, PANEL_BORDER_BOTTOM, PANEL_BORDER_RIGHT));

        add(createTitleLabel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createDonePanel(), BorderLayout.SOUTH);

        editTagButton.addActionListener(this);
        deleteTagButton.addActionListener(this);
        createTagButton.addActionListener(this);
        doneButton.addActionListener(this);

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    }

    private JLabel createTitleLabel() {
        final JLabel title = new JLabel(VIEW_NAME);
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, TITLE_FONT_SIZE));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        return title;
    }

    private JPanel createCenterPanel() {
        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(createSelectionPanel());
        centerPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_SMALL));
        centerPanel.add(createEditDeletePanel());
        centerPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_LARGE));
        centerPanel.add(createCreatePanel());
        centerPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_SMALL));

        return centerPanel;
    }

    private JPanel createSelectionPanel() {
        final JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectionPanel.setBackground(Color.WHITE);
        selectionPanel.add(new JLabel("Select Tag:"));

        final List<CustomTag> tagOptions = manageTagsViewModel.getTagOptions();
        customTagCombo = new JComboBox<>(tagOptions.toArray(new CustomTag[0]));
        customTagCombo.setPreferredSize(new Dimension(COMBOBOX_WIDTH, COMBOBOX_HEIGHT));
        customTagCombo.setFont(new Font(FONT_FAMILY, Font.PLAIN, BUTTON_FONT_SIZE));
        customTagCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    final JList<?> list,
                    final Object value,
                    final int index,
                    final boolean isSelected,
                    final boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof CustomTag tag) {
                    setText(tag.getTagName() + " " + tag.getTagIcon());
                }
                return this;
            }
        });

        selectionPanel.add(customTagCombo);
        return selectionPanel;
    }

    private JPanel createEditDeletePanel() {
        editTagButton = new JButton("Edit");
        deleteTagButton = new JButton("Delete");
        styleButton(editTagButton);
        styleButton(deleteTagButton);

        final JPanel editDeletePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, FLOWLAYOUT_GAP, 0));
        editDeletePanel.setBackground(Color.WHITE);
        editDeletePanel.add(editTagButton);
        editDeletePanel.add(deleteTagButton);

        return editDeletePanel;
    }

    private JPanel createCreatePanel() {
        createTagButton = new JButton("Create Custom Tag");
        final JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createPanel.setBackground(Color.WHITE);
        createPanel.add(createTagButton);
        return createPanel;
    }

    private JPanel createDonePanel() {
        doneButton = new JButton("Done");
        styleButton(doneButton);

        final JPanel donePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        donePanel.setBackground(Color.WHITE);
        donePanel.add(doneButton);
        return donePanel;
    }

    private void onTagUpdate(final PropertyChangeEvent evt) {
        if ("tagsUpdated".equals(evt.getPropertyName())) {
            refreshTags();
        }
    }

    private void styleButton(final JButton button) {
        button.setFont(new Font(FONT_FAMILY, Font.PLAIN, BUTTON_FONT_SIZE));
        button.setBackground(COLOR_BG);
        button.setForeground(COLOR_FG);
        button.setFocusPainted(false);
        button.setBorder(javax.swing.BorderFactory.createEmptyBorder(
                BUTTON_PADDING_VERTICAL, BUTTON_PADDING_HORIZONTAL,
                BUTTON_PADDING_VERTICAL, BUTTON_PADDING_HORIZONTAL));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        String errorMessage = null;

        if (source == createTagButton) {
            handleCreateTag();
        }
        else if (source == doneButton) {
            handleDone();
        }
        else {
            final CustomTag selectedTag = (CustomTag) customTagCombo.getSelectedItem();
            if (selectedTag == null) {
                errorMessage = "Please select a tag first.";
            }
            else if (source == editTagButton) {
                handleEditTag(selectedTag);
            }
            else if (source == deleteTagButton) {
                handleDeleteTag(selectedTag);
            }
        }

        if (errorMessage != null) {
            JOptionPane.showMessageDialog(this, errorMessage);
        }
    }

    private void handleCreateTag() {
        createCustomTagViewModel.setUsername(manageTagsViewModel.getUsername());
        viewManagerModel.setState(CreateCustomTagView.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    private void handleDone() {
        viewManagerModel.setState(LoggedInView.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    private void handleEditTag(final CustomTag selectedTag) {
        final ManageTagsState state = manageTagsViewModel.getState();
        state.setCurrTag(selectedTag);

        editTagViewModel.loadTag(selectedTag);
        editTagViewModel.setUsername(manageTagsViewModel.getUsername());

        viewManagerModel.setState(EditTagView.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    private void handleDeleteTag(final CustomTag selectedTag) {
        final int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the tag \"" + selectedTag.getTagName() + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            deleteTagController.execute(selectedTag.getTagName());
        }
    }

    private void refreshTags() {
        final List<CustomTag> newTags = manageTagsViewModel.getTagOptions();
        final CustomTag selected = (CustomTag) customTagCombo.getSelectedItem();

        customTagCombo.removeAllItems();
        for (final CustomTag tag : newTags) {
            customTagCombo.addItem(tag);
        }

        if (selected != null) {
            for (int i = 0; i < customTagCombo.getItemCount(); i++) {
                if (customTagCombo.getItemAt(i).equals(selected)) {
                    customTagCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    /**
     * Unregister listeners to avoid memory leaks.
     */
    public void dispose() {
        TagChangeEventNotifier.removeListener(tagListener);
    }

    public static String getViewName() {
        return VIEW_NAME;
    }
}
