package use_case.CreateTag;

public enum TagErrors {
    NAME_TAKEN("Tag name is already in use!"),
    ICON_TAKEN("Tag icon is already in use!"),;

    private final String message;

    TagErrors(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

