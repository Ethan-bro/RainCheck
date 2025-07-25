package data_access;

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

    private static final String URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/%s/%s?unitGroup=metric&iconSet=icons2&key=%s&contentType=json";

    public WeatherApiService() throws IOException {
        JsonObject config = JsonParser.parseReader(new FileReader("config/secrets.json")).getAsJsonObject();
        this.apiKey = config.get("weather_api_key").getAsString();
    }

    public Map<String, Object> getDailyWeather(String location, LocalDate date) throws IOException {
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
        String iconPath = "/images/weatherIcons/" + iconName + ".png";

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
        String url = String.format(
                URL,
                location,
                date.toString(),
                apiKey
        );

        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code: " + response);

        if (response.body() != null) {
            return JsonParser.parseString(response.body().string()).getAsJsonObject();
        }

        return null;
    }
}
