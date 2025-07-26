package use_case.weatherApiCall;

import data_access.WeatherApiService;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WeatherApiServiceTest {

    @Test
    void testGetDailyWeather() throws Exception {
        WeatherApiService apiService = new WeatherApiService();

        Map<String, Object> daily = apiService.getDailyWeather("Toronto", LocalDate.now());

        assertNotNull(daily, "Daily weather should not be null");
        assertTrue(daily.containsKey("tempmax"), "Should contain tempmax");
        assertTrue(daily.containsKey("tempmin"), "Should contain tempmin");
        assertTrue(daily.containsKey("feelslikemax"), "Should contain feelslikemax");
        assertTrue(daily.containsKey("feelslikemin"), "Should contain feelslikemin");
        assertTrue(daily.containsKey("icon"), "Should contain icon");

        Object iconObj = daily.get("icon");

        assertTrue(iconObj == null || iconObj instanceof ImageIcon,
                "Icon should be null or an instance of ImageIcon");

        System.out.println("=== Daily Weather ===");
        System.out.println("High: " + daily.get("tempmax") + "°C");
        System.out.println("Low: " + daily.get("tempmin") + "°C");
        System.out.println("Feels Like High: " + daily.get("feelslikemax") + "°C");
        System.out.println("Feels Like Low: " + daily.get("feelslikemin") + "°C");
        System.out.println("=====================\n");

        System.out.println("Daily weather test passed\n");
    }

    @Test
    void testGetHourlyWeather() throws Exception {
        WeatherApiService apiService = new WeatherApiService();

        List<Map<String, String>> hourly = apiService.getHourlyWeather("Toronto", LocalDate.now(), 9, 12);

        assertNotNull(hourly, "Hourly weather should not be null");
        assertFalse(hourly.isEmpty(), "Hourly weather should not be empty");

        System.out.println("=== Hourly Weather ===");
        for (Map<String, String> hour : hourly) {
            assertTrue(hour.containsKey("time"), "Each hour should contain time");
            assertTrue(hour.containsKey("description"), "Each hour should contain description");
            assertTrue(hour.containsKey("feelslike"), "Each hour should contain feelslike");

            System.out.println(hour.get("time") + " — " +
                    hour.get("feelslike") + ", " +
                    hour.get("description"));
        }
        System.out.println("======================\n");

        System.out.println("Hourly weather test passed");
    }
}
