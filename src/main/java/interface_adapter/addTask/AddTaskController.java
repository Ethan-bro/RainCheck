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
    private final ViewManagerModel viewManagerModel;
    private final AddTaskViewModel addTaskViewModel;

    public AddTaskController(AddTaskInputBoundary addTaskInteractor,
                             ViewManagerModel viewManagerModel,
                             AddTaskViewModel addTaskViewModel) {
        this.addTaskInteractor = addTaskInteractor;
        this.viewManagerModel = viewManagerModel;
        this.addTaskViewModel = addTaskViewModel;
    }

    public void execute(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                        Priority priority, CustomTag customTag, Reminder reminder) {

        String username = addTaskViewModel.getUsername();

        if (username == null) {
            System.err.println("AddTaskController Error: Username is null. Aborting task creation.");
            return;
        }

        AddTaskInputData inputData = new AddTaskInputData(taskName, startDateTime, endDateTime,
                priority, customTag, reminder);
        addTaskInteractor.execute(inputData, username);
    }

    public void createCustomTag() {
        viewManagerModel.setState("createCustomTag");
    }
}
