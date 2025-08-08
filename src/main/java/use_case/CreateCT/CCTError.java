package use_case.CreateCT;

public enum CCTError {
    NAME_TAKEN("Tag name is already in use!"),
    ICON_TAKEN("Tag icon is already in use!"),;

    private final String message;

    CCTError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

