package interface_adapter.addTask;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import use_case.addTask.AddTaskInputBoundary;
import use_case.addTask.AddTaskInputData;
import use_case.addTask.AddTaskInteractor;
import use_case.createCustomTag.CustomTagDataAccessInterface;
import use_case.createCustomTag.customTagDataAccessInterface;
import view.ViewManager;

import java.time.LocalDateTime;

public class AddTaskController {

    private final AddTaskInputBoundary addTaskInteractor;
    private final CustomTagDataAccessInterface tagDao;
    private final String username;
    private final ViewManagerModel viewManagerModel;

    public AddTaskController(String username, AddTaskInputBoundary addTaskInteractor,
                             CustomTagDataAccessInterface tagDao, ViewManagerModel viewManagerModel) {
        this.addTaskInteractor = addTaskInteractor;
        this.username = username;
        this.tagDao = tagDao;
        this.viewManagerModel = viewManagerModel;
    }

    public void execute(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                        Priority priority, CustomTag customTag, Reminder reminder){

        AddTaskInputData inputData = new AddTaskInputData(taskName, startDateTime, endDateTime,
                priority, customTag, reminder);
        addTaskInteractor.execute(inputData, username);
    }

    public void createCustomTag(){
        viewManagerModel.setState("createCustomTag");
    }
}
