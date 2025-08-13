package entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents task details, including weather-related information.
 * To create a fully initialized task, call in order:
 * <ol>
 *     <li>{@link #setCoreDetails(TaskID, String, LocalDateTime, LocalDateTime)}</li>
 *     <li>{@link #setAdditionalDetails(Priority, CustomTag, Reminder, String)}</li>
 *     <li>{@link #setWeatherInfo(String, String, String, String)}</li>
 * </ol>
 */
public class TaskInfo {

    private TaskID id;
    private String taskName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Priority priority;
    private CustomTag customTag;
    private String taskStatus;
    private Reminder reminder;
    private String isDeleted;
    private String weatherDescription;
    private String weatherIconName;
    private String temperature;
    private String uvIndex;

    /**
     * Creates an empty TaskInfo.
     * Use the three initialization methods to set all fields after construction.
     */
    /**
     * Constructs an empty TaskInfo.
     * Use the three initialization methods to set all fields after construction.
     */
    public TaskInfo() {
        // Default empty constructor
    }

    /**
     * Sets core task details such as ID, name, and times.
     * Must be called along with {@link #setAdditionalDetails(Priority, CustomTag, Reminder, String)}
     * and {@link #setWeatherInfo(String, String, String, String)} to fully initialize the task.
     *
     * @param idParam             the unique task ID
     * @param taskNameParam       the name of the task
     * @param startDateTimeParam  the start date and time
     * @param endDateTimeParam    the end date and time
     */
    public void setCoreDetails(TaskID idParam,
                               String taskNameParam,
                               LocalDateTime startDateTimeParam,
                               LocalDateTime endDateTimeParam) {

        this.id = Objects.requireNonNull(idParam);
        this.taskName = Objects.requireNonNull(taskNameParam);
        this.startDateTime = Objects.requireNonNull(startDateTimeParam);
        this.endDateTime = Objects.requireNonNull(endDateTimeParam);
        this.taskStatus = "Incomplete";
    }

    /**
     * Sets additional details like priority, tags, reminders, and deletion status.
     * Must be called along with {@link #setCoreDetails(TaskID, String, LocalDateTime, LocalDateTime)}
     * and {@link #setWeatherInfo(String, String, String, String)} to fully initialize the task.
     *
     * @param priorityParam   the task priority, defaults to LOW if null
     * @param tagParam        a custom tag for the task
     * @param reminderParam   the reminder object
     * @param isDeletedParam  deletion status as "Yes" or "No"
     */
    public void setAdditionalDetails(Priority priorityParam,
                                     CustomTag tagParam,
                                     Reminder reminderParam,
                                     String isDeletedParam) {

        this.priority = Objects.requireNonNullElse(priorityParam, Priority.LOW);
        this.customTag = tagParam;
        this.reminder = reminderParam;
        this.isDeleted = isDeletedParam;
    }

    /**
     * Adds weather-related information to the task.
     * Must be called along with {@link #setCoreDetails(TaskID, String, LocalDateTime, LocalDateTime)}
     * and {@link #setAdditionalDetails(Priority, CustomTag, Reminder, String)} to fully initialize the task.
     *
     * @param weatherDescriptionParam  the description of the weather
     * @param weatherIconNameParam     the weather icon identifier
     * @param temperatureParam         the temperature in Celsius
     * @param uvIndexParam             the UV index
     */
    public void setWeatherInfo(String weatherDescriptionParam,
                               String weatherIconNameParam,
                               String temperatureParam,
                               String uvIndexParam) {

        this.weatherDescription = weatherDescriptionParam;
        this.weatherIconName = weatherIconNameParam;
        this.temperature = temperatureParam;
        this.uvIndex = uvIndexParam;
    }

    /**
     * Gets the task ID.
     * @return the task ID
     */
    public TaskID getId() {
        return id;
    }

    /**
     * Sets the task ID.
     * @param id the task ID to set
     */
    public void setId(TaskID id) {
        this.id = id;
    }

    /**
     * Gets the task name.
     * @return the task name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Sets the task name.
     * @param taskName the task name to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Gets the start date and time.
     * @return the start date and time
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets the start date and time.
     * @param startDateTime the start date and time to set
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Gets the end date and time.
     * @return the end date and time
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets the end date and time.
     * @param endDateTime the end date and time to set
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Gets the task priority.
     * @return the priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets the task priority.
     * @param priority the priority to set
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Gets the custom tag.
     * @return the custom tag
     */
    public CustomTag getTag() {
        return customTag;
    }

    /**
     * Sets the custom tag.
     * @param tag the custom tag to set
     */
    public void setTag(CustomTag tag) {
        this.customTag = tag;
    }

    /**
     * Gets the task status.
     * @return the task status
     */
    public String getTaskStatus() {
        return taskStatus;
    }

    /**
     * Sets the task status.
     * @param taskStatus the task status to set
     */
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * Gets the reminder.
     * @return the reminder
     */
    public Reminder getReminder() {
        return reminder;
    }

    /**
     * Sets the reminder.
     * @param reminder the reminder to set
     */
    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
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
     * Gets the weather icon name.
     * @return the weather icon name
     */
    public String getWeatherIconName() {
        return weatherIconName;
    }

    /**
     * Sets the weather icon name.
     * @param weatherIconName the weather icon name to set
     */
    public void setWeatherIconName(String weatherIconName) {
        this.weatherIconName = weatherIconName;
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

    /**
     * Gets the UV index.
     * @return the UV index
     */
    public String getUvIndex() {
        return uvIndex;
    }

    /**
     * Sets the UV index.
     * @param uvIndex the UV index to set
     */
    public void setUvIndex(String uvIndex) {
        this.uvIndex = uvIndex;
    }

    /**
     * Gets the deleted status.
     * @return the deleted status
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * Sets the deleted status.
     * @param isDeleted the deleted status to set
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
