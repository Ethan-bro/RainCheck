package use_case.deleteCustomTag;

public class DeleteCustomTagOutputData {

    private final String message;
    private final boolean isUseCaseFailed;

    public DeleteCustomTagOutputData(String message) {
        this.isUseCaseFailed = false;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUseCaseFailed() {
        return isUseCaseFailed;
    }
}
