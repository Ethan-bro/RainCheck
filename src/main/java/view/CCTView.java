package view;

import entity.CustomTag;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_customTag.CCTController;
import interface_adapter.create_customTag.CCTViewModel;
import interface_adapter.logged_in.LoggedInViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import static use_case.createCustomTag.CustomTagIcons.IconList;

public class CCTView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final String viewName = "Create Custom Tag";
    private final CCTViewModel createCustomTagViewModel;
    private final CCTController createCustomTagController;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;

    // UI components
    private final JTextField tagNameTextField;
    private final ButtonGroup iconGroup;
    private final JButton createTag;
    private final JButton cancelTag;

    public CCTView(CCTViewModel model,
                   CCTController createCustomTagController,
                   LoggedInViewModel loggedInViewModel,
                   ViewManagerModel viewManagerModel) {

        this.createCustomTagViewModel = model;
        this.createCustomTagViewModel.addPropertyChangeListener(this);
        this.createCustomTagController = createCustomTagController;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;

        // Build Panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tag Name Text Field
        JPanel tagNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tagNameTextField = new JTextField(20);
        JLabel tagNameLabel = new JLabel("Tag Name: ");
        tagNamePanel.add(tagNameLabel);
        tagNamePanel.add(tagNameTextField);
        add(tagNamePanel, BorderLayout.NORTH);

        // Icon Selection
        JPanel iconSelectionPanel = new JPanel();
        iconSelectionPanel.setLayout(new BoxLayout(iconSelectionPanel, BoxLayout.Y_AXIS));
        JLabel tagIconLabel = new JLabel("Select Tag Icon: ");
        JPanel iconPanel = new JPanel();
        iconGroup = new ButtonGroup();
        for (String icon : IconList) {
            JToggleButton iconButton = new JToggleButton(icon);
            iconButton.setActionCommand(icon);
            iconGroup.add(iconButton);
            iconButton.setMargin(new Insets(0, 0, 0, 0));
            iconButton.setBorder(BorderFactory.createEmptyBorder());
            iconButton.setFocusPainted(false);
            iconButton.setContentAreaFilled(false);
            iconButton.setOpaque(false);
            iconButton.addItemListener(e -> {
                if (iconButton.isSelected()) {
                    iconButton.setBorder(
                            BorderFactory.createLineBorder(new Color(0, 0, 0, 100), 2)
                    );
                } else {
                    iconButton.setBorder(BorderFactory.createEmptyBorder());
                }
            });
            iconPanel.add(iconButton);
        }
        iconSelectionPanel.add(tagIconLabel);
        iconSelectionPanel.add(iconPanel);
        add(iconSelectionPanel, BorderLayout.CENTER);

        // Submit and Cancel Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        createTag = new JButton("Create Tag");
        cancelTag = new JButton("Cancel");
        createTag.addActionListener(this);
        cancelTag.addActionListener(this);
        bottomPanel.add(createTag);
        bottomPanel.add(cancelTag);
        add(bottomPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(450, 300));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createTag) {
            String supposedName = tagNameTextField.getText().trim();
            if (supposedName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a tag name.");
                return;
            }
            ButtonModel selectedTag = iconGroup.getSelection();
            if (selectedTag == null) {
                JOptionPane.showMessageDialog(this, "Please select a tag.");
                return;
            }
            String supposedIcon = selectedTag.getActionCommand();
            String username = loggedInViewModel.getState().getUsername();
            CustomTag supposedTag = new CustomTag(supposedName, supposedIcon);
            createCustomTagController.execute(supposedTag, username);

        } else if (e.getSource() == cancelTag) {
            viewManagerModel.setState(ManageTagsView.getViewName());
            viewManagerModel.firePropertyChanged();
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if ("Success".equals(prop)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Custom tag created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            resetForm();
            // close the window holding this panel
            Window win = SwingUtilities.getWindowAncestor(this);
            viewManagerModel.setState(ManageTagsView.getViewName());
            viewManagerModel.firePropertyChanged();

        } else if ("Failed".equals(prop)) {
            String errorMsg = createCustomTagViewModel.getState().getErrorMsg();
            JOptionPane.showMessageDialog(
                    this,
                    errorMsg,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            resetForm();
        }
    }
    private void resetForm() {
        createTag.setEnabled(true);
        tagNameTextField.setText("");
        iconGroup.clearSelection();
    }

    public static String getViewName() {
        return viewName;
    }
}
