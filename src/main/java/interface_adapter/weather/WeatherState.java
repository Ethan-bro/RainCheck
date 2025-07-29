package interface_adapter.weather;

import java.util.List;
import java.util.Map;

public class WeatherState {

    private double highTemp;
    private double lowTemp;
    private double feelsLike;
    private List<Map<String, String>> hourlySummaries;
    private String error;

    public double getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(double highTemp) {
        this.highTemp = highTemp;
    }

    public double getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(double lowTemp) {
        this.lowTemp = lowTemp;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public List<Map<String, String>> getHourlySummaries() {
        return hourlySummaries;
    }

    public void setHourlySummaries(List<Map<String, String>> hourlySummaries) {
        this.hourlySummaries = hourlySummaries;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
