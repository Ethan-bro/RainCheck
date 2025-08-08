package use_case.createCustomTag;

import entity.CustomTag;

import java.util.Map;

/**
 * Interactor for creating custom tags. Ensures tag name and emoji are unique for the user.
 */
public class cctInteractor implements cctInputBoundary {

    private final CustomTagDataAccessInterface dao;
    private final cctOutputBoundary createCustomTagPresenter;

    public cctInteractor(CustomTagDataAccessInterface dao,
                         cctOutputBoundary createCustomTagPresenter) {
        this.dao = dao;
        this.createCustomTagPresenter = createCustomTagPresenter;
    }

    @Override
    public void execute(cctInputData customTagInputData, String username) {
        final String tagName = customTagInputData.getTagName();
        final String tagEmoji = customTagInputData.getTagEmoji();

        // Fetch all existing tags for this user
        final Map<String, String> existingTags = dao.getCustomTags(username);

        // Check if tag name is already taken
        if (existingTags.containsKey(tagName)) {
            final cctOutputData failedOutput =
                    new cctOutputData(cctError.NAME_TAKEN);
            createCustomTagPresenter.prepareFailView(failedOutput);
            return;
        }

        // Check if emoji is already in use
        if (existingTags.containsValue(tagEmoji)) {
            final cctOutputData failedOutput =
                    new cctOutputData(cctError.ICON_TAKEN);
            createCustomTagPresenter.prepareFailView(failedOutput);
            return;
        }

        // If valid, create the tag and save it
        final CustomTag finalTag = new CustomTag(tagName, tagEmoji);
        dao.addCustomTag(username, finalTag);

        final cctOutputData outputData = new cctOutputData(finalTag);
        createCustomTagPresenter.prepareSuccessView(outputData);
    }
}
