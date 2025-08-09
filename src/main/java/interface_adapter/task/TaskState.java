package interface_adapter.task;

import entity.CustomTag;

import java.time.LocalDateTime;

/**
 * Holds the state for a single Task, used by TaskViewModel.
 */
public class TaskState {
    private final String taskId;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private String priority;
    private String tagString;
    private Integer reminderMinutes;
    private CustomTag tag;
    private boolean completed;

    private String weatherDescription;
    private String weatherEmoji;
    private String temperature;

    public TaskState(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public Integer getReminderMinutes() {
        return reminderMinutes;
    }

    public void setReminderMinutes(Integer reminderMinutes) {
        this.reminderMinutes = reminderMinutes;
    }

    public CustomTag getTag() {
        return tag;
    }

    public void setTag(CustomTag tag) {
        this.tag = tag;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherEmoji() {
        return weatherEmoji;
    }

    public void setWeatherEmoji(String weatherEmoji) {
        this.weatherEmoji = weatherEmoji;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
