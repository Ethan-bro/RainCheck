package view;

import interface_adapter.create_customTag.createCustomTagViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class createCustomTagView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final String viewName = "Create Custom Tag";
    private final createCustomTagViewModel createCustomTagViewModel;

    public createCustomTagView(createCustomTagViewModel model) {
        this.createCustomTagViewModel = model;
        this.createCustomTagViewModel.addPropertyChangeListener(this);

        // UI Construction:
        JPanel createCustomTagPanel = new JPanel();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(viewName));
        JLabel title = new JLabel(viewName);

        // Custom Tag Name Text Field
        JLabel customTagName = new JLabel("Custom Tag Name");
        JTextField customTagNameField = new JTextField(15);
        customTagNameField.setEditable(true);
        customTagNameField.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
