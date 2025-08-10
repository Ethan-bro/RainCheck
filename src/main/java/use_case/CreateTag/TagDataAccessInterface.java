package use_case.CreateTag;

import entity.CustomTag;

import java.util.Map;

public interface TagDataAccessInterface {

    Map<String, String> getCustomTags(String username);

    void addCustomTag(String username, CustomTag tagToAdd);

    void deleteCustomTag(String username, CustomTag tagToDelete);

}
