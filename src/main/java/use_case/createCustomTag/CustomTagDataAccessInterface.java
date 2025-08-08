package use_case.createCustomTag;

import entity.CustomTag;

import java.util.Map;

public interface customTagDataAccessInterface {

    Map<String, String> getCustomTags(String username);

    void addCustomTag(String username, CustomTag tagToAdd);

    void deleteCustomTag(String username, CustomTag tagToDelete);

}
