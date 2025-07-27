package use_case.MarkTaskComplete;

import entity.Task;
import com.google.gson.JsonObject;

import java.io.IOException;

public class MarkTaskCompleteInteractor implements MarkTaskCompleteInputBoundary {

    private final MarkTaskCompleteDataAccessInterface dataAccess;
    private final MarkTaskCompleteOutputBoundary presenter;

    public MarkTaskCompleteInteractor(MarkTaskCompleteDataAccessInterface dataAccess,
                                      MarkTaskCompleteOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(MarkTaskCompleteInputData inputData) throws IOException {
        // Retrieve the task by username and task ID
        Task task = dataAccess.getTaskById(inputData.getUsername(), inputData.getTaskId());

        if (task == null) {
            presenter.prepareFailView("Task not found.");
            return;
        }

        // Mark task as complete
        task.setCompleted(true);

        // Update the task in the data source
        dataAccess.updateUsersTasks(inputData.getUsername(), task);

        // Return a success view
        MarkTaskCompleteOutputData outputData = new MarkTaskCompleteOutputData(
                task.getId(),
                false  // useCaseFailed = false (meaning success)
        );
        presenter.prepareSuccessView(outputData);
    }}
