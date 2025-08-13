package data_access;

import java.util.HashMap;
import java.util.Map;

import entity.CustomTag;
import use_case.createCustomTag.CustomTagDataAccessInterface;

public class InMemoryTagDataAccessObject implements CustomTagDataAccessInterface {

    private Map<String, String> customTags;

    /**
     * Constructs an in-memory tag data access object.
     */
    public InMemoryTagDataAccessObject() {
        this.customTags = new HashMap<>();
    }

    @Override
    /**
     * Gets the custom tags for the given username.
     * @param username the username
     * @return a map of tag names to tag icons
     */
    public Map<String, String> getCustomTags(String username) {
        return customTags;
    }

    @Override
    /**
     * Adds a custom tag for the given username.
     * @param username the username
     * @param tagToAdd the tag to add
     */
    public void addCustomTag(String username, CustomTag tagToAdd) {
        final String tagName = tagToAdd.getTagName();
        final String tagIcon = tagToAdd.getTagIcon();

        customTags.put(tagName, tagIcon);
    }

    @Override
    /**
     * Deletes a custom tag for the given username.
     * @param username the username
     * @param tagToDelete the tag to delete
     */
    public void deleteCustomTag(String username, CustomTag tagToDelete) {
        final String tagName = tagToDelete.getTagName();

        customTags.remove(tagName);
    }
}
