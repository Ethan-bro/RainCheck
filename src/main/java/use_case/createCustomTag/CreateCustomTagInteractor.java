package use_case.createCustomTag;

import entity.CustomTag;

import java.util.Map;

/**
 * Interactor for creating custom tags. Ensures tag name and emoji are unique for the user.
 */
public class CreateCustomTagInteractor implements CreateCustomTagInputBoundary {

    private final CustomTagDataAccessInterface dao;
    private final CreateCustomTagOutputBoundary createCustomTagPresenter;

    public CreateCustomTagInteractor(CustomTagDataAccessInterface dao,
                                     CreateCustomTagOutputBoundary createCustomTagPresenter) {
        this.dao = dao;
        this.createCustomTagPresenter = createCustomTagPresenter;
    }

    @Override
    public void execute(CreateCustomTagInputData customTagInputData, String username) {
        final String tagName = customTagInputData.getTagName();
        final String tagEmoji = customTagInputData.getTagEmoji();

        // Fetch all existing tags for this user
        final Map<String, String> existingTags = dao.getCustomTags(username);

        final CreateCustomTagOutputData outputData;
        final boolean isFailure;

        if (existingTags.containsKey(tagName)) {
            outputData = new CreateCustomTagOutputData(CreateCustomTagError.NAME_TAKEN);
            isFailure = true;
        }
        else if (existingTags.containsValue(tagEmoji)) {
            outputData = new CreateCustomTagOutputData(CreateCustomTagError.ICON_TAKEN);
            isFailure = true;
        }
        else {
            // If valid, create the tag and save it
            final CustomTag finalTag = new CustomTag(tagName, tagEmoji);
            dao.addCustomTag(username, finalTag);
            outputData = new CreateCustomTagOutputData(finalTag);
            isFailure = false;
        }

        if (isFailure) {
            createCustomTagPresenter.prepareFailView(outputData);
        }
        else {
            createCustomTagPresenter.prepareSuccessView(outputData);
        }
    }
}
