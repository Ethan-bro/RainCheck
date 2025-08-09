package use_case.addTask;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;

import java.time.LocalDateTime;
import java.util.Objects;

public class AddTaskInputData {

    private String taskName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Priority priority;
    private CustomTag customTag;
    private String taskStatus;
    private Reminder reminder;
    private String isDeleted;

    public AddTaskInputData(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                            Priority priority, CustomTag customTag, Reminder reminder) {
        this.taskName = Objects.requireNonNull(taskName);
        this.startDateTime = Objects.requireNonNull(startDateTime);
        this.endDateTime = Objects.requireNonNull(endDateTime);

        this.priority = Objects.requireNonNullElse(priority, Priority.LOW);

        this.customTag = customTag;
        this.taskStatus = "Incomplete";
        this.isDeleted = "No";
        this.reminder = reminder;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public CustomTag getTag() {
        return customTag;
    }

    public void setTag(CustomTag tag) {
        this.customTag = tag;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getIsDeleted() {
        return isDeleted;
    }
}
