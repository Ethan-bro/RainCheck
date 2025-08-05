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
        firePropertyChange("refreshTagOptions", null, getTagOptions());
    }

    public List<Object> getTagOptions() {
        Map<String,String> raw = tagDao.getCustomTags(username);
        return raw.entrySet().stream()
                .map(e -> new CustomTag(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}
