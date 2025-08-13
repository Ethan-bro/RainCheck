package entity;

/**
 * Record representing weather information for a task.
 * @param description the weather description
 * @param iconName the weather icon name
 * @param temperature the temperature
 * @param uvIndex the UV index
 */
public record WeatherInfo(String description, String iconName, String temperature, String uvIndex) {
}
