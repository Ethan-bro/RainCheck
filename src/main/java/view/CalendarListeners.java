package view;

import interface_adapter.calendar.TaskClickListener;

import java.awt.event.ActionListener;

public class CalendarListeners {
    public final TaskClickListener taskClickListener;
    public final ActionListener addTaskListener;
    public final ActionListener manageTagsListener;
    public final ActionListener logoutListener;

    public CalendarListeners(TaskClickListener taskClickListener,
                             ActionListener addTaskListener,
                             ActionListener manageTagsListener,
                             ActionListener logoutListener) {
        this.taskClickListener = taskClickListener;
        this.addTaskListener = addTaskListener;
        this.manageTagsListener = manageTagsListener;
        this.logoutListener = logoutListener;
    }

    protected TaskClickListener getTaskClickListener() {
        return taskClickListener;
    }

    protected ActionListener getAddTaskListener() {
        return addTaskListener;
    }

    protected ActionListener getManageTagsListener() {
        return manageTagsListener;
    }

    protected ActionListener getLogoutListener() {
        return logoutListener;
    }
}
