package interface_adapter.editTask;

import entity.CustomTag;
import interface_adapter.ViewModel;
import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EditTaskViewModel extends ViewModel<EditTaskState> {

    private final CustomTagDataAccessInterface tagDao;
    private String username;

    public EditTaskViewModel(String viewName, CustomTagDataAccessInterface tagDao) {
        super(viewName);
        this.tagDao = tagDao;
        setState(new EditTaskState());
    }

    @Override
    public void setState(EditTaskState state) {
        super.setState(state);
        firePropertyChanged("state");
    }

    public void setUsername(String username) {
        this.username = username;
        refreshTags();
    }

    public void refreshTags() {
        firePropertyChange("refreshTagOptions", null, getTagOptions());
    }

    public List<Object> getTagOptions() {
        Map<String,String> raw = tagDao.getCustomTags(username);
        List<Object> tags = raw.entrySet().stream()
                .map(e -> new CustomTag(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        tags.add("Create New Tag...");
        return tags;
    }
}
