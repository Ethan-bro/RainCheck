package use_case.weather.daily;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public interface DailyWeatherDataAccessInterface {
    Map<String, Object> getDailyWeather(String location, LocalDate date) throws IOException;
}
