package interface_adapter.editTask;

import entity.CustomTag;

import interface_adapter.ViewModel;
import interface_adapter.events.TagChangeEventNotifier;

import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EditTaskViewModel extends ViewModel<EditTaskState> {

    private final CustomTagDataAccessInterface tagDao;
    private String username;

    /**
     * Constructs an EditTaskViewModel with the given tag data access object and username.
     *
     * @param tagDao the data access interface for custom tags
     * @param username the current username
     */
    public EditTaskViewModel(CustomTagDataAccessInterface tagDao, String username) {
        super("Edit Task");
        this.tagDao = tagDao;
        this.username = username;
        setState(new EditTaskState());

        // Subscribe to tag change events
        TagChangeEventNotifier.subscribe(() -> {
            final List<CustomTag> updatedTags = getTagOptions();
            firePropertyChange("refreshTagOptions", null, updatedTags);
        });
    }

    /**
     * Sets the state of the view model and fires a property change event.
     *
     * @param state the new state to set
     */
    @Override
    public void setState(EditTaskState state) {
        super.setState(state);
        firePropertyChanged("state");
    }

    /**
     * Sets the username and refreshes tag options immediately.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
        final List<CustomTag> updatedTags = getTagOptions();
        firePropertyChange("refreshTagOptions", null, updatedTags);
    }

    /**
     * Returns the list of available custom tags for the current user.
     *
     * @return list of CustomTag objects
     */
    public List<CustomTag> getTagOptions() {
        final Map<String, String> raw = tagDao.getCustomTags(username);
        return raw.entrySet().stream()
                .map(entry -> new CustomTag(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
