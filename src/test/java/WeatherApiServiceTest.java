import data_access.WeatherApiService;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class WeatherApiServiceTest {

    @Test
    public void testGetDailyWeather() throws Exception {
        WeatherApiService apiService = new WeatherApiService();

        Map<String, Object> daily = apiService.getDailyWeather("Toronto", LocalDate.now());

        assertNotNull("Daily weather should not be null", daily);
        assertTrue("Should contain tempmax", daily.containsKey("tempmax"));
        assertTrue("Should contain tempmin", daily.containsKey("tempmin"));
        assertTrue("Should contain feelslikemax", daily.containsKey("feelslikemax"));
        assertTrue("Should contain feelslikemin", daily.containsKey("feelslikemin"));
        assertTrue("Should contain icon", daily.containsKey("icon"));

        System.out.println("=== Daily Weather ===");
        System.out.println("High: " + daily.get("tempmax") + "°C");
        System.out.println("Low: " + daily.get("tempmin") + "°C");
        System.out.println("Feels Like High: " + daily.get("feelslikemax") + "°C");
        System.out.println("Feels Like Low: " + daily.get("feelslikemin") + "°C");
        System.out.println("Icon: " + daily.get("icon"));
        System.out.println("=====================\n");

        System.out.println("Daily weather test passed\n");
    }

    @Test
    public void testGetHourlyWeather() throws Exception {
        WeatherApiService apiService = new WeatherApiService();

        List<Map<String, String>> hourly = apiService.getHourlyWeather("Toronto", LocalDate.now(), 9, 12);

        assertNotNull("Hourly weather should not be null", hourly);
        assertFalse("Hourly weather should not be empty", hourly.isEmpty());

        System.out.println("=== Hourly Weather ===");
        for (Map<String, String> hour : hourly) {
            assertTrue("Each hour should contain time", hour.containsKey("time"));
            assertTrue("Each hour should contain description", hour.containsKey("description"));
            assertTrue("Each hour should contain feelslike", hour.containsKey("feelslike"));

            System.out.println(hour.get("time") + " — " +
                    hour.get("feelslike") + ", " +
                    hour.get("description"));
        }
        System.out.println("======================\n");

        System.out.println("Hourly weather test passed");
    }
}
