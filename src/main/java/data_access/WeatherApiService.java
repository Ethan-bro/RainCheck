package data_access;

import app.Main;
import com.google.gson.*;
import okhttp3.*;
import use_case.weather.daily.DailyWeatherDataAccessInterface;
import use_case.weather.hourly.HourlyWeatherDataAccessInterface;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class WeatherApiService implements DailyWeatherDataAccessInterface, HourlyWeatherDataAccessInterface {

    private final String apiKey;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final Map<String, JsonObject> weeklyWeatherCache = new HashMap<>();

    private final boolean USE_FAKE_DATA = true; // Set to true for testing

    private static final String URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/%s/%s?unitGroup=metric&iconSet=icons2&key=%s&contentType=json";

    public WeatherApiService() throws IOException {
        JsonObject config = JsonParser.parseReader(new FileReader("config/secrets.json")).getAsJsonObject();
        this.apiKey = config.get("weather_api_key").getAsString();
    }

    public Map<String, Object> getDailyWeather(String location, LocalDate date) throws IOException {
        if (USE_FAKE_DATA) {
            System.out.println("Using fake data");
            Map<String, Object> result = new HashMap<>();
            result.put("tempmax", 25.0);
            result.put("tempmin", 18.0);
            result.put("feelslikemax", 26.0);
            result.put("feelslikemin", 17.0);
            result.put("icon", getWeatherImageIcon("clear-day")); // use any default
            return result;
        }

        JsonObject body = getVisualCrossingJSONBody(location, date);

        if (body == null) {
            return Collections.emptyMap();
        }

        JsonObject day = body.getAsJsonArray("days").get(0).getAsJsonObject();

        Map<String, Object> result = new HashMap<>();
        result.put("tempmax", day.get("tempmax").getAsDouble());
        result.put("tempmin", day.get("tempmin").getAsDouble());
        result.put("feelslikemax", day.has("feelslikemax") && !day.get("feelslikemax").isJsonNull() ? day.get("feelslikemax").getAsDouble() : null);
        result.put("feelslikemin", day.has("feelslikemin") && !day.get("feelslikemin").isJsonNull() ? day.get("feelslikemin").getAsDouble() : null);

        String iconName = day.get("icon").getAsString(); // e.g. clear-day, thunder-rain, showers-night
        ImageIcon icon = getWeatherImageIcon(iconName);
        result.put("icon", icon);

        return result;
    }

    private ImageIcon getWeatherImageIcon(String iconName) {
        String iconPath = "/weatherIcons/" + iconName + ".png";

        ImageIcon icon = null;
        try {
            java.net.URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) {
                Image img = new ImageIcon(imgURL).getImage();
                Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImg);
            } else {
                System.err.println("Couldn't find icon: " + iconPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return icon;
    }

    public List<Map<String, String>> getHourlyWeather(String location, LocalDate date, int startHour, int endHour) throws IOException {
        if (USE_FAKE_DATA) {
            System.out.println("Using fake data");
            List<Map<String, String>> result = new ArrayList<>();
            for (int i = startHour; i <= endHour; i++) {
                Map<String, String> mock = new HashMap<>();
                mock.put("time", String.format("%02d:00", i));
                mock.put("description", "Sunny");
                mock.put("feelslike", "22°C");
                result.add(mock);
            }
            return result;
        }

        JsonObject body = getVisualCrossingJSONBody(location, date);

        if (body == null) {
            return Collections.emptyList();
        }

        JsonArray hours = body.getAsJsonArray("days").get(0).getAsJsonObject().getAsJsonArray("hours");

        List<Map<String, String>> result = new ArrayList<>();

        for (int i = startHour; i <= endHour && i < hours.size(); i++) {
            JsonObject hour = hours.get(i).getAsJsonObject();
            String time = hour.get("datetime").getAsString();
            String description = hour.get("conditions").getAsString();
            String feelsLike = hour.get("feelslike").getAsString();

            Map<String, String> weather = new HashMap<>();
            weather.put("time", time);
            weather.put("description", description);
            weather.put("feelslike", feelsLike + "°C");

            result.add(weather);
        }

        return result;
    }

    private JsonObject getVisualCrossingJSONBody(String location, LocalDate date) throws IOException {
        String key = location + "-" + date.toString();

        if (weeklyWeatherCache.containsKey(key)) {
            return weeklyWeatherCache.get(key);
        }

        System.out.println("Making an API call for " + location + ", count = " + Main.getNumOfAPIcallsMade());

        String url = String.format(URL, location, date, apiKey);

        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        Main.incrementNumOfAPIcallsMade();

        if (!response.isSuccessful()) throw new IOException("Unexpected code: " + response);

        if (response.body() != null) {
            JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
            weeklyWeatherCache.put(key, json);
            return json;
        }

        return null;
    }
}
