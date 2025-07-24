package view;

import java.time.*;
import java.time.format.*;
import java.util.*;

public class CalendarData {

    public static final String[] DAYS_OF_WEEK = {
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };

    public static final String[] HOURS_OF_DAY;

    static {
        HOURS_OF_DAY = new String[24];
        for (int i = 0; i < 24; i++) {
            HOURS_OF_DAY[i] = String.format("%02d:00", i);  // e.g., "00:00", "01:00", ..., "23:00"
        }
    }

    // Dynamic calendar data
    private LocalDate today;
    private LocalDate startOfWeek;
    private List<LocalDate> weekDates;

    public CalendarData() {
        this.today = LocalDate.now();
        computeCurrentWeek();
    }

    private void computeCurrentWeek() {
        // Get the day of week index (Sunday = 0)
        DayOfWeek dayOfWeek = today.getDayOfWeek(); // MONDAY = 1, ..., SUNDAY = 7
        int shift = dayOfWeek.getValue() % 7; // Sunday becomes 0, Monday 1, etc.
        this.startOfWeek = today.minusDays(shift);

        this.weekDates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDates.add(startOfWeek.plusDays(i));
        }
    }

    public String getMonthYearLabel() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return today.format(formatter); // e.g., "July 2025"
    }

    public List<LocalDate> getWeekDates() {
        return weekDates;
    }
}