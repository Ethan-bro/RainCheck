package entity;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private TaskInfo taskInfo;

    public Task(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(TaskInfo taskInfo) {this.taskInfo = taskInfo;}

    public String getReminderMessage() {
        int minutesBefore = taskInfo.getReminder().getMinutesBefore();
        String message;

        if (minutesBefore == 0) {
            message = "now";
        } else {
            int days = minutesBefore / 1440;
            int hours = (minutesBefore % 1440) / 60;
            int minutes = minutesBefore % 60;

            List<String> parts = new ArrayList<>();
            if (days > 0) parts.add(days + " day" + (days > 1 ? "s" : ""));
            if (hours > 0) parts.add(hours + " hour" + (hours > 1 ? "s" : ""));
            if (minutes > 0) parts.add(minutes + " minute" + (minutes > 1 ? "s" : ""));

            message = "in " + String.join(" and ", parts);
        }

        return String.format("Task '%s' is due %s", taskInfo.getTaskName(), message);
    }

}
