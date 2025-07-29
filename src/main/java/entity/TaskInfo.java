package entity;

import java.time.LocalDateTime;

public class TaskInfo {
    private TaskID id;
    private String taskName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Priority priority;
    private CustomTag customTag;
    private String taskStatus;
    private Reminder reminder;

    public TaskInfo(TaskID id, String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.id = id;
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = null;
        this.customTag = null;
        this.taskStatus = "Incomplete";
        this.reminder = null;
    }

    public TaskInfo(TaskID id, String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                    Priority priority) {
        this.id = id;
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.customTag = null;
        this.taskStatus = "Incomplete";
        this.reminder = null;
    }

    public TaskInfo(TaskID id, String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                    CustomTag customTag) {
        this.id = id;
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = null;
        this.customTag = customTag;
        this.taskStatus = "Incomplete";
        this.reminder = null;
    }

    public TaskInfo(TaskID id, String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                    Reminder reminder) {
        this.id = id;
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = null;
        this.customTag = null;
        this.taskStatus = "Incomplete";
        this.reminder = reminder;
    }

    public TaskInfo(TaskID id, String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                    Priority priority, CustomTag customTag) {
        this.id = id;
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.customTag = customTag;
        this.taskStatus = "Incomplete";
        this.reminder = null;
    }

    public TaskInfo(TaskID id, String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                    Priority priority, Reminder reminder) {
        this.id = id;
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.customTag = null;
        this.taskStatus = "Incomplete";
        this.reminder = reminder;
    }

    public TaskInfo(TaskID id, String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                    CustomTag customTag, Reminder reminder) {
        this.id = id;
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = null;
        this.customTag = customTag;
        this.taskStatus = "Incomplete";
        this.reminder = reminder;
    }

    public TaskInfo(TaskID id, String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                    Priority priority, CustomTag customTag, Reminder reminder) {
        this.id = id;
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.customTag = customTag;
        this.taskStatus = "Incomplete";
        this.reminder = reminder;
    }

    public TaskID getId() {return id;}

    public void setId(TaskID id) {this.id = id;}

    public String getTaskName() {return taskName;}

    public void setTaskName(String taskName) {this.taskName = taskName;}

    public LocalDateTime getStartDateTime() {return startDateTime;}

    public void setStartDateTime(LocalDateTime startDateTime) {this.startDateTime = startDateTime;}

    public LocalDateTime getEndDateTime() {return endDateTime;}

    public void setEndDateTime(LocalDateTime endDateTime) {this.endDateTime = endDateTime;}

    public Priority getPriority() {return priority;}

    public void setPriority(Priority priority) {this.priority = priority;}

    public CustomTag getCategory() {return customTag;}

    public void setCategory(CustomTag customTag) {this.customTag = customTag;}

    public String getTaskStatus() {return taskStatus;}

    public void setTaskStatus(String taskStatus) {this.taskStatus = taskStatus;}

    public Reminder getReminder() {return reminder;}

    public void setReminder(Reminder reminder) {this.reminder = reminder;}
}
