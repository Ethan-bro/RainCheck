package use_case.createCustomTag;

import entity.CustomTag;

import java.util.Map;

/**
 * Interactor for creating custom tags. Ensures tag name and emoji are unique for the user.
 */
public class CCTInteractor implements CCTInputBoundary {

    private final CustomTagDataAccessInterface dao;
    private final CCTOutputBoundary createCustomTagPresenter;

    public CCTInteractor(CustomTagDataAccessInterface dao,
                         CCTOutputBoundary createCustomTagPresenter) {
        this.dao = dao;
        this.createCustomTagPresenter = createCustomTagPresenter;
    }

    @Override
    public void execute(CCTInputData customTagInputData, String username) {
        final String tagName = customTagInputData.getTagName();
        final String tagEmoji = customTagInputData.getTagEmoji();

        // Fetch all existing tags for this user
        final Map<String, String> existingTags = dao.getCustomTags(username);

        // Check if tag name is already taken
        if (existingTags.containsKey(tagName)) {
            final CCTOutputData failedOutput =
                    new CCTOutputData(CCTError.NAME_TAKEN);
            createCustomTagPresenter.prepareFailView(failedOutput);
            return;
        }

        // Check if emoji is already in use
        if (existingTags.containsValue(tagEmoji)) {
            final CCTOutputData failedOutput =
                    new CCTOutputData(CCTError.ICON_TAKEN);
            createCustomTagPresenter.prepareFailView(failedOutput);
            return;
        }

        // If valid, create the tag and save it
        final CustomTag finalTag = new CustomTag(tagName, tagEmoji);
        dao.addCustomTag(username, finalTag);

        final CCTOutputData outputData = new CCTOutputData(finalTag);
        createCustomTagPresenter.prepareSuccessView(outputData);
    }
}
