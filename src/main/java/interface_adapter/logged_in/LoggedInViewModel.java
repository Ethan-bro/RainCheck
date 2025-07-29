package interface_adapter.logged_in;

import interface_adapter.ViewModel;
import use_case.task.ListTasksInputBoundary;

import java.time.LocalDate;

/**
 * The View Model for the Logged In View.
 */
public class LoggedInViewModel extends ViewModel<LoggedInState> {
    private ListTasksInputBoundary listTasksInteractor;

    public LoggedInViewModel() {
        super("logged in");
        setState(new LoggedInState());
    }

    public void setListTasksInteractor(ListTasksInputBoundary listTasksInteractor) {
        this.listTasksInteractor = listTasksInteractor;
    }

    public void loadTasksForWeek(LocalDate startDate, LocalDate endDate) {
        listTasksInteractor.listTasks(getState().getUsername(), startDate, endDate);
    }
}