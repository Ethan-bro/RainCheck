package interface_adapter.calendar;

import entity.Task;

/**
 * Listener interface for handling task click events in the calendar.
 */
public interface TaskClickListener {

    /**
     * Called when a task is clicked.
     *
     * @param task the task that was clicked
     */
    void onTaskClick(Task task);
}
