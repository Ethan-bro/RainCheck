package use_case.CreateTag;

public interface CreateTagInputBoundary {

    /**
     * Executes the createCustomTag use case.
     * @param CustomTagInputData the input data
     * @param  username the currently signed in user's username
     */
    void execute(CreateTagInputData CustomTagInputData, String username);
}
