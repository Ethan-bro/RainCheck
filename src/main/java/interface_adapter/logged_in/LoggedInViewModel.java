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

    /**
     * Sets the interactor responsible for listing tasks.
     *
     * @param listTasksInteractor the interactor to set
     */
    public void setListTasksInteractor(final ListTasksInputBoundary listTasksInteractor) {
        this.listTasksInteractor = listTasksInteractor;
    }

    /**
     * Loads tasks for the specified week defined by start and end dates.
     *
     * @param startDate the start date of the week
     * @param endDate the end date of the week
     */
    public void loadTasksForWeek(final LocalDate startDate, final LocalDate endDate) {
        listTasksInteractor.listTasks(getState().getUsername(), startDate, endDate);
    }
}
