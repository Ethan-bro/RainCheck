package data_access;

import entity.CustomTag;

import use_case.createCustomTag.CustomTagDataAccessInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * SupabaseTagDataAccessObject implements methods defined in CustomTagDataAccessInterface.
 * These methods support tag storage/retrieval for each user using Supabase REST API,
 * where custom_tags is stored as a JSONB dictionary: tagName → emoji.
 */
public class SupabaseTagDataAccessObject implements CustomTagDataAccessInterface {

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
     *
     * @param username the user's username
     * @return a map of tag names to emojis, or empty map if none found or error occurs
     */
    @Override
    public Map<String, String> getCustomTags(String username) {
        final String customTagsKey = "custom_tags";
        Map<String, String> tags = Map.of();

        final HttpUrl url = HttpUrl.parse(baseUrl + "/rest/v1/users")
                .newBuilder()
                .addQueryParameter("username", "eq." + username)
                .addQueryParameter("select", customTagsKey)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                final String responseBody = Objects.requireNonNull(response.body()).string();
                final JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();

                if (!jsonArray.isEmpty()) {
                    final JsonObject tagsObject = jsonArray.get(0).getAsJsonObject().getAsJsonObject(customTagsKey);
                    if (tagsObject != null) {
                        final Map<String, String> modifiableTags = new HashMap<>();
                        for (Map.Entry<String, JsonElement> entry : tagsObject.entrySet()) {
                            modifiableTags.put(entry.getKey(), entry.getValue().getAsString());
                        }
                        tags = modifiableTags;
                    }
                }
            }
        }
        catch (IOException ioEx) {
            ioEx.printStackTrace();
        }

        return tags;
    }

    /**
     * Adds or replaces a custom tag for the user.
     *
     * @param username the user's username
     * @param tagToAdd the tag to add or replace
     */
    @Override
    public void addCustomTag(String username, CustomTag tagToAdd) {
        final Map<String, String> tags = getCustomTags(username);
        tags.put(tagToAdd.getTagName(), tagToAdd.getTagIcon());
        patchTags(username, tags);
    }

    /**
     * Deletes a tag by name for the user.
     *
     * @param username    the user's username
     * @param tagToDelete the tag to delete
     */
    @Override
    public void deleteCustomTag(String username, CustomTag tagToDelete) {
        final Map<String, String> tags = getCustomTags(username);
        tags.remove(tagToDelete.getTagName());
        patchTags(username, tags);
    }

    /**
     * Helper method to update the Supabase user's custom_tags column with the provided map.
     *
     * @param username the user's username
     * @param tags     the map of tags to update
     */
    private void patchTags(String username, Map<String, String> tags) {
        final String customTagsKey = "custom_tags";

        final JsonObject jsonTags = new JsonObject();
        for (Map.Entry<String, String> entry : tags.entrySet()) {
            jsonTags.addProperty(entry.getKey(), entry.getValue());
        }

        final JsonObject update = new JsonObject();
        update.add(customTagsKey, jsonTags);

        final RequestBody body = RequestBody.create(
                gson.toJson(update), MediaType.get("application/json"));

        final Request request = new Request.Builder()
                .url(baseUrl + "/rest/v1/users?username=eq." + username)
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Prefer", "return=minimal")
                .patch(body)
                .build();

        try {
            client.newCall(request).execute().close();
        }
        catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }
}
