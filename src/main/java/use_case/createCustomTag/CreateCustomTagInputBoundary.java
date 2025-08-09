package use_case.createCustomTag;

public interface CreateCustomTagInputBoundary {

    /**
     * Executes the createCustomTag use case.
     * @param CustomTagInputData the input data
     * @param  username the currently signed in user's username
     */
    void execute(CreateCustomTagInputData CustomTagInputData, String username);
}
