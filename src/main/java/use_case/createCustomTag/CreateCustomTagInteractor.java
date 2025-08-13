package use_case.createCustomTag;

import entity.CustomTag;

import java.util.Map;

/**
 * Interactor for creating custom tags. Ensures tag name and emoji are unique for the user.
 */
public class CreateCustomTagInteractor implements CreateCustomTagInputBoundary {

    private final CustomTagDataAccessInterface dao;
    private final CreateCustomTagOutputBoundary createCustomTagPresenter;

    /**
     * Constructs a CreateCustomTagInteractor with the required dependencies.
     *
     * @param dao the data access interface for custom tags
     * @param createCustomTagPresenter the output boundary for presenting results
     */
    public CreateCustomTagInteractor(CustomTagDataAccessInterface dao,
                                     CreateCustomTagOutputBoundary createCustomTagPresenter) {
        this.dao = dao;
        this.createCustomTagPresenter = createCustomTagPresenter;
    }

    /**
     * Executes the create custom tag use case: validates uniqueness and creates the tag if valid.
     *
     * @param customTagInputData the input data for the new custom tag
     * @param username the username of the user creating the tag
     */
    @Override
    public void execute(CreateCustomTagInputData customTagInputData, String username) {
        final String tagName = customTagInputData.getTagName();
        final String tagIcon = customTagInputData.getTagEmoji();

        // Fetch all existing tags for this user
        final Map<String, String> existingTags = dao.getCustomTags(username);

        final CreateCustomTagOutputData outputData;
        final boolean isFailure;

        if (existingTags.containsKey(tagName)) {
            outputData = new CreateCustomTagOutputData(CreateCustomTagError.NAME_TAKEN);
            isFailure = true;
        }
        else if (existingTags.containsValue(tagIcon)) {
            outputData = new CreateCustomTagOutputData(CreateCustomTagError.ICON_TAKEN);
            isFailure = true;
        }
        else {
            // If valid, create the tag and save it
            final CustomTag finalTag = new CustomTag(tagName, tagIcon);
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
