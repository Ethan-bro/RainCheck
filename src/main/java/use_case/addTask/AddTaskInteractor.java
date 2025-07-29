package use_case.addTask;

import data_access.SupabaseTaskDataAccessObject;
import entity.Task;
import entity.TaskID;
import entity.TaskInfo;
import interface_adapter.addTask.Constants;
import interface_adapter.addTask.TaskIDGenerator;

public class AddTaskInteractor implements AddTaskInputBoundary {

    private final SupabaseTaskDataAccessObject dao;
    private final AddTaskOutputBoundary addTaskPresenter;
    private final TaskIDGenerator taskIDGenerator;

    public AddTaskInteractor(SupabaseTaskDataAccessObject dao, TaskIDGenerator taskIDGenerator,
                             AddTaskOutputBoundary addTaskPresenter) {
        this.dao = dao;
        this.taskIDGenerator = taskIDGenerator;
        this.addTaskPresenter = addTaskPresenter;
    }

    @Override
    public void execute(AddTaskInputData inputData, String username){
        if (inputData.getTaskName() == null || inputData.getTaskName().isEmpty()){
            AddTaskOutputData failedOutput = new AddTaskOutputData(AddTaskError.EMPTY_NAME);
            addTaskPresenter.prepareFailView(failedOutput);
            return;
        }

        if (inputData.getTaskName().length() > Constants.CHAR_LIMIT){
            AddTaskOutputData failedOutput = new AddTaskOutputData(AddTaskError.EXCEEDS_CHAR_LIM);
            addTaskPresenter.prepareFailView(failedOutput);
            return;
        }

        if (inputData.getStartDateTime() == null || inputData.getEndDateTime() == null){
            AddTaskOutputData failedOutput = new AddTaskOutputData(AddTaskError.EMPTY_TIME);
            addTaskPresenter.prepareFailView(failedOutput);
            return;
        }

        if (!inputData.getEndDateTime().isAfter(inputData.getStartDateTime())){
            AddTaskOutputData failedOutput = new AddTaskOutputData(AddTaskError.START_AFTER_END);
            addTaskPresenter.prepareFailView(failedOutput);
            return;
        }

        TaskID newID = taskIDGenerator.generateTaskID();

        TaskInfo newTaskInfo = new TaskInfo(newID, inputData.getTaskName(), inputData.getStartDateTime(),
                inputData.getEndDateTime(), inputData.getPriority(), inputData.getCustomTag(),
                inputData.getReminder()
                );

        Task newTask = new Task(newTaskInfo);

        dao.addTask(username, newTask);

        AddTaskOutputData outputData = new AddTaskOutputData(newTask);
        addTaskPresenter.prepareSuccessView(outputData);
    }
}
