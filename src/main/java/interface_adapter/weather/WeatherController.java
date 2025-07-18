package interface_adapter.weather;

import use_case.weather.daily.DailyWeatherInputBoundary;
import use_case.weather.daily.DailyWeatherInputData;
import use_case.weather.hourly.HourlyWeatherInputBoundary;
import use_case.weather.hourly.HourlyWeatherInputData;

import java.time.LocalDate;

public class WeatherController {
    private final DailyWeatherInputBoundary dailyInteractor;
    private final HourlyWeatherInputBoundary hourlyInteractor;

    public WeatherController(DailyWeatherInputBoundary dailyInteractor,
                             HourlyWeatherInputBoundary hourlyInteractor) {
        this.dailyInteractor = dailyInteractor;
        this.hourlyInteractor = hourlyInteractor;
    }

    public void fetchDaily(String location, LocalDate date) {
        DailyWeatherInputData inputData = new DailyWeatherInputData(location, date);
        dailyInteractor.fetchDailyWeather(inputData);
    }

    public void fetchHourly(String location, LocalDate date, int startHour, int endHour) {
        HourlyWeatherInputData inputData = new HourlyWeatherInputData(location, date, startHour, endHour);
        hourlyInteractor.fetchHourlyWeather(inputData);
    }
}
