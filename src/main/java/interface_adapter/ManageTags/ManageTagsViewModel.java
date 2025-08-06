package interface_adapter.ManageTags;

import entity.CustomTag;
import interface_adapter.ViewModel;
import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ManageTagsViewModel extends ViewModel<ManageTasksState> {

    private final CustomTagDataAccessInterface tagDao;
    private String username;

    public ManageTagsViewModel(CustomTagDataAccessInterface tagDao, String username) {
        super("Manage Tags");
        setState(new ManageTasksState());

        this.tagDao = tagDao;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        refreshTags();
    }

    public void refreshTags() {
        /*
        This was the root cause of the "Manage Tags" side button in LoggedInView hanging:
        The call to fetch tags from the data access object (tagDao.getCustomTags) is a blocking operation
        that was executed synchronously on the Swing Event Dispatch Thread (EDT).
        Since Swing UI updates must happen on the EDT, blocking it causes the UI to freeze,
        making the application unresponsive.
        To resolve this, the data fetching must be performed asynchronously off the EDT,
        and the UI update triggered afterwards on the EDT to maintain thread safety and responsiveness.
         */
        new Thread(() -> {
            List<CustomTag> tags = getTagOptions();
            javax.swing.SwingUtilities.invokeLater(() -> firePropertyChange("refreshTagOptions", null, tags));
        }).start();
    }

    public List<CustomTag> getTagOptions() {
        Map<String,String> raw = tagDao.getCustomTags(username);
        return raw.entrySet().stream()
                .map(e -> new CustomTag(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public String getTagEmojiFor(String tagName) {
        Map<String,String> raw = tagDao.getCustomTags(username);
        return raw.getOrDefault(tagName, "");
    }
}
