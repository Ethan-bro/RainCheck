package interface_adapter.weather;

import use_case.weather.daily.DailyWeatherOutputBoundary;
import use_case.weather.daily.DailyWeatherOutputData;

public class DailyWeatherPresenter implements DailyWeatherOutputBoundary {

    private final WeatherViewModel viewModel;

    public DailyWeatherPresenter(WeatherViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(DailyWeatherOutputData outputData) {
        WeatherState state = viewModel.getState();
        state.setHighTemp(outputData.getHigh());
        state.setLowTemp(outputData.getLow());
        state.setFeelsLike(outputData.getFeelsLike());
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
