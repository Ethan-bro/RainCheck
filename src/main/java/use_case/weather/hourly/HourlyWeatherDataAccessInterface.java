package use_case.weather.hourly;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HourlyWeatherDataAccessInterface {
    List<Map<String, String>> getHourlyWeather(String location, LocalDate date, int startHour, int endHour) throws IOException;
}
