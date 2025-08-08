package use_case.weather;

import entity.WeatherInfo;

import java.time.LocalDateTime;

/**
 * Interface defining methods to retrieve weather information.
 */
public interface WeatherApiInterface {

    /**
     * Gets the weather information for a given date and time.
     *
     * @param dateTime the date and time to get weather info for
     * @return WeatherInfo object containing weather details
     */
    WeatherInfo getWeatherInfo(LocalDateTime dateTime);
}
