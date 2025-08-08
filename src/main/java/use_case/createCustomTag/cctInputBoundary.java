package use_case.createCustomTag;

public interface cctInputBoundary {

    /**
     * Executes the createCustomTag use case.
     * @param CustomTagInputData the input data
     * @param  username the currently signed in user's username
     */
    void execute(cctInputData CustomTagInputData, String username);
}
