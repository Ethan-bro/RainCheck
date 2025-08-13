package interface_adapter.manageTags;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import entity.CustomTag;
import interface_adapter.ViewModel;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.createCustomTag.CustomTagDataAccessInterface;

public class ManageTagsViewModel extends ViewModel<ManageTagsState> {

    private final CustomTagDataAccessInterface tagDao;
    private String username;

    /**
     * Constructs a ManageTagsViewModel with the given tag data access object
     * and username.
     *
     * @param tagDao the data access object for custom tags
     * @param username the username associated with this view model
     */
    public ManageTagsViewModel(CustomTagDataAccessInterface tagDao, String username) {
        super("Manage Tags");
        setState(new ManageTagsState());

        this.tagDao = tagDao;
        this.username = username;
    }

    /**
     * Returns the username associated with this view model.
     *
     * @return the current username
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
     * Returns a list of CustomTag objects for the current username.
     *
     * @return a list of CustomTag representing the user's tags
     */
    public List<CustomTag> getTagOptions() {
        final Map<String, String> raw = tagDao.getCustomTags(username);
        return raw.entrySet().stream()
                .map(entry -> new CustomTag(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the emoji for a given tag name, or null if not found.
     *
     * @param tagName the tag name to look up
     * @return the emoji string, or null if not found
     */
    public String getTagEmojiFor(String tagName) {
        final Map<String, String> raw = tagDao.getCustomTags(username);
        return raw.get(tagName);
    }
}
