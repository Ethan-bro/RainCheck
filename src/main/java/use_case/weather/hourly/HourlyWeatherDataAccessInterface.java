package use_case.weather.hourly;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Data access interface for retrieving hourly weather data.
 */
public interface HourlyWeatherDataAccessInterface {

    /**
     * Retrieves hourly weather data for a specific location, date, and time range.
     *
     * @param location  the name of the location (e.g., city name)
     * @param date      the date for which to retrieve the weather
     * @param startHour the starting hour (0–23) for the weather data
     * @param endHour   the ending hour (0–23) for the weather data
     * @return a list of maps, where each map contains key-value pairs of hourly weather data
     * @throws IOException if there is an error retrieving the weather data
     */
    List<Map<String, String>> getHourlyWeather(
            String location,
            LocalDate date,
            int startHour,
            int endHour
    ) throws IOException;
}
