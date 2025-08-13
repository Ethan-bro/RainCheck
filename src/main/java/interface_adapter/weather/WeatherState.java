package interface_adapter.weather;

import java.util.List;
import java.util.Map;

public class WeatherState {

    private double highTemp;
    private double lowTemp;
    private double feelsLike;
    private List<Map<String, String>> hourlySummaries;
    private String error;

    /**
     * Gets the high temperature.
     * @return the high temperature
     */
    public double getHighTemp() {
        return highTemp;
    }

    /**
     * Sets the high temperature.
     * @param highTemp the high temperature to set
     */
    public void setHighTemp(double highTemp) {
        this.highTemp = highTemp;
    }

    /**
     * Gets the low temperature.
     * @return the low temperature
     */
    public double getLowTemp() {
        return lowTemp;
    }

    /**
     * Sets the low temperature.
     * @param lowTemp the low temperature to set
     */
    public void setLowTemp(double lowTemp) {
        this.lowTemp = lowTemp;
    }

    /**
     * Gets the feels like temperature.
     * @return the feels like temperature
     */
    public double getFeelsLike() {
        return feelsLike;
    }

    /**
     * Sets the feels like temperature.
     * @param feelsLike the feels like temperature to set
     */
    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    /**
     * Gets the hourly summaries.
     * @return the hourly summaries
     */
    public List<Map<String, String>> getHourlySummaries() {
        return hourlySummaries;
    }

    /**
     * Sets the hourly summaries.
     * @param hourlySummaries the hourly summaries to set
     */
    public void setHourlySummaries(List<Map<String, String>> hourlySummaries) {
        this.hourlySummaries = hourlySummaries;
    }

    /**
     * Gets the error message.
     * @return the error message
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message.
     * @param error the error message to set
     */
    public void setError(String error) {
        this.error = error;
    }
}
