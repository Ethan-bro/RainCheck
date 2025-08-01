package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class TaskInfo {
    private TaskID id;
    private String taskName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Priority priority;
    private CustomTag customTag;
    private String taskStatus;
    private Reminder reminder;

    private String weatherDescription;
    private String weatherIconName;
    private String temperature;

    public TaskInfo(TaskID id, String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                    Priority priority, CustomTag customTag, Reminder reminder, String weatherDescription,
                    String weatherIconName, String temperature) {
        this.id = Objects.requireNonNull(id);
        this.taskName = Objects.requireNonNull(taskName);
        this.startDateTime = Objects.requireNonNull(startDateTime);
        this.endDateTime = Objects.requireNonNull(endDateTime);
        this.priority = (priority != null) ? priority : Priority.LOW;
        this.customTag = customTag;
        this.taskStatus = "Incomplete";
        this.reminder = reminder;
        this.weatherDescription = weatherDescription;
        this.weatherIconName = weatherIconName;
        this.temperature = temperature;
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

    public CustomTag getTag() {return customTag;}

    public void setTag(CustomTag customTag) {this.customTag = customTag;}

    public String getTaskStatus() {return taskStatus;}

    public void setTaskStatus(String taskStatus) {this.taskStatus = taskStatus;}

    public Reminder getReminder() {return reminder;}

    public void setReminder(Reminder reminder) {this.reminder = reminder;}

    public String getWeatherDescription() {
        return weatherDescription;
    }
    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherIconName() {
        return weatherIconName;
    }
    public void setWeatherIconName(String weatherIconName) {
        this.weatherIconName = weatherIconName;
    }

    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
