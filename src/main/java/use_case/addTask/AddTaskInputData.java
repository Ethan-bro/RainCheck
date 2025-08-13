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

    /**
     * Constructs an AddTaskInputData object with the provided task details.
     *
     * @param taskName the name of the task (must not be null)
     * @param startDateTime the start date and time of the task (must not be null)
     * @param endDateTime the end date and time of the task (must not be null)
     * @param priority the priority of the task (defaults to LOW if null)
     * @param customTag the custom tag associated with the task (nullable)
     * @param reminder the reminder setting for the task (nullable)
     */
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

    /**
     * Returns the name of the task.
     *
     * @return the task name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Sets the name of the task.
     *
     * @param taskName the new task name
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Returns the start date and time of the task.
     *
     * @return the start date and time
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets the start date and time of the task.
     *
     * @param startDateTime the new start date and time
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Returns the end date and time of the task.
     *
     * @return the end date and time
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets the end date and time of the task.
     *
     * @param endDateTime the new end date and time
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Returns the priority of the task.
     *
     * @return the task priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets the priority of the task.
     *
     * @param priority the new priority
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Returns the custom tag associated with the task.
     *
     * @return the custom tag, or null if none
     */
    public CustomTag getTag() {
        return customTag;
    }

    /**
     * Sets the custom tag for the task.
     *
     * @param tag the new custom tag
     */
    public void setTag(CustomTag tag) {
        this.customTag = tag;
    }

    /**
     * Returns the status of the task (e.g., "Incomplete").
     *
     * @return the task status
     */
    public String getTaskStatus() {
        return taskStatus;
    }

    /**
     * Sets the status of the task.
     *
     * @param taskStatus the new task status
     */
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * Returns the reminder setting for the task.
     *
     * @return the reminder, or null if none
     */
    public Reminder getReminder() {
        return reminder;
    }

    /**
     * Sets the reminder for the task.
     *
     * @param reminder the new reminder
     */
    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    /**
     * Sets the deletion status of the task.
     *
     * @param isDeleted the deletion status (e.g., "Yes" or "No")
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * Returns the deletion status of the task.
     *
     * @return the deletion status string
     */
    public String getIsDeleted() {
        return isDeleted;
    }
}
