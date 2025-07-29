package app;

import data_access.WeatherApiService;
import interface_adapter.weather.*;
import use_case.weather.daily.*;
import use_case.weather.hourly.*;

import java.io.IOException;

/**
 * Factory class for creating weather-related use case components.
 */
public class WeatherUseCaseFactory {

    /** Prevent instantiation. */
    private WeatherUseCaseFactory() {
    }

    /**
     * Creates and wires up the WeatherController and shared ViewModel.
     *
     * @return an array containing [WeatherController, WeatherViewModel]
     * @throws IOException if loading the API key fails
     */
    public static Object[] create() throws IOException {
        WeatherViewModel weatherViewModel = new WeatherViewModel();

        DailyWeatherOutputBoundary dailyPresenter = new DailyWeatherPresenter(weatherViewModel);
        HourlyWeatherOutputBoundary hourlyPresenter = new HourlyWeatherPresenter(weatherViewModel);

        WeatherApiService apiService = new WeatherApiService();

        DailyWeatherInputBoundary dailyInteractor = new DailyWeatherInteractor(apiService, dailyPresenter);
        HourlyWeatherInputBoundary hourlyInteractor = new HourlyWeatherInteractor(apiService, hourlyPresenter);

        WeatherController weatherController = new WeatherController(dailyInteractor, hourlyInteractor);

        return new Object[]{weatherController, weatherViewModel};
    }
}
