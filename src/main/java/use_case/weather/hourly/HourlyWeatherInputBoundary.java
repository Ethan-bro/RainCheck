package use_case.weather.hourly;

/**
 * Input boundary for fetching hourly weather data.
 */
public interface HourlyWeatherInputBoundary {
    void fetchHourlyWeather(HourlyWeatherInputData inputData);
}
