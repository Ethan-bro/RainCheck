package use_case.weather.daily;

public class DailyWeatherOutputData {

    private final double high;
    private final double low;
    private final double feelsLike;

    public DailyWeatherOutputData(double high, double low, double feelsLike) {
        this.high = high;
        this.low = low;
        this.feelsLike = feelsLike;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getFeelsLike() {
        return feelsLike;
    }
}
