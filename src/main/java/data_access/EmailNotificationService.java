package data_access;

import entity.Reminder;
import entity.ScheduledNotification;
import entity.Task;
import entity.TaskInfo;
import entity.User;

import use_case.notification.EmailNotificationServiceInterface;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Implementation of email notification service using JavaMail API.
 */
public class EmailNotificationService implements EmailNotificationServiceInterface {

    private static final String TRUE = "true";

    private final String smtpHost;
    private final String smtpPort;
    private final String senderEmail;
    private final String senderPassword;

    /**
     * Constructs an EmailNotificationService with SMTP configuration and sender credentials.
     * @param smtpHost the SMTP host
     * @param smtpPort the SMTP port
     * @param senderEmail the sender's email address
     * @param senderPassword the sender's email password
     */
    public EmailNotificationService(final String smtpHost, final String smtpPort,
                                    final String senderEmail, final String senderPassword) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
    }

    /**
     * Sends a task reminder email to the specified user.
     * @param user the user to notify
     * @param task the task for which to send a reminder
     * @param userEmail the recipient's email address
     */
    @Override
    public void sendTaskReminder(final User user, final Task task, final String userEmail) {
        try {
            final Message message = createEmailMessage(userEmail, task);
            Transport.send(message);
            System.out.println("Reminder email sent successfully to: " + userEmail);
        }
        catch (MessagingException ex) {
            System.err.println("Failed to send email reminder: " + ex.getMessage());
            throw new RuntimeException("Email sending failed", ex);
        }
    }

    /**
     * Schedules an email reminder for a task.
     * @param task the task to schedule a reminder for
     * @param userEmail the recipient's email address
     * @param reminder the reminder configuration
     * @return the scheduled notification
     */
    @Override
    public ScheduledNotification scheduleEmailReminder(final Task task, final String userEmail,
                                                       final Reminder reminder) {
        return new ScheduledNotification(
                task.getTaskInfo().getId().toString(),
                task.getTaskInfo().getStartDateTime().minusMinutes(reminder.getMinutesBefore()),
                userEmail
        );
    }

    /**
     * Cancels a scheduled email reminder by notification ID.
     * @param notificationId the ID of the notification to cancel
     */
    @Override
    public void cancelEmailReminder(final String notificationId) {
        System.out.println("Cancelled email reminder: " + notificationId);
    }

    /**
     * Processes all pending email notifications.
     */
    @Override
    public void processPendingNotifications() {
        System.out.println("Processing pending notifications...");
    }

    private Message createEmailMessage(final String recipientEmail, final Task task) throws MessagingException {
        final Properties props = new Properties();
        props.put("mail.smtp.auth", TRUE);
        props.put("mail.smtp.starttls.enable", TRUE);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.ssl.trust", smtpHost);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.debug", TRUE);

        final Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        final Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

        final TaskInfo taskInfo = task.getTaskInfo();

        final String priority = taskInfo.getPriority().toString();
        final String subject = String.format("🌦️ RainCheck Reminder: %s (%s Priority)",
                taskInfo.getTaskName(), priority);
        message.setSubject(subject);

        final String emailBody = createEmailBody(task);
        message.setContent(emailBody, "text/html; charset=utf-8");

        return message;
    }

    private String createEmailBody(final Task task) {
        final TaskInfo taskInfo = task.getTaskInfo();

        final String priorityColor;
        switch (taskInfo.getPriority()) {
            case HIGH -> priorityColor = "#FF0000";
            case MEDIUM -> priorityColor = "#FFA500";
            case LOW -> priorityColor = "#FFFF00";
            default -> priorityColor = "#000000";
        }

        final String weatherEmoji = getWeatherEmojiFromIcon(taskInfo.getWeatherIconName());

        final String tagSection;
        if (taskInfo.getTag() != null) {
            tagSection = "<p><strong>Tag:</strong> " + taskInfo.getTag().toString() + "</p>";
        }
        else {
            tagSection = "";
        }

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
                taskInfo.getTaskName(),
                taskInfo.getStartDateTime().toString(),
                priorityColor,
                taskInfo.getPriority().toString(),
                taskInfo.getTemperature(),
                weatherEmoji,
                taskInfo.getWeatherDescription(),
                tagSection
        );
    }

    private String getWeatherEmojiFromIcon(final String iconName) {
        final String emoji;

        if (iconName == null || iconName.isEmpty()) {
            emoji = "🌤️";
        }
        else {
            emoji = switch (iconName.toLowerCase()) {
                case "clear-day", "clear" -> "☀️";
                case "clear-night", "partly-cloudy-night" -> "🌙";
                case "rain" -> "🌧️";
                case "snow" -> "❄️";
                case "sleet" -> "🌨️";
                case "wind" -> "💨";
                case "fog" -> "🌫️";
                case "cloudy" -> "☁️";
                case "partly-cloudy-day" -> "⛅";
                case "thunderstorm" -> "⛈️";
                default -> "🌤️";
            };
        }

        return emoji;
    }
}
