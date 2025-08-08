package interface_adapter.editTask;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import entity.CustomTag;
import interface_adapter.ViewModel;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.createCustomTag.CustomTagDataAccessInterface;

/**
 * View model for the edit task view.
 */
public class EditTaskViewModel extends ViewModel<EditTaskState> {

    private final CustomTagDataAccessInterface tagDao;
    private String username;

    /**
     * Constructs the view model.
     * @param tagDao the custom tag data access interface
     * @param username the username associated with the view
     */
    public EditTaskViewModel(CustomTagDataAccessInterface tagDao, String username) {
        super("Edit Task");
        this.tagDao = tagDao;
        this.username = username;
        setState(new EditTaskState());

        // Subscribe to tag change events
        TagChangeEventNotifier.subscribe(() -> {
            List<CustomTag> updatedTags = getTagOptions();
            firePropertyChange("refreshTagOptions", null, updatedTags);
        });
    }

    @Override
    public void setState(EditTaskState state) {
        super.setState(state);
        firePropertyChanged("state");
    }

    /**
     * Sets the username for this view model and refreshes the tag options.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;

        // Optionally trigger refresh immediately when username is set
        List<CustomTag> updatedTags = getTagOptions();
        firePropertyChange("refreshTagOptions", null, updatedTags);
    }

    /**
     * Returns the list of custom tag options for the current user.
     * @return a list of custom tags
     */
    public List<CustomTag> getTagOptions() {
        Map<String, String> raw = tagDao.getCustomTags(username);
        return raw.entrySet().stream()
                .map(event -> new CustomTag(event.getKey(), event.getValue()))
                .collect(Collectors.toList());
    }
}
