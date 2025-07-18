package use_case.weather.daily;

/**
 * Output boundary for presenting the daily weather results.
 */
public interface DailyWeatherOutputBoundary {
    void prepareSuccessView(DailyWeatherOutputData outputData);
    void prepareFailView(String error);
}
