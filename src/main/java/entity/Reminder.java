package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reminder {
    private LocalDateTime remindDateTime;
    private String message;

    // Full constructor
    public Reminder(LocalDateTime remindDateTime, String message) {
        this.remindDateTime = Objects.requireNonNull(remindDateTime);
        this.message = Objects.requireNonNull(message);
    }

    // Constructor for relative time (e.g., 10 minutes before task)
    public Reminder(int minutesBefore, LocalDateTime taskStartTime, String message) {
        this.remindDateTime = taskStartTime.minusMinutes(minutesBefore);
        this.message = message;
    }

    // Constructor with just message, time can be set later
    public Reminder(String message) {
        this.remindDateTime = null;
        this.message = Objects.requireNonNull(message);
    }

    public LocalDateTime getRemindDateTime() {
        return remindDateTime;
    }

    public void setRemindDateTime(LocalDateTime remindDateTime) {
        this.remindDateTime = remindDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Calculates how many minutes before the given task start time the reminder is set.
     * @param taskStartTime The LocalDateTime the task starts.
     * @return Number of minutes before taskStartTime.
     */
    public Integer getMinutes(LocalDateTime taskStartTime) {
        if (remindDateTime == null || taskStartTime == null) return null;

        long minutes = Duration.between(remindDateTime, taskStartTime).toMinutes();

        if (minutes > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (minutes < Integer.MIN_VALUE) return Integer.MIN_VALUE;

        return (int) minutes;
    }

    public boolean isDue(LocalDateTime now) {
        return remindDateTime != null && !now.isBefore(remindDateTime);
    }

    @Override
    public String toString() {
        return (remindDateTime != null ? "[" + remindDateTime + "] " : "") + message;
    }
}
