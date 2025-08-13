package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;

import interface_adapter.ViewManagerModel;

/**
 * View displaying Gmail setup instructions with a go back button.
 */
public class GmailInstructionsView extends JPanel {

    private static final String VIEW_NAME = "GMAIL_INSTRUCTIONS";

    private static final int BORDER_TOP = 20;
    private static final int BORDER_LEFT = 40;
    private static final int BORDER_BOTTOM = 20;
    private static final int BORDER_RIGHT = 40;

    private static final int TITLE_FONT_SIZE = 20;
    private static final int TITLE_TOP_BOTTOM_BORDER = 10;
    private static final int TITLE_BOTTOM_BORDER = 20;

    private static final int SCROLL_WIDTH = 700;
    private static final int SCROLL_HEIGHT = 420;

    private static final int SCROLL_INCREMENT = 14;

    private static final int BUTTON_FONT_SIZE = 13;
    private static final Color BUTTON_BG_COLOR = new Color(66, 133, 244);
    private static final Color BUTTON_BG_HOVER_COLOR = new Color(52, 103, 202);
    private static final int BUTTON_PADDING_VERTICAL = 10;
    private static final int BUTTON_PADDING_HORIZONTAL = 20;

    private static final int BOTTOM_PANEL_TOP_BORDER = 10;

    private String previousViewName;

    /**
     * Constructs the GmailInstructionsView and initializes the UI components.
     * @param viewManagerModel the model managing view state transitions
     */
    public GmailInstructionsView(ViewManagerModel viewManagerModel) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(BORDER_TOP, BORDER_LEFT, BORDER_BOTTOM, BORDER_RIGHT));
        setBackground(Color.WHITE);

        addTitleLabel();
        addInstructionsPane();
        addBottomPanel(viewManagerModel);
    }

    /**
     * Adds the title label to the view.
     */
    private void addTitleLabel() {
        final JLabel titleLabel = new JLabel("Gmail Setup for RainCheck Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(TITLE_TOP_BOTTOM_BORDER, 0, TITLE_BOTTOM_BORDER, 0));
        add(titleLabel, BorderLayout.NORTH);
    }

    /**
     * Adds the instructions pane containing Gmail setup steps and troubleshooting.
     */
    private void addInstructionsPane() {
        final JEditorPane instructionsPane = new JEditorPane();
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
                  "database_url": "https://jbjoxiauljridpmnunuh.supabase.co",
                  "database_anon_key": "eyJhbGciOi...7lMfBw",
                  "weather_api_key": "your_weather_api_key",
                  "email_username": "your_email@gmail.com",
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
                </html>"""
        );
        instructionsPane.setEditable(false);
        instructionsPane.setOpaque(false);
        instructionsPane.setBorder(null);
        instructionsPane.setCaretPosition(0);
        instructionsPane.addHyperlinkListener(event -> {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(new URI(event.getURL().toString()));
                }
                catch (URISyntaxException | java.io.IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        final JScrollPane scrollPane = new JScrollPane(instructionsPane);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(SCROLL_WIDTH, SCROLL_HEIGHT));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Adds the bottom panel with a go back button, enabling navigation to the previous view.
     * @param viewManagerModel the model managing view state transitions
     */
    private void addBottomPanel(ViewManagerModel viewManagerModel) {
        final JButton goBack = new JButton("← Go Back");
        goBack.setFont(new Font("Segoe UI", Font.PLAIN, BUTTON_FONT_SIZE));
        goBack.setForeground(Color.WHITE);
        goBack.setBackground(BUTTON_BG_COLOR);
        goBack.setFocusPainted(false);
        goBack.setBorder(new EmptyBorder(
                BUTTON_PADDING_VERTICAL, BUTTON_PADDING_HORIZONTAL,
                BUTTON_PADDING_VERTICAL, BUTTON_PADDING_HORIZONTAL));
        goBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        goBack.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                goBack.setBackground(BUTTON_BG_HOVER_COLOR);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                goBack.setBackground(BUTTON_BG_COLOR);
            }
        });

        goBack.addActionListener(actionEvent -> {
            viewManagerModel.setState(previousViewName);
            viewManagerModel.firePropertyChanged();
        });

        final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(goBack);
        bottomPanel.setBorder(new EmptyBorder(BOTTOM_PANEL_TOP_BORDER, 0, 0, 0));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets the name of the previous view for navigation purposes.
     * @param previousViewName the name of the previous view
     */
    public void setPreviousViewName(String previousViewName) {
        this.previousViewName = previousViewName;
    }

    /**
     * Returns the unique view name identifier for this view.
     * @return the view name string
     */
    public static String getViewName() {
        return VIEW_NAME;
    }

}
