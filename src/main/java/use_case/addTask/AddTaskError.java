package use_case.addTask;

import interface_adapter.addTask.Constants;

public enum AddTaskError {
    EMPTY_NAME("Task Name cannot be empty!"),
    EXCEEDS_CHAR_LIM("Task Name must be less than " +Constants.CHAR_LIMIT + " characters!"),
    EMPTY_TIME("Start Date/Time and End Date/Time cannot be empty!"),
    START_AFTER_END("End Date/Time must be after Start Date/Time"),
    NO_TAG_SELECTED("Please create and select a tag first");

    private final String message;

    AddTaskError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
