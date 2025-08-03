package interface_adapter.logged_in;

import interface_adapter.ViewModel;
import use_case.listTasks.ListTasksInputBoundary;

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

    public void loadTasksForWeek(LocalDate start, LocalDate end) {
        if (listTasksInteractor == null) {
            throw new IllegalStateException("ListTasksInteractor not set!");
        }
        // This will fetch tasks → call the presenter → update state & fire weekTasks
        listTasksInteractor.listTasks(getState().getUsername(), start, end);
    }
}