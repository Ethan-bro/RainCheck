package data_access;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class LocationService {

    private static final String GEOLOCATION_API_URL = "https://ipapi.co/json/";
    private static final String FALLBACK_CITY = "Toronto";
    private static final int RATE_LIMIT_CODE = 429;
    private static final OkHttpClient CLIENT = new OkHttpClient();

    private LocationService() {
        // empty constructor to prevent instantiation
    }

    /**
     * Attempts to get the user's current city using their IP address.
     * Falls back to "Toronto" if the API fails.
     *
     * @return City name as a String
     */
    public static String getUserCity() {
        String city = FALLBACK_CITY;

        try {
            final Request request = new Request.Builder()
                    .url(GEOLOCATION_API_URL)
                    .build();

            try (Response response = CLIENT.newCall(request).execute()) {
                if (response.code() == RATE_LIMIT_CODE) {
                    System.err.println("Rate limit exceeded. Using fallback city.");
                }
                else if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                else if (response.body() != null) {
                    final String responseBody = response.body().string();
                    final JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
                    city = json.get("city").getAsString();
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return city;
    }
}
