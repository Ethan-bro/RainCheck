package entity;

public class WeatherInfo {
    private final String description;
    private final String feels;
    private final String iconName;
    private final String temperature;

    public WeatherInfo(String description, String feels, String iconName, String temperature) {
        this.description = description;
        this.feels = feels;
        this.iconName = iconName;
        this.temperature = temperature;
    }

    public String getDescription() { return description; }
    public String getFeels() { return feels; }
    public String getIconName() { return iconName; }
    public String getTemperature() { return temperature; }
}
