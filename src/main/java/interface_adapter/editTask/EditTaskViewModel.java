package interface_adapter.editTask;

import entity.CustomTag;
import interface_adapter.ViewModel;
import interface_adapter.events.TagChangeEventNotifier;
import use_case.CreateCT.CustomTagDataAccessInterface;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EditTaskViewModel extends ViewModel<EditTaskState> {

    private final CustomTagDataAccessInterface tagDao;
    private String username;

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

    public void setUsername(String username) {
        this.username = username;

        // Optionally trigger refresh immediately when username is set
        List<CustomTag> updatedTags = getTagOptions();
        firePropertyChange("refreshTagOptions", null, updatedTags);
    }

    public List<CustomTag> getTagOptions() {
        Map<String, String> raw = tagDao.getCustomTags(username);
        return raw.entrySet().stream()
                .map(e -> new CustomTag(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}
