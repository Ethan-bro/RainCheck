package use_case.weather.daily;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Interface for accessing daily weather data from a data source.
 */
public interface DailyWeatherDataAccessInterface {

    /**
     * Retrieves daily weather information for a given location and date.
     *
     * @param location the name of the location (e.g., city)
     * @param date     the date for which to retrieve weather data
     * @return a map containing daily weather details such as icon name, temperature, etc.
     * @throws IOException if there is an error retrieving the data
     */
    Map<String, Object> getDailyWeather(String location, LocalDate date) throws IOException;
}
