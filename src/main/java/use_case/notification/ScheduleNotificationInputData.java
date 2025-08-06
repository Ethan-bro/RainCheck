package use_case.notification;

import entity.Reminder;

/**
 * Input data for scheduling email notifications.
 */
public class ScheduleNotificationInputData {
    private final String taskId;
    private final String username;
    private final Reminder reminder;

    public ScheduleNotificationInputData(String taskId, String username, Reminder reminder) {
        this.taskId = taskId;
        this.username = username;
        this.reminder = reminder;
    }

    public String getTaskId() { return taskId; }
    public String getUsername() { return username; }
    public Reminder getReminder() { return reminder; }
}