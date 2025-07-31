package entity;

public class Reminder {
    public static final Reminder NONE = new Reminder(-1);

    private final int minutesBefore;

    public Reminder(int minutesBefore) {
        if (minutesBefore < -1) throw new IllegalArgumentException("Reminder must be >= -1");
        this.minutesBefore = minutesBefore;
    }

    public int getMinutesBefore() {return minutesBefore;}

    @Override
    public String toString() {
        return switch (minutesBefore) {
            case -1 -> "No reminder";
            case 0 -> "At event (0 minutes)";
            case 10 -> "10 minutes";
            case 30 -> "30 minutes";
            case 60 -> "1 hour";
            case 1440 -> "1 day";
            default -> minutesBefore + " min before";
        };
    }

    public boolean equals(Object o) {
        return o instanceof Reminder r && r.minutesBefore == minutesBefore;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(minutesBefore);
    }

}
