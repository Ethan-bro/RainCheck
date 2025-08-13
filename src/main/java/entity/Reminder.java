package entity;

/**
 * Represents a reminder for a task specifying how many minutes before the event
 * the reminder occurs.
 */
public class Reminder {

    public static final Reminder NONE = new Reminder(-1);

    private static final int REMINDER_AT_EVENT = 0;
    private static final int REMINDER_10_MIN = 10;
    private static final int REMINDER_30_MIN = 30;
    private static final int REMINDER_1_HOUR = 60;
    private static final int REMINDER_1_DAY = 1440;

    private final int minutesBefore;

    /**
     * Constructs a Reminder.
     *
     * @param minutesBefore the number of minutes before the event to remind, -1
     * means no reminder. Must be >= -1.
     * @throws IllegalArgumentException if minutesBefore < -1
     */
    public Reminder(int minutesBefore) {
        if (minutesBefore < -1) {
            throw new IllegalArgumentException("Reminder must be >= -1");
        }
        this.minutesBefore = minutesBefore;
    }

    /**
     * Gets the number of minutes before the event for the reminder.
     *
     * @return the minutes before the event
     */
    public int getMinutesBefore() {
        return minutesBefore;
    }

    /**
     * Returns a string representation of the reminder.
     *
     * @return a string describing the reminder time
     */
    @Override
    public String toString() {
        return switch (minutesBefore) {
            case -1 ->
                "No reminder";
            case REMINDER_AT_EVENT ->
                "At event (0 minutes)";
            case REMINDER_10_MIN ->
                "10 minutes";
            case REMINDER_30_MIN ->
                "30 minutes";
            case REMINDER_1_HOUR ->
                "1 hour";
            case REMINDER_1_DAY ->
                "1 day";
            default ->
                minutesBefore + " min before";
        };
    }

    /**
     * Checks equality with another object.
     *
     * @param other the object to compare to
     * @return true if other is a Reminder with the same minutesBefore value
     */
    @Override
    public boolean equals(Object other) {
        final boolean result;
        if (!(other instanceof Reminder reminder)) {
            result = false;
        } else {
            result = reminder.minutesBefore == this.minutesBefore;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(minutesBefore);
    }
}
