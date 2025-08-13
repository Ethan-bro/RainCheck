package interface_adapter.task;

import java.time.LocalDateTime;

import entity.CustomTag;

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


    /**
     * Constructs a TaskState for the given task ID.
     *
     * @param taskId the ID of the task
     */
    public TaskState(String taskId) {
        this.taskId = taskId;
    }


    /**
     * Gets the task ID.
     * @return the task ID
     */
    public String getTaskId() {
        return taskId;
    }


    /**
     * Gets the task title.
     * @return the task title
     */
    public String getTitle() {
        return title;
    }


    /**
     * Sets the task title.
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * Gets the start date and time.
     * @return the start date and time
     */
    public LocalDateTime getStart() {
        return start;
    }


    /**
     * Sets the start date and time.
     * @param start the start date and time to set
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }


    /**
     * Gets the end date and time.
     * @return the end date and time
     */
    public LocalDateTime getEnd() {
        return end;
    }


    /**
     * Sets the end date and time.
     * @param end the end date and time to set
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Gets the priority.
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the priority.
     * @param priority the priority to set
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Gets the tag string.
     * @return the tag string
     */
    public String getTagString() {
        return tagString;
    }

    /**
     * Sets the tag string.
     * @param tagString the tag string to set
     */
    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    /**
     * Gets the reminder minutes.
     * @return the reminder minutes
     */
    public Integer getReminderMinutes() {
        return reminderMinutes;
    }

    /**
     * Sets the reminder minutes.
     * @param reminderMinutes the reminder minutes to set
     */
    public void setReminderMinutes(Integer reminderMinutes) {
        this.reminderMinutes = reminderMinutes;
    }

    /**
     * Gets the custom tag.
     * @return the custom tag
     */
    public CustomTag getTag() {
        return tag;
    }

    /**
     * Sets the custom tag.
     * @param tag the custom tag to set
     */
    public void setTag(CustomTag tag) {
        this.tag = tag;
    }

    /**
     * Gets whether the task is completed.
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Sets whether the task is completed.
     * @param completed true if completed, false otherwise
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Gets the weather description.
     * @return the weather description
     */
    public String getWeatherDescription() {
        return weatherDescription;
    }

    /**
     * Sets the weather description.
     * @param weatherDescription the weather description to set
     */
    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    /**
     * Gets the weather emoji.
     * @return the weather emoji
     */
    public String getWeatherEmoji() {
        return weatherEmoji;
    }

    /**
     * Sets the weather emoji.
     * @param weatherEmoji the weather emoji to set
     */
    public void setWeatherEmoji(String weatherEmoji) {
        this.weatherEmoji = weatherEmoji;
    }

    /**
     * Gets the temperature.
     * @return the temperature
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature.
     * @param temperature the temperature to set
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
