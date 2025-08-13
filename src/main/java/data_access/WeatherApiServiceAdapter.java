package data_access;

import entity.WeatherInfo;

import use_case.weather.WeatherApiInterface;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Adapter implementing WeatherApiInterface using WeatherApiService and LocationService.
 */
public class WeatherApiServiceAdapter implements WeatherApiInterface {

    private static final String DEFAULT_ICON = "clear-day";
    private static final String DEFAULT_TEMP = "25.0";
    private static final String DEFAULT_UV_INDEX = "0";

    private final WeatherApiService weatherApiService;

    /**
     * Constructs the adapter with a WeatherApiService instance.
     *
     * @param weatherApiService the WeatherApiService to delegate weather fetching to
     */
    public WeatherApiServiceAdapter(WeatherApiService weatherApiService) {
        this.weatherApiService = weatherApiService;
    }

    /**
     * Gets weather information for the specified date and time using the user's city.
     *
     * @param dateTime the date and time for which to fetch weather info
     * @return a WeatherInfo object containing weather details
     */
    @Override
    public WeatherInfo getWeatherInfo(LocalDateTime dateTime) {
        final String location = LocationService.getUserCity();
        WeatherInfo resultWeatherInfo;

        try {
            final Map<String, Object> dailyData = weatherApiService.getDailyWeather(location, dateTime.toLocalDate());

            final String description = dailyData.getOrDefault("iconName", DEFAULT_ICON).toString();
            final String temp = dailyData.getOrDefault("tempmax", DEFAULT_TEMP).toString();
            final String iconName = dailyData.getOrDefault("iconName", DEFAULT_ICON).toString();

            String uvIndex = DEFAULT_UV_INDEX;
            if (dailyData.containsKey("uvindex")) {
                uvIndex = dailyData.get("uvindex").toString();
            }

            resultWeatherInfo = new WeatherInfo(description, iconName, temp, uvIndex);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            // Return default weather info on failure
            resultWeatherInfo = new WeatherInfo(DEFAULT_ICON, DEFAULT_ICON, DEFAULT_TEMP, DEFAULT_UV_INDEX);
        }

        return resultWeatherInfo;
    }
}
