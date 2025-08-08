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

    public TaskID getId() {
        return id;
    }

    public void setId(TaskID id) {
        this.id = id;
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

    public String getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(String uvIndex) {
        this.uvIndex = uvIndex;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
