package use_case.weather.hourly;

import java.util.List;
import java.util.Map;

public class HourlyWeatherOutputData {
    private final List<Map<String, String>> hourlyWeather;

    public HourlyWeatherOutputData(List<Map<String, String>> hourlyWeather) {
        this.hourlyWeather = hourlyWeather;
    }

    public List<Map<String, String>> getHourlyWeather() {
        return hourlyWeather;
    }
}
