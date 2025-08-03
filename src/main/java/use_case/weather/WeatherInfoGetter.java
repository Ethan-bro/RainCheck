package use_case.weather;

import data_access.LocationService;
import data_access.WeatherApiService;
import entity.WeatherInfo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class WeatherInfoGetter {

    private WeatherInfoGetter() {}

    public static WeatherInfo getWeatherInfo(WeatherApiService weatherApiService, LocalDateTime startDateTime) {
        String description = "";
        String feels = "";
        String iconName = "";
        try {
            LocalDate date = startDateTime.toLocalDate();
            int hour = startDateTime.getHour();

            List<Map<String,String>> hourly = weatherApiService.getHourlyWeather(LocationService.getUserCity(),
                    date, hour, hour);

            if (!hourly.isEmpty()){
                Map<String, String> hourlyMap = hourly.get(0);
                description = hourlyMap.get("description");
                feels = hourlyMap.get("feels");
                Map<String, Object> daily = weatherApiService.getDailyWeather(LocationService.getUserCity(),
                        date);
                iconName = daily.get("iconName") != null ? daily.get("iconName").toString() : "";
            }
        } catch (IOException e) {
            System.err.println("Weather Lookup Failed: " + e.getMessage());
        }
        String temperature = feels;

        return new WeatherInfo(description, iconName, temperature);
    }

}
