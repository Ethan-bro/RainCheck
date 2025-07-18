package use_case.weather.daily;

import java.time.LocalDate;

public class DailyWeatherInputData {
    private final String location;
    private final LocalDate date;

    public DailyWeatherInputData(String location, LocalDate date) {
        this.location = location;
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getDate() {
        return date;
    }
}
