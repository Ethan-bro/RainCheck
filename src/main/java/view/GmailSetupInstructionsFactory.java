package view;

import interface_adapter.ViewManagerModel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JButton;

/**
 * Factory for creating the Gmail Setup Instructions button.
 */
public final class GmailSetupInstructionsFactory {

    private static final int BUTTON_FONT_SIZE = 13;
    private static final Color BUTTON_FONT_COLOR = new Color(30, 144, 255);
    private static final String BUTTON_TEXT = "Gmail Setup Instructions";

    private GmailSetupInstructionsFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a button that opens the Gmail setup instructions view.
     *
     * @param viewManagerModel The view manager model.
     * @param currentViewName The name of the current view.
     * @return The configured JButton.
     */
    public static JButton createButton(final ViewManagerModel viewManagerModel,
                                       final String currentViewName) {
        final JButton button = new JButton(BUTTON_TEXT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, BUTTON_FONT_SIZE));
        button.setForeground(BUTTON_FONT_COLOR);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addActionListener(event -> {
            final GmailInstructionsView instructionsView =
                    (GmailInstructionsView) viewManagerModel.getView(GmailInstructionsView.getViewName());

            instructionsView.setPreviousViewName(currentViewName);

            viewManagerModel.setState(GmailInstructionsView.getViewName());
            viewManagerModel.firePropertyChanged();
        });

        return button;
    }
}
