package data_access;

import entity.CustomTag;
import use_case.CreateCT.CustomTagDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryTagDataAccessObject implements CustomTagDataAccessInterface {

    private Map<String, String> customTags;

    public InMemoryTagDataAccessObject() {
        this.customTags = new HashMap<String, String>();
    }

    @Override
    public Map<String, String> getCustomTags(String username) {
        return customTags;
    }

    @Override
    public void addCustomTag(String username, CustomTag tagToAdd) {
        String tagName = tagToAdd.getTagName();
        String tagIcon = tagToAdd.getTagIcon();

        customTags.put(tagName, tagIcon);
    }

    @Override
    public void deleteCustomTag(String username, CustomTag tagToDelete) {
        String tagName = tagToDelete.getTagName();

        customTags.remove(tagName);
    }
}
