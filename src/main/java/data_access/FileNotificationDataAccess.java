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

    /**
     * Constructs a FileNotificationDataAccess with file paths for configs and notifications.
     * @param emailConfigsFile the file for email configs
     * @param scheduledNotificationsFile the file for scheduled notifications
     */
    public FileNotificationDataAccess(String emailConfigsFile, String scheduledNotificationsFile) {
        this.emailConfigsFile = emailConfigsFile;
        this.scheduledNotificationsFile = scheduledNotificationsFile;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        createFileIfNotExists(emailConfigsFile);
        createFileIfNotExists(scheduledNotificationsFile);
    }

    /**
     * Saves the email notification config for a user.
     * @param username the username
     * @param config the email notification config
     */
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

    /**
     * Gets the email notification config for a user.
     * @param username the username
     * @return the email notification config, or null if not found
     */
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

    /**
     * Saves a scheduled notification.
     * @param notification the scheduled notification to save
     */
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

    /**
     * Gets all pending notifications scheduled before the given time.
     * @param beforeTime the cutoff time
     * @return list of pending notifications
     */
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

    /**
     * Marks a notification as sent by its ID.
     * @param notificationId the notification ID
     */
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

    /**
     * Deletes a scheduled notification by its ID.
     * @param notificationId the notification ID
     */
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

    /**
     * Gets all notifications for a specific task ID.
     * @param taskId the task ID
     * @return list of notifications for the task
     */
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

    // Creates the file if it does not exist.
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

    // Loads email notification configs from file.
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

    // Saves email notification configs to file.
    private void saveEmailConfigs(Map<String, EmailNotificationConfig> configs) throws IOException {
        objectMapper.writeValue(new File(emailConfigsFile), configs);
    }

    // Loads scheduled notifications from file.
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

    // Saves scheduled notifications to file.
    private void saveScheduledNotifications(List<ScheduledNotification> notifications) throws IOException {
        objectMapper.writeValue(new File(scheduledNotificationsFile), notifications);
    }
}
