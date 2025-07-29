package use_case.addTask;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;

import java.time.LocalDateTime;

public class AddTaskInputData {

    private String taskName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Priority priority;
    private CustomTag customTag;
    private String taskStatus;
    private Reminder reminder;

    public AddTaskInputData(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = null;
        this.customTag = null;
        this.taskStatus = "Incomplete";
        this.reminder = null;
    }

    public AddTaskInputData(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                            Priority priority) {
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.customTag = null;
        this.taskStatus = "Incomplete";
        this.reminder = null;
    }

    public AddTaskInputData(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                            CustomTag customTag) {
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = null;
        this.customTag = customTag;
        this.taskStatus = "Incomplete";
        this.reminder = null;
    }

    public AddTaskInputData(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                            Reminder reminder) {
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = null;
        this.customTag = null;
        this.taskStatus = "Incomplete";
        this.reminder = reminder;
    }

    public AddTaskInputData(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                            Priority priority, CustomTag customTag) {
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.customTag = customTag;
        this.taskStatus = "Incomplete";
        this.reminder = null;
    }

    public AddTaskInputData(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                            Priority priority, Reminder reminder) {
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.customTag = null;
        this.taskStatus = "Incomplete";
        this.reminder = reminder;
    }

    public AddTaskInputData(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                            CustomTag customTag, Reminder reminder) {
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = null;
        this.customTag = customTag;
        this.taskStatus = "Incomplete";
        this.reminder = reminder;
    }

    public AddTaskInputData(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                            Priority priority, CustomTag customTag, Reminder reminder) {
        this.taskName = taskName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.customTag = customTag;
        this.taskStatus = "Incomplete";
        this.reminder = reminder;
    }

    public String getTaskName() {return taskName;}

    public void setTaskName(String taskName) {this.taskName = taskName;}

    public LocalDateTime getStartDateTime() {return startDateTime;}

    public void setStartDateTime(LocalDateTime startDateTime) {this.startDateTime = startDateTime;}

    public LocalDateTime getEndDateTime() {return endDateTime;}

    public void setEndDateTime(LocalDateTime endDateTime) {this.endDateTime = endDateTime;}

    public Priority getPriority() {return priority;}

    public void setPriority(Priority priority) {this.priority = priority;}

    public CustomTag getCustomTag() {return customTag;}

    public void setCategory(CustomTag customTag) {this.customTag = customTag;}

    public String getTaskStatus() {return taskStatus;}

    public void setTaskStatus(String taskStatus) {this.taskStatus = taskStatus;}

    public Reminder getReminder() {return reminder;}

    public void setReminder(Reminder reminder) {this.reminder = reminder;}
}
