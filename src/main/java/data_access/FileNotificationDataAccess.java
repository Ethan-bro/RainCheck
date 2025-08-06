package data_access;

import entity.EmailNotificationConfig;
import entity.ScheduledNotification;
import use_case.notification.NotificationDataAccessInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

        // Create files if they don't exist
        createFileIfNotExists(emailConfigsFile);
        createFileIfNotExists(scheduledNotificationsFile);
    }

    @Override
    public void saveEmailConfig(String username, EmailNotificationConfig config) {
        try {
            Map<String, EmailNotificationConfig> configs = loadEmailConfigs();
            configs.put(username, config);
            saveEmailConfigs(configs);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save email config", e);
        }
    }

    @Override
    public EmailNotificationConfig getEmailConfig(String username) {
        try {
            Map<String, EmailNotificationConfig> configs = loadEmailConfigs();
            return configs.get(username);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email config", e);
        }
    }

    @Override
    public void saveScheduledNotification(ScheduledNotification notification) {
        try {
            List<ScheduledNotification> notifications = loadScheduledNotifications();
            notifications.add(notification);
            saveScheduledNotifications(notifications);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save scheduled notification", e);
        }
    }

    @Override
    public List<ScheduledNotification> getPendingNotifications(LocalDateTime beforeTime) {
        try {
            List<ScheduledNotification> notifications = loadScheduledNotifications();
            return notifications.stream()
                    .filter(n -> !n.isSent() && n.getScheduledTime().isBefore(beforeTime))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load pending notifications", e);
        }
    }

    @Override
    public void markNotificationAsSent(String notificationId) {
        try {
            List<ScheduledNotification> notifications = loadScheduledNotifications();
            List<ScheduledNotification> updated = notifications.stream()
                    .map(n -> n.getNotificationId().equals(notificationId) ? n.markAsSent() : n)
                    .collect(Collectors.toList());
            saveScheduledNotifications(updated);
        } catch (IOException e) {
            throw new RuntimeException("Failed to mark notification as sent", e);
        }
    }

    @Override
    public void deleteScheduledNotification(String notificationId) {
        try {
            List<ScheduledNotification> notifications = loadScheduledNotifications();
            notifications.removeIf(n -> n.getNotificationId().equals(notificationId));
            saveScheduledNotifications(notifications);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete scheduled notification", e);
        }
    }

    @Override
    public List<ScheduledNotification> getNotificationsForTask(String taskId) {
        try {
            List<ScheduledNotification> notifications = loadScheduledNotifications();
            return notifications.stream()
                    .filter(n -> n.getTaskId().equals(taskId))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load notifications for task", e);
        }
    }

    // Helper methods
    private void createFileIfNotExists(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                // Only create parent directories if parent exists
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                // Write empty JSON structure
                if (filename.equals(emailConfigsFile)) {
                    objectMapper.writeValue(file, new HashMap<String, EmailNotificationConfig>());
                } else {
                    objectMapper.writeValue(file, new ArrayList<ScheduledNotification>());
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to create file: " + filename, e);
            }
        }
    }

    private Map<String, EmailNotificationConfig> loadEmailConfigs() throws IOException {
        File file = new File(emailConfigsFile);
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }
        return objectMapper.readValue(file,
                objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, EmailNotificationConfig.class));
    }

    private void saveEmailConfigs(Map<String, EmailNotificationConfig> configs) throws IOException {
        objectMapper.writeValue(new File(emailConfigsFile), configs);
    }

    private List<ScheduledNotification> loadScheduledNotifications() throws IOException {
        File file = new File(scheduledNotificationsFile);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(file,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, ScheduledNotification.class));
    }

    private void saveScheduledNotifications(List<ScheduledNotification> notifications) throws IOException {
        objectMapper.writeValue(new File(scheduledNotificationsFile), notifications);
    }
}
