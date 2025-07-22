package use_case.weather.daily;

/**
 * Input boundary for fetching daily weather data.
 */
public interface DailyWeatherInputBoundary {
    void fetchDailyWeather(DailyWeatherInputData inputData);
}
