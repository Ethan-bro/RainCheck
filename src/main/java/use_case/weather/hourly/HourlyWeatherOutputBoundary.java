package use_case.weather.hourly;

/**
 * Output boundary for presenting hourly weather results.
 */
public interface HourlyWeatherOutputBoundary {
    void prepareSuccessView(HourlyWeatherOutputData outputData);
    void prepareFailView(String errorMessage);
}
