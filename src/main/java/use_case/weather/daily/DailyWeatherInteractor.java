package use_case.weather.daily;

import java.io.IOException;
import java.util.Map;

public class DailyWeatherInteractor implements DailyWeatherInputBoundary {
    private final DailyWeatherDataAccessInterface weatherApi;
    private final DailyWeatherOutputBoundary presenter;

    public DailyWeatherInteractor(DailyWeatherDataAccessInterface weatherApi,
                                  DailyWeatherOutputBoundary presenter) {
        this.weatherApi = weatherApi;
        this.presenter = presenter;
    }

    @Override
    public void fetchDailyWeather(DailyWeatherInputData inputData) {
        try {
            Map<String, Object> data = weatherApi.getDailyWeather(inputData.getLocation(), inputData.getDate());
            if (data.isEmpty()) {
                presenter.prepareFailView("No weather data found.");
                return;
            }

            double tempMax = (double) data.get("tempmax");
            double tempMin = (double) data.get("tempmin");
            double feelsLike = (double) data.get("feelslike");

            DailyWeatherOutputData outputData = new DailyWeatherOutputData(tempMax, tempMin, feelsLike);
            presenter.prepareSuccessView(outputData);
        } catch (IOException e) {
            presenter.prepareFailView("Failed to fetch weather: " + e.getMessage());
        }
    }
}
