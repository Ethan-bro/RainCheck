package interface_adapter.createTag;

import entity.CustomTag;

import use_case.createCustomTag.CreateCustomTagInputBoundary;
import use_case.createCustomTag.CreateCustomTagInputData;

/**
 * Controller responsible for handling the creation of custom tags.
 */
public class CreateCustomTagController {

    private final CreateCustomTagInputBoundary createCustomTagInteractor;

    public CreateCustomTagController(CreateCustomTagInputBoundary createCustomTagInteractor) {
        this.createCustomTagInteractor = createCustomTagInteractor;
    }

    /**
     * Executes the process to create a new custom tag for the specified user.
     *
     * @param customTag the custom tag to be created
     * @param username  the username of the user creating the tag
     */
    public void execute(CustomTag customTag, String username) {
        final CreateCustomTagInputData inputData = new CreateCustomTagInputData(
                customTag.getTagName(), customTag.getTagIcon()
        );
        createCustomTagInteractor.execute(inputData, username);
    }
}
