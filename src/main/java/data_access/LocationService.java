package data_access;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationService {

    private static final String GEOLOCATION_API_URL = "https://ipapi.co/json/";
    private static final OkHttpClient client = new OkHttpClient();

    private LocationService() {
        // empty constructor
    }

    /**
     * Attempts to get the user's current city using their IP address.
     * Falls back to "Toronto" if the API fails.
     *
     * @return City name as a String
     */
    public static String getUserCity() {
        try {
            Request request = new Request.Builder()
                    .url(GEOLOCATION_API_URL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Unexpected code " + response);
                }

                assert response.body() != null;
                String responseBody = response.body().string();
                JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();

                return json.get("city").getAsString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Toronto"; // fallback if request fails
        }
    }
}
