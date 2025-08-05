package view;

import entity.CustomTag;
import interface_adapter.create_customTag.CCTController;
import interface_adapter.create_customTag.CCTViewModel;
import org.jetbrains.annotations.NotNull;

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

    private String username = null;

    public CCTView(CCTViewModel model, CCTController controller) {
        this.createCustomTagViewModel = model;
        this.createCustomTagController = controller;
        this.createCustomTagViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // --- Tag Name Field ---
        JPanel tagNamePanel = new JPanel();
        JTextField tagNameTextField = new JTextField(20);
        JLabel tagNameLabel = new JLabel("Tag Name: ");
        tagNamePanel.add(tagNameLabel);
        tagNamePanel.add(tagNameTextField);

        // --- Icon Selection ---
        JPanel iconSelectionPanel = new JPanel();
        iconSelectionPanel.setLayout(new BoxLayout(iconSelectionPanel, BoxLayout.Y_AXIS));
        JLabel iconLabel = new JLabel("Select Tag Icon:");
        JPanel iconPanel = new JPanel();

        ButtonGroup iconGroup = new ButtonGroup();

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
                    iconButton.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 100), 2));
                } else {
                    iconButton.setBorder(BorderFactory.createEmptyBorder());
                }
            });

            iconPanel.add(iconButton);
        }

        iconSelectionPanel.add(iconLabel);
        iconSelectionPanel.add(iconPanel);

        // --- Bottom Buttons ---
        JPanel bottomPanel = getJPanel(tagNameTextField, iconGroup);

        // --- Assemble ---
        add(tagNamePanel, BorderLayout.NORTH);
        add(iconSelectionPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    @NotNull
    private JPanel getJPanel(JTextField tagNameTextField, ButtonGroup iconGroup) {
        JPanel bottomPanel = new JPanel();
        JButton createButton = new JButton("Create Tag");
        JButton cancelButton = new JButton("Cancel");

        createButton.addActionListener(e -> {
            String tagName = tagNameTextField.getText().trim();
            if (tagName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a tag name.");
                return;
            }

            ButtonModel selectedIcon = iconGroup.getSelection();
            if (selectedIcon == null) {
                JOptionPane.showMessageDialog(this, "Please select a tag icon.");
                return;
            }

            String icon = selectedIcon.getActionCommand();
            CustomTag tag = new CustomTag(tagName, icon);
            createCustomTagController.execute(tag, username);

            createButton.setEnabled(false);
        });

        cancelButton.addActionListener(e -> {
            tagNameTextField.setText("");
            iconGroup.clearSelection();
            createCustomTagViewModel.setUsername(null);  // signal to close
        });

        bottomPanel.add(createButton);
        bottomPanel.add(cancelButton);
        return bottomPanel;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static String getViewName() {
        return viewName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // No action commands yet
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "Success" -> {
                JOptionPane.showMessageDialog(this,
                        "Custom tag created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                createCustomTagViewModel.setUsername(null);
            }
            case "Failed" -> {
                String errorMsg = createCustomTagViewModel.getState().getErrorMsg();
                JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
