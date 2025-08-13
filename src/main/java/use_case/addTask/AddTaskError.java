
/**
 * Enum representing possible error states for the Add Task use case.
 * <p>
 * Each constant corresponds to a specific validation or business rule error that can occur
 * when a user attempts to add a new task. This approach centralizes error handling and
 * improves maintainability by providing clear, descriptive error messages for each case.
 * This design follows the Open/Closed Principle by allowing new error types to be added
 * without modifying existing logic.
 */
package use_case.addTask;

import interface_adapter.addTask.Constants;


public enum AddTaskError {
    /**
     * Error when the task name is empty.
     */
    EMPTY_NAME("Task Name cannot be empty!"),

    /**
     * Error when the task name exceeds the character limit.
     */
    EXCEEDS_CHAR_LIM("Task Name must be less than " + Constants.CHAR_LIMIT + " characters!"),

    /**
     * Error when start or end time is not provided.
     */
    EMPTY_TIME("Start Date/Time and End Date/Time cannot be empty!"),

    /**
     * Error when the end time is before the start time.
     */
    START_AFTER_END("End Date/Time must be after Start Date/Time"),

    /**
     * Error when no tag is selected for the task.
     */
    NO_TAG_SELECTED("Please create and select a tag first");


    /**
     * The error message associated with this error type.
     */
    private final String message;


    /**
     * Constructs an AddTaskError with the specified error message.
     *
     * @param message the error message to associate with this error type
     */
    AddTaskError(String message) {
        this.message = message;
    }


    /**
     * Returns the error message associated with this error type.
     *
     * @return the error message string
     */
    public String getMessage() {
        return message;
    }
}
