package interface_adapter.weather;

import use_case.weather.hourly.HourlyWeatherOutputBoundary;
import use_case.weather.hourly.HourlyWeatherOutputData;

public class HourlyWeatherPresenter implements HourlyWeatherOutputBoundary {

    private final WeatherViewModel viewModel;

    public HourlyWeatherPresenter(WeatherViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(HourlyWeatherOutputData outputData) {
        WeatherState state = viewModel.getState();
        state.setHourlySummaries(outputData.getSummaries());
        state.setError(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        WeatherState state = viewModel.getState();
        state.setError(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
