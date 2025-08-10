package use_case.DeleteTag;

public class DeleteTagOutputData {

    private final String message;
    private final boolean isUseCaseFailed;

    public DeleteTagOutputData(String message) {
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
