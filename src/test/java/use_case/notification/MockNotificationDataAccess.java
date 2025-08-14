package use_case.notification;

import entity.EmailNotificationConfig;
import entity.ScheduledNotification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockNotificationDataAccess implements NotificationDataAccessInterface {

    private EmailNotificationConfig emailConfig;
    private boolean shouldThrowException = false;
    private String exceptionMessage = "Test exception";
    private List<ScheduledNotification> notifications = new ArrayList<>();

    public void setEmailConfig(EmailNotificationConfig config) {
        this.emailConfig = config;
    }

    public void setShouldThrowException(boolean shouldThrow) {
        this.shouldThrowException = shouldThrow;
    }

    public void setExceptionMessage(String message) {
        this.exceptionMessage = message;
    }

    @Override
    public void saveEmailConfig(String username, EmailNotificationConfig config) {
        if (shouldThrowException) {
            throw new IllegalArgumentException(exceptionMessage);
        }
        this.emailConfig = config;
    }

    @Override
    public EmailNotificationConfig getEmailConfig(String username) {
        if (shouldThrowException) {
            throw new IllegalArgumentException(exceptionMessage);
        }
        return emailConfig;
    }

    @Override
    public void saveScheduledNotification(ScheduledNotification notification) {
        if (shouldThrowException) {
            throw new IllegalArgumentException(exceptionMessage);
        }
        notifications.add(notification);
    }

    @Override
    public List<ScheduledNotification> getPendingNotifications(LocalDateTime beforeTime) {
        if (shouldThrowException) {
            throw new IllegalArgumentException(exceptionMessage);
        }
        return new ArrayList<>(notifications);
    }

    @Override
    public void markNotificationAsSent(String notificationId) {
        if (shouldThrowException) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    @Override
    public void deleteScheduledNotification(String notificationId) {
        if (shouldThrowException) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    @Override
    public List<ScheduledNotification> getNotificationsForTask(String taskId) {
        if (shouldThrowException) {
            throw new IllegalArgumentException(exceptionMessage);
        }
        return new ArrayList<>(notifications);
    }
}