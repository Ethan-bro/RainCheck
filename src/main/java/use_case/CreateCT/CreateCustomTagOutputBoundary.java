package use_case.createCustomTag;

/**
 * Output boundary interface for the Create Custom Tag use case.
 */
public interface CreateCustomTagOutputBoundary {

    /**
     * Prepares the view to display when creating a custom tag fails.
     *
     * @param failedOutputData the output data containing failure details
     */
    void prepareFailView(CreateCustomTagOutputData failedOutputData);

    /**
     * Prepares the view to display when creating a custom tag succeeds.
     *
     * @param createCustomTagOutputData the output data containing success details
     */
    void prepareSuccessView(CreateCustomTagOutputData createCustomTagOutputData);
}
