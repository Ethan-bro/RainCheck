package interface_adapter.create_customTag;

import entity.CustomTag;
import use_case.createCustomTag.createCustomTagInputBoundary;
import use_case.createCustomTag.createCustomTagInputData;

public class createCustomTagController {

    private final createCustomTagInputBoundary createCustomTagInteractor;

    public createCustomTagController(createCustomTagInputBoundary createCustomTagInteractor) {
        this.createCustomTagInteractor = createCustomTagInteractor;
    }

    /**
     * Executes the interactor with tag name and emoji, plus the current username.
     * @param customTagName The name of the tag to create.
     * @param customTagEmoji The emoji of the tag.
     * @param username The currently signed-in user's username.
     */
    public void execute(CustomTag customTag, String username) {
        createCustomTagInputData inputData = new createCustomTagInputData(customTag.getTagName(), customTag.getTagEmoji());
        createCustomTagInteractor.execute(inputData, username);
    }
}
