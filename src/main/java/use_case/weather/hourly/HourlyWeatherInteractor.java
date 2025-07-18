package use_case.weather.hourly;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HourlyWeatherInteractor implements HourlyWeatherInputBoundary {
    private final HourlyWeatherDataAccessInterface weatherApi;
    private final HourlyWeatherOutputBoundary presenter;

    public HourlyWeatherInteractor(HourlyWeatherDataAccessInterface weatherApi,
                                   HourlyWeatherOutputBoundary presenter) {
        this.weatherApi = weatherApi;
        this.presenter = presenter;
    }

    @Override
    public void fetchHourlyWeather(HourlyWeatherInputData inputData) {
        try {
            List<Map<String, String>> result = weatherApi.getHourlyWeather(
                    inputData.getLocation(),
                    inputData.getDate(),
                    inputData.getStartHour(),
                    inputData.getEndHour()
            );

            if (result.isEmpty()) {
                presenter.prepareFailView("No hourly data available.");
                return;
            }

            HourlyWeatherOutputData outputData = new HourlyWeatherOutputData(result);
            presenter.prepareSuccessView(outputData);
        } catch (IOException e) {
            presenter.prepareFailView("Failed to fetch hourly weather: " + e.getMessage());
        }
    }
}
