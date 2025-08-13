package data_access;

import entity.CustomTag;

import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryTagDataAccessObject implements CustomTagDataAccessInterface {

    private Map<String, String> customTags;

    /**
     * Constructs an in-memory tag data access object.
     */
    public InMemoryTagDataAccessObject() {
        this.customTags = new HashMap<>();
    }

    /**
     * Gets the custom tags for the given username.
     * @param username the username
     * @return a map of tag names to tag icons
     */
    @Override
    public Map<String, String> getCustomTags(String username) {
        return customTags;
    }

    /**
     * Adds a custom tag for the given username.
     * @param username the username
     * @param tagToAdd the tag to add
     */
    @Override
    public void addCustomTag(String username, CustomTag tagToAdd) {
        final String tagName = tagToAdd.getTagName();
        final String tagIcon = tagToAdd.getTagIcon();

        customTags.put(tagName, tagIcon);
    }

    /**
     * Deletes a custom tag for the given username.
     * @param username the username
     * @param tagToDelete the tag to delete
     */
    @Override
    public void deleteCustomTag(String username, CustomTag tagToDelete) {
        final String tagName = tagToDelete.getTagName();

        customTags.remove(tagName);
    }
}
