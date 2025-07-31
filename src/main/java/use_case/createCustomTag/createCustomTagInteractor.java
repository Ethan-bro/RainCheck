package use_case.createCustomTag;

import entity.CustomTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Interactor for creating custom tags. Ensures tag name and emoji are unique for the user.
 */
public class createCustomTagInteractor implements createCustomTagInputBoundary {

    private final customTagDataAccessInterface dao;
    private final createCustomTagOutputBoundary createCustomTagPresenter;

    public createCustomTagInteractor(customTagDataAccessInterface dao,
                                     createCustomTagOutputBoundary createCustomTagPresenter) {
        this.dao = dao;
        this.createCustomTagPresenter = createCustomTagPresenter;
    }

    @Override
    public void execute(createCustomTagInputData customTagInputData, String username) {
        String tagName = customTagInputData.getTagName();
        String tagEmoji = customTagInputData.getTagEmoji();

        // Fetch all existing tags for this user
        Map<String, String> existingTags = dao.getCustomTags(username);

        // Check if tag name is already taken
        if (existingTags.containsKey(tagName)) {
            createCustomTagOutputData failedOutput =
                    new createCustomTagOutputData(createCustomTagError.NAME_TAKEN);
            createCustomTagPresenter.prepareFailView(failedOutput);
            return;
        }

        // Check if emoji is already in use
        if (existingTags.containsValue(tagEmoji)) {
            createCustomTagOutputData failedOutput =
                    new createCustomTagOutputData(createCustomTagError.ICON_TAKEN);
            createCustomTagPresenter.prepareFailView(failedOutput);
            return;
        }

        // If valid, create the tag and save it
        CustomTag finalTag = new CustomTag(tagName, tagEmoji);
        dao.addCustomTag(username, finalTag);

        createCustomTagOutputData outputData = new createCustomTagOutputData(finalTag);
        createCustomTagPresenter.prepareSuccessView(outputData);
    }

    /**
     * Fetch all tags for the user and return as a list of CustomTag entities.
     */
    public List<CustomTag> fetchTags(String username) {
        Map<String, String> tagsMap = dao.getCustomTags(username);
        List<CustomTag> tags = new ArrayList<>();
        for (Map.Entry<String, String> entry : tagsMap.entrySet()) {
            tags.add(new CustomTag(entry.getKey(), entry.getValue()));
        }
        return tags;
    }
}
