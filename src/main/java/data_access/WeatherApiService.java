package data_access;

import use_case.weather.daily.DailyWeatherDataAccessInterface;
import use_case.weather.hourly.HourlyWeatherDataAccessInterface;

import java.awt.Image;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Service to fetch weather data from Visual Crossing Weather API.
 * Implements interfaces for daily and hourly weather data access.
 */
public class WeatherApiService implements DailyWeatherDataAccessInterface, HourlyWeatherDataAccessInterface {

    private static final String URL_TEMPLATE =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/%s/%s"
                    + "?unitGroup=metric&iconSet=icons2&key=%s&contentType=json";

    private static final int ICON_WIDTH = 40;
    private static final int ICON_HEIGHT = 40;

    private static final double DEFAULT_TEMP_MAX = 25.0;
    private static final double DEFAULT_TEMP_MIN = 18.0;
    private static final double DEFAULT_FEELS_LIKE_MAX = 26.0;
    private static final double DEFAULT_FEELS_LIKE_MIN = 17.0;

    private static final String DEFAULT_ICON_NAME = "clear-day";

    private static final String KEY_TEMP_MAX = "tempmax";
    private static final String KEY_TEMP_MIN = "tempmin";
    private static final String KEY_FEELS_LIKE_MAX = "feelslikemax";
    private static final String KEY_FEELS_LIKE_MIN = "feelslikemin";
    private static final String KEY_ICON = "icon";
    private static final String KEY_ICON_NAME = "iconName";

    // New constants to avoid multiple string literal violations
    private static final String KEY_FEELSLIKE = "feelslike";
    private static final String KEY_UVINDEX = "uvindex";

    private final String apiKey;
    private final OkHttpClient client = new OkHttpClient();
    private final Map<String, JsonObject> weeklyWeatherCache = new HashMap<>();

    private final boolean useFakeData = true;

    /**
     * Constructs the WeatherApiService by reading the API key from config file.
     *
     * @throws IOException if config file reading fails
     */
    public WeatherApiService() throws IOException {
        final JsonObject config = JsonParser.parseReader(new FileReader("config/secrets.json")).getAsJsonObject();
        this.apiKey = config.get("weather_api_key").getAsString();
    }

    /**
     * Returns daily weather data for the given location and date.
     *
     * @param location the location name
     * @param date     the date for weather data
     * @return map of weather attributes including temperatures and icons
     * @throws IOException if API call fails
     */
    @Override
    public Map<String, Object> getDailyWeather(final String location, final LocalDate date) throws IOException {
        final Map<String, Object> result = new HashMap<>();

        if (useFakeData) {
            System.out.println("Using fake data");
            result.put(KEY_TEMP_MAX, DEFAULT_TEMP_MAX);
            result.put(KEY_TEMP_MIN, DEFAULT_TEMP_MIN);
            result.put(KEY_FEELS_LIKE_MAX, DEFAULT_FEELS_LIKE_MAX);
            result.put(KEY_FEELS_LIKE_MIN, DEFAULT_FEELS_LIKE_MIN);
            result.put(KEY_ICON, getWeatherImageIcon(DEFAULT_ICON_NAME));
            result.put(KEY_ICON_NAME, DEFAULT_ICON_NAME);
        }
        else {
            final JsonObject body = getVisualCrossingJSONBody(location, date);
            if (body != null) {
                final JsonArray daysArray = body.getAsJsonArray("days");
                if (daysArray != null && !daysArray.isEmpty()) {
                    extractWeatherAndPutInMap(daysArray, result);
                }
            }
        }

        return result;
    }

    /**
     * Extracts weather information from the first day in the given JsonArray
     * and puts relevant data into the provided map.
     * The map is mutated by adding keys for maximum and minimum temperatures,
     * feels-like temperatures (which may be null), and weather icon data.
     *
     * @param daysArray JsonArray containing daily weather data; expected to have at least one element
     * @param map       the Map to be mutated with extracted weather data; keys include temperature,
     *                  feels-like temperature, and icon information
     */
    private void extractWeatherAndPutInMap(JsonArray daysArray, Map<String, Object> map) {
        final JsonObject day = daysArray.get(0).getAsJsonObject();

        map.put(KEY_TEMP_MAX, day.get(KEY_TEMP_MAX).getAsDouble());
        map.put(KEY_TEMP_MIN, day.get(KEY_TEMP_MIN).getAsDouble());

        if (day.has(KEY_FEELS_LIKE_MAX) && !day.get(KEY_FEELS_LIKE_MAX).isJsonNull()) {
            map.put(KEY_FEELS_LIKE_MAX, day.get(KEY_FEELS_LIKE_MAX).getAsDouble());
        }
        else {
            map.put(KEY_FEELS_LIKE_MAX, null);
        }

        if (day.has(KEY_FEELS_LIKE_MIN) && !day.get(KEY_FEELS_LIKE_MIN).isJsonNull()) {
            map.put(KEY_FEELS_LIKE_MIN, day.get(KEY_FEELS_LIKE_MIN).getAsDouble());
        }
        else {
            map.put(KEY_FEELS_LIKE_MIN, null);
        }

        final String iconName = day.get(KEY_ICON).getAsString();
        map.put(KEY_ICON, getWeatherImageIcon(iconName));
        map.put(KEY_ICON_NAME, iconName);
    }

    // Loads and scales a weather icon image for the given icon name.
    private ImageIcon getWeatherImageIcon(final String iconName) {
        final String iconPath = "/weatherIcons/" + iconName + ".png";
        ImageIcon icon = null;

        try {
            final java.net.URL imgUrl = getClass().getResource(iconPath);
            if (imgUrl != null) {
                final Image img = new ImageIcon(imgUrl).getImage();
                final Image scaledImg = img.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImg);
            }
            else {
                System.err.println("Couldn't find icon: " + iconPath);
            }
        }
        catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        return icon;
    }

    /**
     * Returns hourly weather data for the given location and date between startHour and endHour.
     *
     * @param location  the location name
     * @param date      the date for weather data
     * @param startHour the start hour (inclusive)
     * @param endHour   the end hour (inclusive)
     * @return list of maps for each hour containing time, description, feelslike, iconName, and uvindex
     * @throws IOException if API call fails
     */
    @Override
    public List<Map<String, String>> getHourlyWeather(final String location, final LocalDate date,
                                                      final int startHour, final int endHour) throws IOException {
        final List<Map<String, String>> result = new ArrayList<>();

        if (useFakeData) {
            System.out.println("Using fake data");
            for (int i = startHour; i <= endHour; i++) {
                final Map<String, String> mock = new HashMap<>();
                populateFakeHourlyWeather(i, mock);
                result.add(mock);
            }
        }
        else {
            final JsonObject body = getVisualCrossingJSONBody(location, date);
            if (body != null) {
                final JsonArray daysArray = body.getAsJsonArray("days");
                if (daysArray != null && !daysArray.isEmpty()) {
                    final JsonArray hours = daysArray.get(0).getAsJsonObject().getAsJsonArray("hours");
                    for (int i = startHour; i <= endHour && i < hours.size(); i++) {
                        final JsonObject hour = hours.get(i).getAsJsonObject();
                        final Map<String, String> weatherMap = new HashMap<>();
                        extractHourlyWeatherAndPutInMap(hour, weatherMap);
                        result.add(weatherMap);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Populates a map with fake hourly weather data for the given hour.
     *
     * @param hour the hour of the day (0-23)
     * @param map  the map to be mutated with fake weather data
     */
    private void populateFakeHourlyWeather(final int hour, final Map<String, String> map) {
        map.put("time", String.format("%02d:00", hour));
        map.put("description", "Sunny");
        map.put(KEY_FEELSLIKE, "22°C");
        map.put(KEY_ICON_NAME, DEFAULT_ICON_NAME);
        map.put(KEY_UVINDEX, "3");
    }

    /**
     * Extracts weather information from a JsonObject representing an hour,
     * and puts relevant data into the provided map.
     * The map is mutated by adding keys for time, description, feels-like temperature,
     * icon name, and UV index.
     *
     * @param hourJson JsonObject containing hourly weather data
     * @param map      the map to be mutated with extracted weather data
     */
    private void extractHourlyWeatherAndPutInMap(final JsonObject hourJson, final Map<String, String> map) {
        final String time = hourJson.get("datetime").getAsString();
        final String description = hourJson.get("conditions").getAsString();
        final String feelsLike = hourJson.get(KEY_FEELSLIKE).getAsString();

        final String iconName;
        if (hourJson.has(KEY_ICON) && !hourJson.get(KEY_ICON).isJsonNull()) {
            iconName = hourJson.get(KEY_ICON).getAsString();
        }
        else {
            iconName = DEFAULT_ICON_NAME;
        }

        String uvIndex = "N/A";
        if (hourJson.has(KEY_UVINDEX) && !hourJson.get(KEY_UVINDEX).isJsonNull()) {
            uvIndex = String.valueOf(hourJson.get(KEY_UVINDEX).getAsInt());
        }

        map.put("time", time);
        map.put("description", description);
        map.put(KEY_FEELSLIKE, feelsLike + "°C");
        map.put(KEY_ICON_NAME, iconName);
        map.put(KEY_UVINDEX, uvIndex);
    }

    /**
     * Retrieves weather data as a JsonObject for the specified location and date.
     * Uses a cache to avoid redundant API calls. If not cached, makes an HTTP request
     * to the Visual Crossing Weather API, parses the response, caches it, and returns it.
     *
     * @param location the location name for which to fetch weather data
     * @param date     the date for the weather forecast
     * @return a JsonObject representing the weather data, or null if no response body is present
     * @throws IOException if the HTTP request fails or returns an unsuccessful response
     */
    private JsonObject getVisualCrossingJSONBody(final String location, final LocalDate date) throws IOException {
        final String key = location + "-" + date.toString();
        JsonObject result = null;

        if (weeklyWeatherCache.containsKey(key)) {
            result = weeklyWeatherCache.get(key);
        }
        else {
            System.out.println("Making an API call for " + location);

            final String url = String.format(URL_TEMPLATE, location, date, apiKey);
            final Request request = new Request.Builder().url(url).build();
            final Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code: " + response);
            }

            if (response.body() != null) {
                final String bodyString = response.body().string();
                final JsonObject json = JsonParser.parseString(bodyString).getAsJsonObject();
                weeklyWeatherCache.put(key, json);
                result = json;
            }
        }

        return result;
    }
}
