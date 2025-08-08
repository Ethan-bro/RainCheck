package use_case.createCustomTag;

import entity.CustomTag;

import java.util.Map;

/**
 * Interface defining data access operations for CustomTag entities.
 */
public interface CustomTagDataAccessInterface {

    /**
     * Retrieves all custom tags for a given user.
     *
     * @param username the username whose tags are to be retrieved
     * @return a map where keys are tag names and values are tag icons
     */
    Map<String, String> getCustomTags(String username);

    /**
     * Adds a new custom tag for the specified user.
     *
     * @param username the username to add the tag for
     * @param tagToAdd the CustomTag object to be added
     */
    void addCustomTag(String username, CustomTag tagToAdd);

    /**
     * Deletes a custom tag for the specified user.
     *
     * @param username the username to delete the tag from
     * @param tagToDelete the CustomTag object to be deleted
     */
    void deleteCustomTag(String username, CustomTag tagToDelete);
}
