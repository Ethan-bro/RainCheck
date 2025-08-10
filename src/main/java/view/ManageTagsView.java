package view;

import entity.CustomTag;
import interface_adapter.EditTag.EditTagViewModel;
import interface_adapter.ManageTags.ManageTagsState;
import interface_adapter.ViewManagerModel;
import interface_adapter.CreateTag.CreateTagViewModel;
import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.DeleteTag.DeleteTagController;
import interface_adapter.events.TagChangeEventNotifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ManageTagsView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final String viewName = "Manage Tags";

    private final ViewManagerModel viewManagerModel;
    private final ManageTagsViewModel manageTagsVM;
    private final CreateTagViewModel cctViewModel;
    private final EditTagViewModel editTagViewModel;
    private final DeleteTagController deleteTagController;

    private final JComboBox<CustomTag> customTagCombo;
    private final JButton editTagButton;
    private final JButton deleteTagButton;
    private final JButton createTagButton;
    private final JButton doneButton;

    private final PropertyChangeListener tagListener = this::onTagUpdate;

    public ManageTagsView(
            ViewManagerModel viewManagerModel,
            ManageTagsViewModel manageTagsVM,
            CreateTagViewModel cctViewModel,
            EditTagViewModel editTagViewModel,
            DeleteTagController deleteTagController
    ) {
        this.viewManagerModel = viewManagerModel;
        this.manageTagsVM = manageTagsVM;
        this.manageTagsVM.addPropertyChangeListener(this);
        this.cctViewModel = cctViewModel;
        this.editTagViewModel = editTagViewModel;
        this.deleteTagController = deleteTagController;

        // Subscribe to global tag change events
        TagChangeEventNotifier.addListener(tagListener);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Manage Tags");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // --- Center Panel ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // --- Combo Box ---
        List<CustomTag> tagOptions = manageTagsVM.getTagOptions();
        customTagCombo = new JComboBox<>(tagOptions.toArray(new CustomTag[0]));
        customTagCombo.setPreferredSize(new Dimension(250, 30));
        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 14);
        customTagCombo.setFont(emojiFont);
        customTagCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    label.setText("Create a tag!");
                    label.setForeground(Color.GRAY);
                } else {
                    label.setText(value.toString());  // this uses your CustomTag.toString()
                }
                return label;
            }
        });

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectionPanel.setBackground(Color.WHITE);
        selectionPanel.add(new JLabel("Select Tag:"));
        selectionPanel.add(customTagCombo);
        centerPanel.add(selectionPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // --- Edit/Delete Buttons ---
        editTagButton = new JButton("Edit");
        deleteTagButton = new JButton("Delete");
        styleSecondaryButton(editTagButton);
        styleSecondaryButton(deleteTagButton);

        JPanel editDeletePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        editDeletePanel.setBackground(Color.WHITE);
        editDeletePanel.add(editTagButton);
        editDeletePanel.add(deleteTagButton);
        centerPanel.add(editDeletePanel);
        centerPanel.add(Box.createVerticalStrut(30));

        // --- Create New Tag ---
        createTagButton = new JButton("Create Custom Tag");
        JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createPanel.setBackground(Color.WHITE);
        createPanel.add(createTagButton);
        centerPanel.add(createPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        stylePrimaryButton(createTagButton);

        add(centerPanel, BorderLayout.CENTER);

        // --- Done Button ---
        doneButton = new JButton("Done");
        styleSecondaryButton(doneButton);
        JPanel donePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        donePanel.setBackground(Color.WHITE);
        donePanel.add(doneButton);
        add(donePanel, BorderLayout.SOUTH);

        // Listeners
        editTagButton.addActionListener(this);
        deleteTagButton.addActionListener(this);
        createTagButton.addActionListener(this);
        doneButton.addActionListener(this);

        setPreferredSize(new Dimension(500, 360));
    }

    private void onTagUpdate(PropertyChangeEvent evt) {
        if ("tagsUpdated".equals(evt.getPropertyName())) {
            refreshTags();
        }
    }

    private void stylePrimaryButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(0x1E90FF));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
    }

    private void styleSecondaryButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBackground(new Color(230, 230, 230));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createTagButton) {
            cctViewModel.setUsername(manageTagsVM.getUsername());
            viewManagerModel.setState(CreateTagView.getViewName());
            viewManagerModel.firePropertyChanged();
            return;
        }

        if (e.getSource() == doneButton) {
            viewManagerModel.setState(LoggedInView.getViewName());
            viewManagerModel.firePropertyChanged();
            return;
        }

        // get the selected tag:
        CustomTag selectedTag = (CustomTag) customTagCombo.getSelectedItem();
        if (selectedTag == null) {
            JOptionPane.showMessageDialog(this, "Please select a tag first.");
            return;
        }

        if (e.getSource() == editTagButton) {

            //.set the current tag to be edited
            ManageTagsState state = manageTagsVM.getState();
            state.setCurrTag(selectedTag);

            // let EditTagView know a tag has been selected to edit
            editTagViewModel.loadTag(selectedTag);

            // switch to edit tag view
            editTagViewModel.setUsername(manageTagsVM.getUsername());
            viewManagerModel.setState(EditTagView.getViewName());
            viewManagerModel.firePropertyChanged();

        } else if (e.getSource() == deleteTagButton) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete the tag \"" + selectedTag.getTagName() + "\"?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteTagController.execute(selectedTag);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String event = evt.getPropertyName();
        if ("tagDeleted".equals(event)) {
            JOptionPane.showMessageDialog(this,
                    "Tag deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            deleteTagButton.setEnabled(true);
        }
    }

    private void refreshTags() {
        List<CustomTag> newTags = manageTagsVM.getTagOptions();
        CustomTag selected = (CustomTag) customTagCombo.getSelectedItem();

        customTagCombo.removeAllItems();
        for (CustomTag tag : newTags) {
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

    public static String getViewName() {
        return viewName;
    }

    public void dispose() {
        TagChangeEventNotifier.removeListener(tagListener);
    }
}
