package view;

import entity.CustomTag;
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

    private final JFrame mainFrame;
    private final JTextField tagNameField;
    private final ButtonGroup iconGroup;
    private final JButton createTagButton;
    private final JButton cancelTagButton;

    private static final String viewName = "Create Custom Tag";
    private final CCTViewModel createCustomTagViewModel;
    private CCTController cctController;
    private final LoggedInViewModel loggedInViewModel;

    public CCTView(
            CCTViewModel model,
            CCTController createCustomTagController,
            LoggedInViewModel loggedInViewModel) {

        this.createCustomTagViewModel = model;
        this.createCustomTagViewModel.addPropertyChangeListener(this);
        this.cctController = createCustomTagController;
        this.loggedInViewModel = loggedInViewModel;

        // UI CONSTRUCTION:

        // Main Frame:
        this.mainFrame = new JFrame(viewName);
        mainFrame.setTitle(viewName);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(700, 700);

        // Tag Name Text Field:
        tagNameField = new JTextField(20);
        JPanel tagNamePanel = new JPanel();
        JLabel tagNameLabel = new JLabel("Tag Name: ");
        tagNamePanel.add(tagNameLabel);
        tagNamePanel.add(tagNameField);

        // Tag Icon Field:
        JPanel IconSelectionPanel = new JPanel();
        IconSelectionPanel.setLayout(new BoxLayout(IconSelectionPanel, BoxLayout.Y_AXIS));
        JLabel tagIconLabel = new JLabel("Select Tag Icon: ");
        JPanel IconPanel = new JPanel();

        iconGroup = new ButtonGroup();
        for (String icon : IconList) {

            JToggleButton iconButton = new JToggleButton(icon);
            iconButton.setActionCommand(icon);
            iconGroup.add(iconButton);

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

        createTagButton = new JButton("Create Tag");
        createTagButton.addActionListener(e -> {

            String supposedName = tagNameField.getText().trim(); // Inputted Tag Name
            if (supposedName.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter a tag name.");
                return;
            }

            ButtonModel selectedTag = iconGroup.getSelection();
            if (selectedTag == null) {
                JOptionPane.showMessageDialog(mainFrame, "Please select a tag.");
                return;
            }

            String supposedIcon = selectedTag.getActionCommand(); // Inputted Tag Icon
            String Username = this.loggedInViewModel.getState().getUsername(); // User


            CustomTag supposedTag = new CustomTag(supposedName, supposedIcon);
            cctController.execute(supposedTag, Username);

            createTagButton.setEnabled(false);

        });

        cancelTagButton = new JButton("Cancel");
        cancelTagButton.addActionListener(e -> mainFrame.dispose());

        bottomPanel.add(createTagButton);
        bottomPanel.add(cancelTagButton);

        // Combining all components and Running
        mainFrame.add(tagNamePanel, BorderLayout.NORTH);
        mainFrame.add(IconSelectionPanel, BorderLayout.CENTER);
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void show() {

        // reset fields
        tagNameField.setText("");
        iconGroup.clearSelection();
        createTagButton.setEnabled(true);

        // show frame
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
            // Show success message dialog first
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Custom tag created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // make frame disappear until use case is executed again
            mainFrame.setVisible(false);
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

    public static String getViewName() {
        return viewName;
    }

    public void setController(CCTController controller) {
        this.cctController = controller;
    }
}
