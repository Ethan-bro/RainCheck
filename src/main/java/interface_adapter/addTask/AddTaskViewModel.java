package interface_adapter.addTask;

import entity.CustomTag;
import interface_adapter.ViewModel;
import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddTaskViewModel extends ViewModel<AddTaskState> {
    private final CustomTagDataAccessInterface tagDao;
    private String username;

    public AddTaskViewModel(CustomTagDataAccessInterface tagDao, String username) {
        super("Add New Task");
        this.tagDao = tagDao;
        setUsername(username);
        setState(new AddTaskState());
    }

    public void setUsername(String username) {
        this.username = username;
        refreshTags();
    }

    public String getUsername() {
        return username;
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

    @Override
    public AddTaskState getState() {
        return super.getState();
    }

}
