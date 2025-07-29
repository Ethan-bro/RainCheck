package interface_adapter.addTask;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;
import interface_adapter.logged_in.LoggedInState;
import use_case.addTask.AddTaskInputBoundary;
import use_case.addTask.AddTaskInputData;
import use_case.addTask.AddTaskInteractor;

import java.time.LocalDateTime;

public class AddTaskController {

    private final AddTaskInputBoundary addTaskInteractor;
    private final String username;

    public AddTaskController(String username, AddTaskInputBoundary addTaskInteractor) {
        this.addTaskInteractor = addTaskInteractor;
        this.username = username;
    }

    public void execute(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime,
                        Priority priority, CustomTag customTag, Reminder reminder){

        AddTaskInputData inputData = new AddTaskInputData(taskName, startDateTime, endDateTime,
                priority, customTag, reminder);
        addTaskInteractor.execute(inputData, username);
    }
}
