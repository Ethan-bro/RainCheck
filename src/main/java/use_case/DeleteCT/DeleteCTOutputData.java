package use_case.DeleteCT;

public class DeleteCTOutputData {

    private final String message;
    private final boolean isUseCaseFailed;

    public DeleteCTOutputData(String message) {
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
