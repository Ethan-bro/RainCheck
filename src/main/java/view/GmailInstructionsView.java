package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;

public class GmailInstructionsView extends JPanel {

    private static final String viewName = "gmail instructions";
    private String previousViewName;

    public GmailInstructionsView(ViewManagerModel viewManagerModel) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea instructions = new JTextArea(
                """
                    Setting up Gmail for RainCheck Notifications
                   \s
                    Creating an App Password for Gmail
                   \s
                    Since Gmail no longer supports "Less secure apps", you'll need to use an App Password:
                   \s
                    1. Go to your Google Account settings: https://myaccount.google.com/
                    2. Select "Security" from the left menu
                    3. Under "Signing in to Google," select "2-Step Verification" (you must enable this first)
                    4. At the bottom of the page, select "App passwords"
                    5. Generate a new app password for "Mail" and "Other (Custom name)" - name it "RainCheck"
                    6. Copy the 16-character password that appears
                    7. Paste this password into your config/secrets.json file for the email_password field
                   \s
                    Configuration in secrets.json
                   \s
                    {
                      "email_username": "your.email@gmail.com",
                      "email_password": "your-16-character-app-password"
                    }
                   \s
                    Troubleshooting
                   \s
                    1. Make sure 2-Step Verification is enabled on your Google account
                    2. Ensure you're using the app password, not your regular Google password
                    3. Check the console for SMTP error messages
                    4. Try enabling "Less secure app access" temporarily for testing: https://myaccount.google.com/lesssecureapps
               \s"""
        );
        instructions.setFont(new Font("Monospaced", Font.PLAIN, 12));
        instructions.setEditable(false);
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(instructions);
        add(scrollPane, BorderLayout.CENTER);

        JButton goBack = new JButton("← Go Back");
        goBack.addActionListener(e -> {
            viewManagerModel.setState(previousViewName);
            viewManagerModel.firePropertyChanged();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(goBack);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setPreviousViewName(String previousViewName) {
        this.previousViewName = previousViewName;
    }

    public static String getViewName() {
        return viewName;
    }
}
