package interface_adapter.manageTags;

import entity.CustomTag;
import interface_adapter.ViewModel;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.createCustomTag.customTagDataAccessInterface;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class manageTagsViewModel extends ViewModel<manageTagsState> {

    private final customTagDataAccessInterface tagDao;
    private String username;

    public manageTagsViewModel(customTagDataAccessInterface tagDao, String username) {
        super("Manage Tags");
        setState(new manageTagsState());

        this.tagDao = tagDao;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        System.out.println("[manageTagsViewModel] setUsername called with: " + username);
        this.username = username;

        // Notify listeners that tags have changed (forces UI to re-pull from getTagOptions)
        TagChangeEventNotifier.fire();
    }

    public List<CustomTag> getTagOptions() {
        Map<String, String> raw = tagDao.getCustomTags(username);
        return raw.entrySet().stream()
                .map(e -> new CustomTag(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public String getTagEmojiFor(String tagName) {
        Map<String, String> raw = tagDao.getCustomTags(username);
        return raw.getOrDefault(tagName, "");
    }
}
