package data_access;

import entity.*;
import use_case.notification.EmailNotificationServiceInterface;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Implementation of email notification service using JavaMail API.
 */
public class EmailNotificationService implements EmailNotificationServiceInterface {

    private final String smtpHost;
    private final String smtpPort;
    private final String senderEmail;
    private final String senderPassword;

    public EmailNotificationService(String smtpHost, String smtpPort,
                                    String senderEmail, String senderPassword) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
    }

    @Override
    public void sendTaskReminder(User user, Task task, String userEmail) {
        try {
            Message message = createEmailMessage(userEmail, task);
            Transport.send(message);
            System.out.println("Reminder email sent successfully to: " + userEmail);
        } catch (MessagingException e) {
            System.err.println("Failed to send email reminder: " + e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }

    @Override
    public ScheduledNotification scheduleEmailReminder(Task task, String userEmail, Reminder reminder) {
        // This method creates the schedule - actual sending is handled by notification processor
        return new ScheduledNotification(
                task.getTaskInfo().getId().toString(), // Use getId().toString() to get UUID string
                task.getTaskInfo().getStartDateTime().minusMinutes(reminder.getMinutesBefore()),
                userEmail
        );
    }

    @Override
    public void cancelEmailReminder(String notificationId) {
        // Implementation depends on how scheduled notifications are stored
        System.out.println("Cancelled email reminder: " + notificationId);
    }

    @Override
    public void processPendingNotifications() {
        // This would be called by a background scheduler
        // Implementation will be added in the next step
        System.out.println("Processing pending notifications...");
    }

    private Message createEmailMessage(String recipientEmail, Task task) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.ssl.trust", smtpHost);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.debug", "true"); // Enable for debugging

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

        String priority = task.getTaskInfo().getPriority().toString();
        String subject = String.format("🌦️ RainCheck Reminder: %s (%s Priority)",
                task.getTaskInfo().getTaskName(), priority); // Use getTaskName()
        message.setSubject(subject);

        String emailBody = createEmailBody(task);
        message.setContent(emailBody, "text/html; charset=utf-8");

        return message;
    }

    private String createEmailBody(Task task) {
        TaskInfo taskInfo = task.getTaskInfo();

        String priorityColor = switch (taskInfo.getPriority()) {
            case HIGH -> "#FF0000"; // Red
            case MEDIUM -> "#FFA500"; // Orange
            case LOW -> "#FFFF00"; // Yellow
        };
        String weatherEmoji = getWeatherEmojiFromIcon(taskInfo.getWeatherIconName());
        return String.format("""
            <html>
            <body>
                <h2 style="color: #2E86AB;">🌦️ RainCheck Task Reminder</h2>
                <div style="border-left: 4px solid %s; padding-left: 15px; margin: 10px 0;">
                    <h3>%s</h3>
                    <p><strong>Scheduled Time:</strong> %s</p>
                    <p><strong>Priority:</strong> <span style="color: %s;">%s</span></p>
                    <p><strong>Temperature:</strong> %s</p>
                    <p><strong>Weather:</strong> %s %s</p>
                    %s
                </div>
                <p style="color: #666; font-size: 12px;">
                    This is an automated reminder from RainCheck. 
                    Plan your day with weather in mind! ☀️🌧️
                </p>
            </body>
            </html>
            """,
                priorityColor,
                taskInfo.getTaskName(), // Use getTaskName()
                taskInfo.getStartDateTime().toString(), // Use getStartDateTime()
                priorityColor,
                taskInfo.getPriority().toString(),
                taskInfo.getTemperature(),
                weatherEmoji, // Use getWeatherEmoji()
                taskInfo.getWeatherDescription(), // Use getWeatherDescription()
                taskInfo.getTag() != null ? "<p><strong>Tag:</strong> " + taskInfo.getTag().toString() + "</p>" : ""
        );
    }

    private String getWeatherEmojiFromIcon(String iconName) {
        if (iconName == null || iconName.isEmpty()) {
            return "🌤️";
        }

        return switch (iconName.toLowerCase()) {
            case "clear-day", "clear" -> "☀️";
            case "clear-night" -> "🌙";
            case "rain" -> "🌧️";
            case "snow" -> "❄️";
            case "sleet" -> "🌨️";
            case "wind" -> "💨";
            case "fog" -> "🌫️";
            case "cloudy" -> "☁️";
            case "partly-cloudy-day" -> "⛅";
            case "partly-cloudy-night" -> "🌙";
            case "thunderstorm" -> "⛈️";
            default -> "🌤️";
        };
    }

}