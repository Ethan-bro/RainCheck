package interface_adapter.addTask;

import entity.CustomTag;
import interface_adapter.ViewModel;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddTaskViewModel extends ViewModel<AddTaskState> {

    private final CustomTagDataAccessInterface tagDao;
    private String username;

    // Listener for global tag changes
    private final PropertyChangeListener tagChangeListener = this::onGlobalTagsChanged;

    public AddTaskViewModel(CustomTagDataAccessInterface tagDao, String username) {
        super("Add New Task");
        this.tagDao = tagDao;
        setState(new AddTaskState());

        setUsername(username);

        // Subscribe to global tag change events
        TagChangeEventNotifier.addListener(tagChangeListener);
    }

    public void setUsername(String username) {
        this.username = username;
        refreshTags();
    }

    public String getUsername() {
        return username;
    }

    public void refreshTags() {
        List<CustomTag> updatedTags = getTagOptions();
        getState().setTagOptions(updatedTags);
        firePropertyChange("refreshTagOptions", null, updatedTags);
    }

    public List<CustomTag> getTagOptions() {
        Map<String, String> raw = tagDao.getCustomTags(username);
        return raw.entrySet().stream()
                .map(e -> new CustomTag(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private void onGlobalTagsChanged(PropertyChangeEvent evt) {
        refreshTags();
    }

    /**
     * Call this method to remove the listener when this ViewModel is no longer needed
     * to avoid memory leaks.
     */
    public void dispose() {
        TagChangeEventNotifier.removeListener(tagChangeListener);
    }

    @Override
    public AddTaskState getState() {
        return super.getState();
    }
}
