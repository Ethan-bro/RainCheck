package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reminder {
    private LocalDateTime remindDateTime;
    private String message;

    public Reminder(LocalDateTime remindDateTime, String message) {
        this.remindDateTime = Objects.requireNonNull(remindDateTime);
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
        return "[" + remindDateTime.toString() + "] " + message;
    }
}
