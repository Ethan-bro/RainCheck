package view;

import entity.CustomTag;
import interface_adapter.create_customTag.createCustomTagController;
import interface_adapter.create_customTag.createCustomTagViewModel;
import interface_adapter.logged_in.LoggedInViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import static use_case.createCustomTag.TagIcons.IconList;

public class createCustomTagView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final String viewName = "Create Custom Tag";
    private final createCustomTagViewModel createCustomTagViewModel;
    private final createCustomTagController createCustomTagController;
    private LoggedInViewModel loggedInViewModel = null;

    public createCustomTagView(createCustomTagViewModel model, createCustomTagController
            createCustomTagController, LoggedInViewModel loggedInViewModel) {
        this.createCustomTagViewModel = model;
        this.createCustomTagViewModel.addPropertyChangeListener(this);
        this.createCustomTagController = createCustomTagController;
        this.loggedInViewModel = loggedInViewModel;

        // UI CONSTRUCTION:

        // Main Frame:
        final JFrame mainFrame = new JFrame(viewName);
        mainFrame.setTitle(viewName);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(700, 700);
        mainFrame.setVisible(true);

        // Tag Name Text Field:
        JPanel tagNamePanel = new JPanel();
        JTextField tagNameTextField = new JTextField(20);
        JLabel tagNameLabel = new JLabel("Tag Name: ");
        tagNamePanel.add(tagNameLabel);
        tagNamePanel.add(tagNameTextField);

        // Tag Icon Field:
        JPanel IconSelectionPanel = new JPanel();
        IconSelectionPanel.setLayout(new BoxLayout(IconSelectionPanel, BoxLayout.Y_AXIS));
        JLabel tagIconLabel = new JLabel("Select Tag Icon: ");
        JPanel IconPanel = new JPanel();

        ButtonGroup IconGroup = new ButtonGroup();
        for (String icon : IconList) {

            JToggleButton iconButton = new JToggleButton(icon);
            iconButton.setActionCommand(icon);
            IconGroup.add(iconButton);

            // Make border transparent
            iconButton.setMargin(new Insets(0, 0, 0, 0));
            iconButton.setBorder(BorderFactory.createEmptyBorder());
            iconButton.setFocusPainted(false);
            iconButton.setContentAreaFilled(false);
            iconButton.setOpaque(false);

            // When emoji is selected, create a translucent black square around it to show that it was selected.
            iconButton.addItemListener(e -> {
                if (iconButton.isSelected()) {
                    // a 2px semi-transparent black border
                    iconButton.setBorder(
                            BorderFactory.createLineBorder(new Color(0, 0, 0, 100), 2)



                    );
                } else {
                    iconButton.setBorder(BorderFactory.createEmptyBorder());
                }
            });
            IconPanel.add(iconButton);
        }

        IconSelectionPanel.add(tagIconLabel);
        IconSelectionPanel.add(IconPanel);


        // Submit and Cancel Buttons:
        JPanel bottomPanel = new JPanel();

        JButton createTag = new JButton("Create Tag");
        createTag.addActionListener(e -> {


            String supposedName = tagNameTextField.getText().trim(); // Inputted Tag Name
            if (supposedName.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter a tag name.");
                return;
            }

            ButtonModel selectedTag = IconGroup.getSelection();
            if (selectedTag == null) {
                JOptionPane.showMessageDialog(mainFrame, "Please select a tag.");
                return;
            }

            String supposedIcon = selectedTag.getActionCommand(); // Inputted Tag Icon
            String Username = loggedInViewModel.getState().getUsername(); // User


            CustomTag supposedTag = new CustomTag(supposedName, supposedIcon);
            createCustomTagController.execute(supposedTag, Username);

            createTag.setEnabled(false);

        });

        JButton cancelTag = new JButton("Cancel");
        cancelTag.addActionListener(e -> mainFrame.dispose());

        bottomPanel.add(createTag);
        bottomPanel.add(cancelTag);

        // Combining all components and Running
        mainFrame.add(tagNamePanel, BorderLayout.NORTH);
        mainFrame.add(IconSelectionPanel, BorderLayout.CENTER);
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);

        mainFrame.pack();

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // method is empty
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if ("Success".equals(prop)) {
            // close the window
            SwingUtilities.getWindowAncestor(this).dispose();
        }
        else if ("Failed".equals(prop)) {
            // model tells us why it failed
            String errorMsg = createCustomTagViewModel.getState().getErrorMsg();
            JOptionPane.showMessageDialog(
                    this,
                    errorMsg,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
