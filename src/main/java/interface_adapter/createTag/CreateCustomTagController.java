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
     * @param tagName the name of the tag to be created
     * @param tagIcon the icon of the tag to be created
     * @param username  the username of the user creating the tag
     */
    public void execute(String tagName,  String tagIcon, String username) {
        final CreateCustomTagInputData inputData = new CreateCustomTagInputData(tagName, tagIcon
        );
        createCustomTagInteractor.execute(inputData, username);
    }
}
