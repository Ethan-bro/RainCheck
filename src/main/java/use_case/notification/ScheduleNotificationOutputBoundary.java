package use_case.notification;

/**
 * Output boundary for notification scheduling use case.
 */
public interface ScheduleNotificationOutputBoundary {
    void presentScheduleResult(ScheduleNotificationOutputData outputData);
}