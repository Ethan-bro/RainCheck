package data_access;

import com.google.gson.*;
import use_case.createCustomTag.customTagDataAccessInterface;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SupabaseTagDataAccessObject implements methods defined in customTagDataAccessInterface.
 * These methods are required to be implemented to support tag storage/retrieval
 * for each user using Supabase's REST API and the custom_tags column.
 */
public class SupabaseTagDataAccessObject implements customTagDataAccessInterface {

    private final String baseUrl;
    private final String apiKey;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public SupabaseTagDataAccessObject(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Override
    public List<String> getCustomTags(String username) {
        try {
            HttpUrl url = HttpUrl.parse(baseUrl + "/rest/v1/users")
                    .newBuilder()
                    .addQueryParameter("username", "eq." + username)
                    .addQueryParameter("select", "custom_tags")
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("apikey", apiKey)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Accept", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Failed to get tags");

            String responseBody = Objects.requireNonNull(response.body()).string();
            JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();

            if (jsonArray.isEmpty()) return new ArrayList<>();

            JsonArray tagsArray = jsonArray.get(0).getAsJsonObject().getAsJsonArray("custom_tags");

            List<String> tags = new ArrayList<>();
            for (JsonElement tag : tagsArray) {
                tags.add(tag.getAsString());
            }
            return tags;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void addCustomTag(String username, String newTag) {
        List<String> tags = getCustomTags(username);
        if (!tags.contains(newTag)) tags.add(newTag);
        patchTags(username, tags);
    }

    @Override
    public void deleteCustomTag(String username, String tagToRemove) {
        List<String> tags = getCustomTags(username);
        tags.removeIf(tag -> tag.equals(tagToRemove));
        patchTags(username, tags);
    }

    private void patchTags(String username, List<String> tags) {
        try {
            JsonArray jsonTags = new JsonArray();
            for (String tag : tags) {
                jsonTags.add(tag);
            }

            JsonObject update = new JsonObject();
            update.add("custom_tags", jsonTags);

            RequestBody body = RequestBody.create(
                    gson.toJson(update), MediaType.get("application/json"));

            Request request = new Request.Builder()
                    .url(baseUrl + "/rest/v1/users?username=eq." + username)
                    .addHeader("apikey", apiKey)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Prefer", "return=minimal")
                    .patch(body)
                    .build();

            client.newCall(request).execute().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
