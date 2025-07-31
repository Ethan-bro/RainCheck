package interface_adapter.addTask;

import entity.CustomTag;
import interface_adapter.ViewModel;
import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddTaskViewModel extends ViewModel<AddTaskState> {
    private final CustomTagDataAccessInterface tagDao;
    private final String username;

    public AddTaskViewModel(CustomTagDataAccessInterface tagDao, String username) {
        super("Add New Task");
        this.tagDao = tagDao;
        this.username = username;
        setState(new AddTaskState());
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
