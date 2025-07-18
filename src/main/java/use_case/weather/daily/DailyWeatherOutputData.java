package use_case.weather.daily;

public class DailyWeatherOutputData {
    private final double tempMax;
    private final double tempMin;
    private final double feelsLike;

    public DailyWeatherOutputData(double tempMax, double tempMin, double feelsLike) {
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.feelsLike = feelsLike;
    }

    public double getTempMax() {
        return tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getFeelsLike() {
        return feelsLike;
    }
}
