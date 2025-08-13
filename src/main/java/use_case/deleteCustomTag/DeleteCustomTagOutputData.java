package use_case.deleteCustomTag;

public class DeleteCustomTagOutputData {

    private final String message;
    private final boolean isUseCaseFailed;

    /**
     * Constructs a DeleteCustomTagOutputData object for a successful operation.
     *
     * @param message the success message
     */
    public DeleteCustomTagOutputData(String message) {
        this.isUseCaseFailed = false;
        this.message = message;
    }

    /**
     * Gets the message associated with the output data.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Indicates whether the use case failed.
     *
     * @return true if the use case failed, false otherwise
     */
    public boolean isUseCaseFailed() {
        return isUseCaseFailed;
    }
}
