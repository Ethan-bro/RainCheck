package interface_adapter.manageTags;

import entity.CustomTag;

import interface_adapter.ViewModel;
import interface_adapter.events.TagChangeEventNotifier;

import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ManageTagsViewModel extends ViewModel<ManageTagsState> {

    private final CustomTagDataAccessInterface tagDao;
    private String username;

    public ManageTagsViewModel(CustomTagDataAccessInterface tagDao, String username) {
        super("Manage Tags");
        setState(new ManageTagsState());

        this.tagDao = tagDao;
        this.username = username;
    }

    /**
     * Returns the username associated with this ViewModel.
     *
     * @return the current username as a String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username and fires a global tag change event.
     *
     * @param username the new username to set
     */
    public void setUsername(String username) {
        this.username = username;

        // Notify listeners that tags have changed (forces UI to re-pull from getTagOptions)
        TagChangeEventNotifier.fire();
    }

    /**
     * Gets a list of CustomTag objects for the current username.
     *
     * @return a List of CustomTag representing the user's tags
     */
    public List<CustomTag> getTagOptions() {
        final Map<String, String> raw = tagDao.getCustomTags(username);
        return raw.entrySet().stream()
                .map(entry -> new CustomTag(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Gets the emoji associated with a given tag name for the current user.
     *
     * @param tagName the name of the tag
     * @return the emoji string associated with the tag, or empty string if none
     */
    public String getTagEmojiFor(String tagName) {
        final Map<String, String> raw = tagDao.getCustomTags(username);
        return raw.getOrDefault(tagName, "");
    }
}
