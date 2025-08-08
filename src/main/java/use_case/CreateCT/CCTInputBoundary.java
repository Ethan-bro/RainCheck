package use_case.CreateCT;

public interface CCTInputBoundary {

    /**
     * Executes the createCustomTag use case.
     * @param CustomTagInputData the input data
     * @param  username the currently signed in user's username
     */
    void execute(CCTInputData CustomTagInputData, String username);
}
