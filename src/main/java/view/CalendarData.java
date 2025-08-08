package view;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds data for the calendar display, including days of the week, hours of the day,
 * and the current week dates.
 */
public class CalendarData {

    /** Days of the week labels. */
    public static final String[] DAYS_OF_WEEK = {
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
    };

    /** Hours of the day labels in HH:00 format. */
    public static final String[] HOURS_OF_DAY;

    /** Constant for total hours in a day. */
    private static final int HOURS_IN_DAY = 24;

    /** Constant for total days in a week. */
    private static final int DAYS_IN_WEEK = 7;

    // Dynamic calendar data
    private LocalDate today;
    private LocalDate startOfWeek;
    private List<LocalDate> weekDates;

    static {
        HOURS_OF_DAY = new String[HOURS_IN_DAY];
        for (int i = 0; i < HOURS_IN_DAY; i++) {
            HOURS_OF_DAY[i] = String.format("%02d:00", i);
        }
    }

    /**
     * Creates a new CalendarData instance set to today's date.
     */
    public CalendarData() {
        this.today = LocalDate.now();
        computeCurrentWeek();
    }

    /**
     * Computes the current week's start date (Sunday) and populates the list of week dates.
     */
    private void computeCurrentWeek() {
        final DayOfWeek dayOfWeek = today.getDayOfWeek();
        final int shift = dayOfWeek.getValue() % DAYS_IN_WEEK;
        this.startOfWeek = today.minusDays(shift);

        this.weekDates = new ArrayList<>();
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            weekDates.add(startOfWeek.plusDays(i));
        }
    }

    /**
     * Gets the list of dates for the current week.
     *
     * @return a list of LocalDate objects representing each day of the week
     */
    public List<LocalDate> getWeekDates() {
        return weekDates;
    }
}
