package interface_adapter.task;

import interface_adapter.ViewModel;
import entity.Task;

/**
 * ViewModel for a single TaskBox, wrapping a TaskState and firing change events.
 */
public class TaskViewModel extends ViewModel<TaskState> {
    private Task task;

    public TaskViewModel(Task task) {
        super("task");
        this.task = task;
        TaskState st = new TaskState(task.getTaskInfo().getId().toString());
        setState(st);
        loadFromTask(task);
    }

    /**
     * Populate the TaskState from the domain Task, then fire a "state" change.
     */
    public void loadFromTask(Task task) {
        this.task = task;
        TaskState st = getState();
        st.setTitle(task.getTaskInfo().getTaskName());
        st.setStart(task.getTaskInfo().getStartDateTime());
        st.setEnd(task.getTaskInfo().getEndDateTime());
        st.setPriority(task.getTaskInfo().getPriority().name());
        st.setTag(task.getTaskInfo().getTag() != null ? task.getTaskInfo().getTag() : null);
        st.setReminderMinutes(task.getTaskInfo().getReminder() != null ?
                task.getTaskInfo().getReminder().getMinutesBefore() : null);
        st.setCompleted("Complete".equalsIgnoreCase(task.getTaskInfo().getTaskStatus()));

        st.setWeatherDescription(task.getTaskInfo().getWeatherDescription());
        st.setWeatherEmoji(task.getTaskInfo().getWeatherIconName());
        st.setTemperature(task.getTaskInfo().getTemperature());

        firePropertyChanged("state");
    }

    public Task getTask() {return task;}
}