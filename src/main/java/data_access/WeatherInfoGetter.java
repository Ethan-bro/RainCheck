package data_access;

import entity.WeatherInfo;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class WeatherInfoGetter {

    private final WeatherApiService weatherApiService;
    private final String userCity;

    public WeatherInfoGetter(WeatherApiService weatherApiService, String userCity) {
        this.weatherApiService = weatherApiService;
        this.userCity = userCity;
    }

    public WeatherInfo getWeatherInfo(LocalDateTime dateTime) {
        String description = "";
        String feels = "";
        String iconName = "";
        String temperature = "";

        try {
            LocalDate date = dateTime.toLocalDate();
            int hour = dateTime.getHour();

            List<Map<String,String>> hourly = weatherApiService.getHourlyWeather(userCity, date, hour, hour);

            if (!hourly.isEmpty()){
                Map<String, String> hourlyMap = hourly.get(0);
                description = hourlyMap.getOrDefault("description", "");
                feels = hourlyMap.getOrDefault("feels", "");

                Map<String, Object> daily = weatherApiService.getDailyWeather(userCity, date);
                iconName = daily.getOrDefault("iconName", "").toString();
            }
        } catch (IOException e) {
            System.err.println("Weather Lookup Failed: " + e.getMessage());
        }

        temperature = feels;

        return new WeatherInfo(description, feels, iconName, temperature);
    }
}
