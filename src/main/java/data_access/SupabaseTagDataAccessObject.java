package data_access;

import com.google.gson.*;
import okhttp3.*;
import use_case.createCustomTag.customTagDataAccessInterface;
import entity.CustomTag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * SupabaseTagDataAccessObject implements methods defined in customTagDataAccessInterface.
 * These methods support tag storage/retrieval for each user using Supabase REST API,
 * where custom_tags is stored as a JSONB dictionary: tagName → emoji.
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

    /**
     * Fetches all custom tags (as a map of tagName → emoji) for a given user.
     */
    @Override
    public Map<String, String> getCustomTags(String username) {
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

            if (jsonArray.isEmpty()) return new HashMap<>();

            JsonObject tagsObject = jsonArray.get(0).getAsJsonObject().getAsJsonObject("custom_tags");
            Map<String, String> tags = new HashMap<>();

            if (tagsObject != null) {
                for (Map.Entry<String, JsonElement> entry : tagsObject.entrySet()) {
                    tags.put(entry.getKey(), entry.getValue().getAsString());
                }
            }

            return tags;

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /**
     * Adds or replaces a custom tag for the user.
     */
    @Override
    public void addCustomTag(String username, CustomTag tagToAdd) {
        Map<String, String> tags = getCustomTags(username);
        tags.put(tagToAdd.getTagName(), tagToAdd.getTagEmoji());
        patchTags(username, tags);
    }

    /**
     * Deletes a tag by name for the user.
     */
    @Override
    public void deleteCustomTag(String username, CustomTag tagToDelete) {
        Map<String, String> tags = getCustomTags(username);
        tags.remove(tagToDelete.getTagName());
        patchTags(username, tags);
    }

    /**
     * Helper method to update the Supabase user's custom_tags column with the provided map.
     */
    private void patchTags(String username, Map<String, String> tags) {
        try {
            JsonObject jsonTags = new JsonObject();
            for (Map.Entry<String, String> entry : tags.entrySet()) {
                jsonTags.addProperty(entry.getKey(), entry.getValue());
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
