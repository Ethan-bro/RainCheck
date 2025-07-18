package use_case.weather.hourly;

import java.time.LocalDate;

public class HourlyWeatherInputData {
    private final String location;
    private final LocalDate date;
    private final int startHour;
    private final int endHour;

    public HourlyWeatherInputData(String location, LocalDate date, int startHour, int endHour) {
        this.location = location;
        this.date = date;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }
}
