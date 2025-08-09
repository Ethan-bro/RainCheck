package data_access;

import entity.CustomTag;

import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryTagDataAccessObject implements CustomTagDataAccessInterface {

    private Map<String, String> customTags;

    public InMemoryTagDataAccessObject() {
        this.customTags = new HashMap<>();
    }

    @Override
    public Map<String, String> getCustomTags(String username) {
        return customTags;
    }

    @Override
    public void addCustomTag(String username, CustomTag tagToAdd) {
        final String tagName = tagToAdd.getTagName();
        final String tagIcon = tagToAdd.getTagIcon();

        customTags.put(tagName, tagIcon);
    }

    @Override
    public void deleteCustomTag(String username, CustomTag tagToDelete) {
        final String tagName = tagToDelete.getTagName();

        customTags.remove(tagName);
    }
}
