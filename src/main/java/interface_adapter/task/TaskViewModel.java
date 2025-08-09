package interface_adapter.task;

import entity.CustomTag;
import entity.Task;

import interface_adapter.ViewModel;

/**
 * ViewModel for a single TaskBox, wrapping a TaskState and firing change events.
 */
public class TaskViewModel extends ViewModel<TaskState> {
    private Task task;

    /**
     * Constructs TaskViewModel wrapping the given Task.
     *
     * @param task the domain task to wrap
     */
    public TaskViewModel(Task task) {
        super("task");
        this.task = task;
        final TaskState st = new TaskState(task.getTaskInfo().getId().toString());
        setState(st);
        loadFromTask(task);
    }

    /**
     * Populate the TaskState from the domain Task, then fire a "state" change.
     *
     * @param updatedTask the domain task to load from
     */
    public void loadFromTask(Task updatedTask) {
        this.task = updatedTask;
        final TaskState st = getState();

        st.setTitle(updatedTask.getTaskInfo().getTaskName());
        st.setStart(updatedTask.getTaskInfo().getStartDateTime());
        st.setEnd(updatedTask.getTaskInfo().getEndDateTime());
        st.setPriority(updatedTask.getTaskInfo().getPriority().name());

        CustomTag tag = null;
        if (updatedTask.getTaskInfo().getTag() != null) {
            tag = updatedTask.getTaskInfo().getTag();
        }
        st.setTag(tag);

        Integer reminderMinutes = null;
        if (updatedTask.getTaskInfo().getReminder() != null) {
            reminderMinutes = updatedTask.getTaskInfo().getReminder().getMinutesBefore();
        }
        st.setReminderMinutes(reminderMinutes);

        boolean isComplete = false;
        if ("Complete".equalsIgnoreCase(updatedTask.getTaskInfo().getTaskStatus())) {
            isComplete = true;
        }
        st.setCompleted(isComplete);

        st.setWeatherDescription(updatedTask.getTaskInfo().getWeatherDescription());
        st.setWeatherEmoji(updatedTask.getTaskInfo().getWeatherIconName());
        st.setTemperature(updatedTask.getTaskInfo().getTemperature());

        firePropertyChanged("state");
    }

    /**
     * Returns the underlying Task.
     *
     * @return the wrapped task
     */
    public Task getTask() {
        return task;
    }
}
