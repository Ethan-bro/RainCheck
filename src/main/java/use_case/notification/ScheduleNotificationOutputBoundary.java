package use_case.notification;

/**
 * Output boundary for notification scheduling use case.
 * Defines the method to present the result of scheduling a notification.
 */
public interface ScheduleNotificationOutputBoundary {

    /**
     * Presents the result of the notification scheduling attempt.
     *
     * @param outputData the data containing scheduling result details
     */
    void presentScheduleResult(ScheduleNotificationOutputData outputData);
}
