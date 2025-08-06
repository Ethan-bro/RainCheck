package view;

import entity.CustomTag;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.ManageTags.ManageTagsViewModel;
import interface_adapter.ManageTags.EditTagController;
import interface_adapter.ManageTags.DeleteTagController;
import use_case.createCustomTag.CustomTagDataAccessInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ManageTagsView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final String viewName = "Manage Tags";

    private final LoggedInViewModel loggedInVM;
    private final ViewManagerModel viewManagerModel;
    private final ManageTagsViewModel manageTagsVM;
    private final CustomTagDataAccessInterface tagDao;
    private final EditTagController editTagController;
    private final DeleteTagController deleteTagController;

    private final String mainViewKey;
    private final String cctViewKey;

    private final JComboBox<CustomTag> customTagCombo;
    private final JButton editTagButton;
    private final JButton deleteTagButton;
    private final JButton createTagButton;
    private final JButton doneButton;

    public ManageTagsView(
            LoggedInViewModel loggedInVM,
            ViewManagerModel viewManagerModel,
            ManageTagsViewModel manageTagsVM,
            CustomTagDataAccessInterface tagDao,
            EditTagController editTagController,
            DeleteTagController deleteTagController
    ) {
        this.loggedInVM = loggedInVM;
        this.viewManagerModel = viewManagerModel;
        this.manageTagsVM = manageTagsVM;
        this.tagDao = tagDao;
        this.editTagController = editTagController;
        this.deleteTagController = deleteTagController;

        this.mainViewKey = loggedInVM.getViewName();
        this.cctViewKey = view.CCTView.getViewName();

        this.manageTagsVM.addPropertyChangeListener(this);

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
        customTagCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        customTagCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof CustomTag tag) {
                    setText(tag.getTagName() + " " + tag.getTagEmoji());
                }
                return this;
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
        stylePrimaryButton(createTagButton);
        JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createPanel.setBackground(Color.WHITE);
        createPanel.add(createTagButton);
        centerPanel.add(createPanel);
        centerPanel.add(Box.createVerticalStrut(20));

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

    private void stylePrimaryButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(0x1E90FF)); // DodgerBlue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleSecondaryButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBackground(new Color(230, 230, 230));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CustomTag selectedTag = (CustomTag) customTagCombo.getSelectedItem();
        if (selectedTag == null) {
            JOptionPane.showMessageDialog(this, "Please select a tag first.");
            return;
        }

        if (e.getSource() == editTagButton) {
            String newName = JOptionPane.showInputDialog(this, "Enter new name for tag:", selectedTag.getTagName());
            if (newName != null && !newName.trim().isEmpty()) {
                editTagController.execute(selectedTag.getTagName(), newName.trim(), selectedTag.getTagEmoji());
                refreshTags();
            }
        } else if (e.getSource() == deleteTagButton) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete the tag \"" + selectedTag.getTagName() + "\"?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteTagController.execute(selectedTag.getTagName());
                refreshTags();
            }
        } else if (e.getSource() == createTagButton) {
            viewManagerModel.setState(cctViewKey);
            viewManagerModel.firePropertyChanged();
        } else if (e.getSource() == doneButton) {
            viewManagerModel.setState(mainViewKey);
            viewManagerModel.firePropertyChanged();
        }
    }

    private void refreshTags() {
        manageTagsVM.refreshTags();
        customTagCombo.removeAllItems();
        for (CustomTag tag : manageTagsVM.getTagOptions()) {
            customTagCombo.addItem(tag);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("refreshTagOptions".equals(evt.getPropertyName())) {
            refreshTags();
        }
    }

    public static String getViewName() {
        return viewName;
    }
}
