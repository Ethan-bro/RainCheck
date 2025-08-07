package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GmailSetupInstructionsFactory {

    private GmailSetupInstructionsFactory() {}

    public static JButton createButton(ViewManagerModel viewManagerModel, String currentViewName) {
        JButton button = new JButton("Gmail Setup Instructions");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(new Color(30, 144, 255));  // hyperlink-like
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            GmailInstructionsView instructionsView = (GmailInstructionsView) viewManagerModel.getView(GmailInstructionsView.getViewName());

            instructionsView.setPreviousViewName(currentViewName);

            viewManagerModel.setState(GmailInstructionsView.getViewName());
            viewManagerModel.firePropertyChanged();
        });

        return button;
    }
}
