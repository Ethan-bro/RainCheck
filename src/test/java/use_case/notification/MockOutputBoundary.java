package use_case.notification;

public class MockOutputBoundary implements ScheduleNotificationOutputBoundary {

    private ScheduleNotificationOutputData lastResult;

    @Override
    public void presentScheduleResult(ScheduleNotificationOutputData outputData) {
        this.lastResult = outputData;
    }

    public ScheduleNotificationOutputData getLastResult() {
        return lastResult;
    }
}