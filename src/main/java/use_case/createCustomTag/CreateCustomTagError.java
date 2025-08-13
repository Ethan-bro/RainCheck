package use_case.createCustomTag;

public enum CreateCustomTagError {
    NAME_TAKEN("Tag name is already in use!"),
    ICON_TAKEN("Tag icon is already in use!"),;

    private final String message;

    /**
     * Constructs a CreateCustomTagError with the specified error message.
     *
     * @param message the error message to associate with this error type
     */
    CreateCustomTagError(String message) {
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
