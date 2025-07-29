package use_case.createCustomTag;

public interface createCustomTagInputBoundary {

    /**
     * Executes the createCustomTag use case.
     * @param CustomTagInputData the input data
     * @param  username the currently signed in user's username
     */
    void execute(createCustomTagInputData CustomTagInputData, String username);
}
