package data_access;

import entity.EmailNotificationConfig;
import entity.ScheduledNotification;

import use_case.notification.NotificationDataAccessInterface;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * File-based implementation of notification data access.
 */
public class FileNotificationDataAccess implements NotificationDataAccessInterface {

    private final String emailConfigsFile;
    private final String scheduledNotificationsFile;
    private final ObjectMapper objectMapper;

    public FileNotificationDataAccess(String emailConfigsFile, String scheduledNotificationsFile) {
        this.emailConfigsFile = emailConfigsFile;
        this.scheduledNotificationsFile = scheduledNotificationsFile;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        createFileIfNotExists(emailConfigsFile);
        createFileIfNotExists(scheduledNotificationsFile);
    }

    @Override
    public void saveEmailConfig(String username, EmailNotificationConfig config) {
        try {
            final Map<String, EmailNotificationConfig> configs = loadEmailConfigs();
            configs.put(username, config);
            saveEmailConfigs(configs);
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to save email config", ex);
        }
    }

    @Override
    public EmailNotificationConfig getEmailConfig(String username) {
        EmailNotificationConfig config = null;
        try {
            final Map<String, EmailNotificationConfig> configs = loadEmailConfigs();
            if (configs.containsKey(username)) {
                config = configs.get(username);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to load email config", ex);
        }
        return config;
    }

    @Override
    public void saveScheduledNotification(ScheduledNotification notification) {
        try {
            final List<ScheduledNotification> notifications = loadScheduledNotifications();
            notifications.add(notification);
            saveScheduledNotifications(notifications);
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to save scheduled notification", ex);
        }
    }

    @Override
    public List<ScheduledNotification> getPendingNotifications(LocalDateTime beforeTime) {
        try {
            final List<ScheduledNotification> notifications = loadScheduledNotifications();
            return notifications.stream()
                    .filter(notification -> isPendingBefore(notification, beforeTime))
                    .collect(Collectors.toList());
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to load pending notifications", ex);
        }
    }

    private boolean isPendingBefore(ScheduledNotification notification, LocalDateTime beforeTime) {
        return !notification.isSent() && notification.getScheduledTime().isBefore(beforeTime);
    }

    @Override
    public void markNotificationAsSent(String notificationId) {
        try {
            final List<ScheduledNotification> notifications = loadScheduledNotifications();
            final List<ScheduledNotification> updated = new ArrayList<>();
            for (ScheduledNotification notification : notifications) {
                if (notification.getNotificationId().equals(notificationId)) {
                    updated.add(notification.markAsSent());
                }
                else {
                    updated.add(notification);
                }
            }
            saveScheduledNotifications(updated);
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to mark notification as sent", ex);
        }
    }

    @Override
    public void deleteScheduledNotification(String notificationId) {
        try {
            final List<ScheduledNotification> notifications = loadScheduledNotifications();
            final List<ScheduledNotification> filtered = notifications.stream()
                    .filter(notification -> !notification.getNotificationId().equals(notificationId))
                    .collect(Collectors.toList());
            saveScheduledNotifications(filtered);
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to delete scheduled notification", ex);
        }
    }

    @Override
    public List<ScheduledNotification> getNotificationsForTask(String taskId) {
        try {
            final List<ScheduledNotification> notifications = loadScheduledNotifications();
            final List<ScheduledNotification> filtered = notifications.stream()
                    .filter(notification -> notification.getTaskId().equals(taskId))
                    .collect(Collectors.toList());
            return filtered;
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to load notifications for task", ex);
        }
    }

    // Helper methods

    private void createFileIfNotExists(String filename) {
        final File file = new File(filename);
        if (!file.exists()) {
            try {
                final File parent = file.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }
                file.createNewFile();

                if (filename.equals(emailConfigsFile)) {
                    objectMapper.writeValue(file, new HashMap<String, EmailNotificationConfig>());
                }
                else {
                    objectMapper.writeValue(file, new ArrayList<ScheduledNotification>());
                }
            }
            catch (IOException ex) {
                throw new RuntimeException("Failed to create file: " + filename, ex);
            }
        }
    }

    private Map<String, EmailNotificationConfig> loadEmailConfigs() throws IOException {
        final File file = new File(emailConfigsFile);
        final Map<String, EmailNotificationConfig> result;

        if (!file.exists() || file.length() == 0) {
            result = new HashMap<>();
        }
        else {
            result = objectMapper.readValue(file,
                    objectMapper.getTypeFactory()
                            .constructMapType(HashMap.class, String.class, EmailNotificationConfig.class));
        }

        return result;
    }

    private void saveEmailConfigs(Map<String, EmailNotificationConfig> configs) throws IOException {
        objectMapper.writeValue(new File(emailConfigsFile), configs);
    }

    private List<ScheduledNotification> loadScheduledNotifications() throws IOException {
        final File file = new File(scheduledNotificationsFile);
        final List<ScheduledNotification> result;

        if (!file.exists() || file.length() == 0) {
            result = new ArrayList<>();
        }
        else {
            result = objectMapper.readValue(file,
                    objectMapper.getTypeFactory()
                            .constructCollectionType(ArrayList.class, ScheduledNotification.class));
        }

        return result;
    }

    private void saveScheduledNotifications(List<ScheduledNotification> notifications) throws IOException {
        objectMapper.writeValue(new File(scheduledNotificationsFile), notifications);
    }
}
