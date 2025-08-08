package interface_adapter.addTask;

import entity.CustomTag;

import interface_adapter.ViewModel;
import interface_adapter.events.TagChangeEventNotifier;

import use_case.createCustomTag.customTagDataAccessInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ViewModel for adding a new task.
 * Manages the state, username, and available custom tags.
 */
public class AddTaskViewModel extends ViewModel<AddTaskState> {

    private final customTagDataAccessInterface tagDao;
    private String username;

    // Listener for global tag changes
    private final PropertyChangeListener tagChangeListener = this::onGlobalTagsChanged;

    /**
     * Constructs an AddTaskViewModel with the given tag DAO and username.
     *
     * @param tagDao   the data access object for custom tags
     * @param username the username of the logged-in user
     */
    public AddTaskViewModel(customTagDataAccessInterface tagDao, String username) {
        super("Add New Task");
        this.tagDao = tagDao;
        setState(new AddTaskState());

        setUsername(username);

        // Subscribe to global tag change events
        TagChangeEventNotifier.addListener(tagChangeListener);
    }

    /**
     * Sets the username and refreshes available tags.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
        refreshTags();
    }

    /**
     * Gets the current username.
     *
     * @return the current username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Refreshes the available tags and updates the view.
     */
    public void refreshTags() {
        final List<CustomTag> updatedTags = getTagOptions();
        getState().setTagOptions(updatedTags);
        firePropertyChange("refreshTagOptions", null, updatedTags);
    }

    /**
     * Retrieves the available tag options for the current user.
     *
     * @return a list of {@link CustomTag} objects
     */
    public List<CustomTag> getTagOptions() {
        final Map<String, String> raw = tagDao.getCustomTags(username);
        return raw.entrySet().stream()
                .map(entry -> new CustomTag(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Handles the global tags changed event by refreshing tags.
     *
     * @param event the property change event
     */
    private void onGlobalTagsChanged(PropertyChangeEvent event) {
        refreshTags();
    }

    /**
     * Removes the tag change listener to avoid memory leaks when this ViewModel is no longer needed.
     */
    public void dispose() {
        TagChangeEventNotifier.removeListener(tagChangeListener);
    }

    /**
     * Gets the current state.
     *
     * @return the current {@link AddTaskState}
     */
    @Override
    public AddTaskState getState() {
        return super.getState();
    }
}
