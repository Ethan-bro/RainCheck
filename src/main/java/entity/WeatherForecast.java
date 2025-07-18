package entity;

public class WeatherForecast {
    private final String date;
    private final double highTemp;
    private final double lowTemp;
    private final double feelsLike;
    private final String description;

    public WeatherForecast(String date, double highTemp, double lowTemp, double feelsLike, String description) {
        this.date = date;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.feelsLike = feelsLike;
        this.description = description;
    }

    public String getDate() { return date; }
    public double getHighTemp() { return highTemp; }
    public double getLowTemp() { return lowTemp; }
    public double getFeelsLike() { return feelsLike; }
    public String getDescription() { return description; }
}
