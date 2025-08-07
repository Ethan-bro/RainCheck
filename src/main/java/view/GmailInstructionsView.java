package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.net.URI;

public class GmailInstructionsView extends JPanel {

    private static final String viewName = "gmail instructions";
    private String previousViewName;

    public GmailInstructionsView(ViewManagerModel viewManagerModel) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 20, 40));
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Gmail Setup for RainCheck Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // HTML Instructions
        JEditorPane instructionsPane = new JEditorPane();
        instructionsPane.setContentType("text/html");
        instructionsPane.setText("""
            <html>
            <body style='font-family: Segoe UI, sans-serif; font-size: 13px; color: #333;'>
                <h2 style='color: #1a73e8;'>Step 1: Generate Gmail App Password</h2>
                <p>Since Gmail doesn't allow less secure apps anymore, follow these steps:</p>
                <ol>
                    <li>Visit <a href='https://myaccount.google.com/'>Google Account Settings</a></li>
                    <li>Click <b>Security</b> in the left menu</li>
                    <li>Enable <b>2-Step Verification</b></li>
                    <li>Click <b>App passwords</b></li>
                    <li>Select <b>Mail</b> and label it <b>RainCheck</b></li>
                    <li>Copy the 16-character password (remove all spaces)</li>
                    <li>Paste it into your <code>config/secrets.json</code> file</li>
                </ol>

                <h2 style='color: #1a73e8;'>Step 2: Sample JSON Format</h2>
                <pre style='background:#f1f1f1; padding:12px; border-radius:6px; font-size: 12px;'>
{
  "database_url": "https://your-db.supabase.co",
  "database_anon_key": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Impiam94aWF1bGpyaWRwbW51bnVoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTIyMDE5MDQsImV4cCI6MjA2Nzc3NzkwNH0.rLCXZN4wuDANPaIy3kU0uxKrhm_Ne3yb2KlLP7lMfBw",
  "weather_api_key": "your_weather_api_key",
  "email_username": "your.email@gmail.com",
  "email_password": "your-16-character-app-password"
}
                </pre>

                <h2 style='color: #1a73e8;'>Step 3: Troubleshooting</h2>
                <ul>
                    <li>Double check 2-Step Verification is enabled</li>
                    <li>Use the <b>App Password</b>, not your normal one</li>
                    <li>Check terminal for any SMTP errors</li>
                    <li>For testing: try <a href='https://myaccount.google.com/lesssecureapps'>less secure apps</a></li>
                </ul>
            </body>
            </html>
        """);

        instructionsPane.setEditable(false);
        instructionsPane.setOpaque(false);
        instructionsPane.setBorder(null);
        instructionsPane.setCaretPosition(0);
        instructionsPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(new URI(e.getURL().toString()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(instructionsPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(700, 420));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        add(scrollPane, BorderLayout.CENTER);

        // Go Back Button
        JButton goBack = new JButton("← Go Back");
        goBack.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        goBack.setForeground(Color.WHITE);
        goBack.setBackground(new Color(66, 133, 244)); // Material Blue
        goBack.setFocusPainted(false);
        goBack.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        goBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        goBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                goBack.setBackground(new Color(52, 103, 202));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                goBack.setBackground(new Color(66, 133, 244));
            }
        });

        goBack.addActionListener(e -> {
            viewManagerModel.setState(previousViewName);
            viewManagerModel.firePropertyChanged();
        });

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(goBack);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setPreviousViewName(String previousViewName) {
        this.previousViewName = previousViewName;
    }

    public static String getViewName() {
        return viewName;
    }
}
