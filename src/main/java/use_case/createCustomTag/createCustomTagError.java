package use_case.createCustomTag;

public enum createCustomTagError {
    NAME_TAKEN("Tag name is already in use!"),
    ICON_TAKEN("Tag icon is already in use!"),;

    private final String message;

    createCustomTagError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

