package interface_adapter.createTag;

import entity.CustomTag;

import use_case.createCustomTag.cctInputBoundary;
import use_case.createCustomTag.cctInputData;

/**
 * Controller responsible for handling the creation of custom tags.
 */
public class CreateCustomTagController {

    private final cctInputBoundary createCustomTagInteractor;

    public CreateCustomTagController(cctInputBoundary createCustomTagInteractor) {
        this.createCustomTagInteractor = createCustomTagInteractor;
    }

    /**
     * Executes the process to create a new custom tag for the specified user.
     *
     * @param customTag the custom tag to be created
     * @param username  the username of the user creating the tag
     */
    public void execute(CustomTag customTag, String username) {
        final cctInputData inputData = new cctInputData(customTag.getTagName(), customTag.getTagIcon());
        createCustomTagInteractor.execute(inputData, username);
    }
}
