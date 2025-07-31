package interface_adapter.addTask;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;
import interface_adapter.ViewManagerModel;
import use_case.addTask.AddTaskInputBoundary;
import use_case.addTask.AddTaskInputData;

import java.time.LocalDateTime;

public class AddTaskController {

    private final AddTaskInputBoundary addTaskInteractor;
    private final String username;
    private final ViewManagerModel viewManagerModel;

    public AddTaskController(String username, AddTaskInputBoundary addTaskInteractor, ViewManagerModel viewManagerModel) {
        this.addTaskInteractor = addTaskInteractor;
        this.username = username;
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
