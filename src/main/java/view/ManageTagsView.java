package view;

import entity.Priority;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.ManageTags.ManageTagsViewModel;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import view.CCTView;

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
    private final String mainViewKey;
    private final String cctViewKey;

    // UI components
    private final JComboBox<Object> customTagCombo;
    private final JButton editTagButton;
    private final JButton deleteTagButton;
    private final JButton createTagButton;
    private final JButton doneButton;

    public ManageTagsView(
            LoggedInViewModel loggedInVM,
            ViewManagerModel viewManagerModel,
            ManageTagsViewModel manageTagsVM,
            CustomTagDataAccessInterface tagDao
    ) {
        this.loggedInVM = loggedInVM;
        this.viewManagerModel = viewManagerModel;
        this.manageTagsVM = manageTagsVM;
        this.tagDao = tagDao;
        this.mainViewKey = loggedInVM.getViewName();
        this.cctViewKey = CCTView.getViewName();

        manageTagsVM.addPropertyChangeListener(this);

        // panel layout
        setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tag selection row
        List<Object> tagOptions = manageTagsVM.getTagOptions();
        customTagCombo = new JComboBox<>(tagOptions.isEmpty() ? new Object[]{} : tagOptions.toArray());
        if (tagOptions.isEmpty()) {
            customTagCombo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                                                              int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value == null) {
                        setText("No tags available - create one first");
                        setForeground(Color.GRAY);
                    }
                    return this;
                }
            });
        }
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.add(new JLabel("Select Tag:"));
        selectionPanel.add(customTagCombo);
        centerPanel.add(selectionPanel);
        centerPanel.add(Box.createVerticalStrut(15));

        // Edit & Delete row
        editTagButton = new JButton("Edit");
        deleteTagButton = new JButton("Delete");
        editTagButton.addActionListener(this);
        deleteTagButton.addActionListener(this);
        JPanel editDeletePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        editDeletePanel.add(editTagButton);
        editDeletePanel.add(deleteTagButton);
        centerPanel.add(editDeletePanel);
        centerPanel.add(Box.createVerticalStrut(15));

        // Create Custom Tag button
        createTagButton = new JButton("Create Custom Tag");
        createTagButton.addActionListener(this);
        JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createPanel.add(createTagButton);
        centerPanel.add(createPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Done button
        doneButton = new JButton("Done");
        doneButton.addActionListener(this);
        JPanel donePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        donePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        donePanel.add(doneButton);
        add(donePanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(450, 300));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == editTagButton) {
            // implement edit
        } else if (e.getSource() == deleteTagButton) {
            // implement delete
        } else if (e.getSource() == createTagButton) {
            viewManagerModel.setState(cctViewKey);
            viewManagerModel.firePropertyChanged();
        } else if (e.getSource() == doneButton) {
            viewManagerModel.setState(mainViewKey);
            viewManagerModel.firePropertyChanged();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // handle updates if needed
    }

    public static String getViewName() {
        return viewName;
    }
}
