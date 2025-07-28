package data_access;

import com.google.gson.*;
import entity.CommonUser;
import entity.CustomTag;
import entity.User;
import okhttp3.*;
import use_case.createCustomTag.customTagDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import javax.swing.*;
import java.io.IOException;
import java.util.Collections;

public class SupabaseUserDataAccessObject implements LoginUserDataAccessInterface, SignupUserDataAccessInterface,
        customTagDataAccessInterface {

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final String baseUrl;
    private final String apiKey;
    private String currentUser = null;

    public SupabaseUserDataAccessObject(String baseUrl, String apiKey) {
        if (!baseUrl.startsWith("http")) {
            throw new IllegalArgumentException("Supabase base URL must start with http or https");
        }
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Override
    public boolean existsByName(String username) {
        Request request = new Request.Builder()
                .url(baseUrl + "/rest/v1/users?username=eq." + username + "&select=username")
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return false;
            String responseBody = response.body() != null ? response.body().string() : "";
            JsonArray result = JsonParser.parseString(responseBody).getAsJsonArray();
            return !result.isEmpty();
        } catch (IOException e) {
            throw new RuntimeException("Failed to check if user exists", e);
        }
    }

    @Override
    public User get(String username) {
        Request request = new Request.Builder()
                .url(baseUrl + "/rest/v1/users?username=eq." + username + "&select=username,password")
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) return null;

            String body = response.body().string();
            JsonArray users = JsonParser.parseString(body).getAsJsonArray();
            if (users.isEmpty()) return null;

            JsonObject user = users.get(0).getAsJsonObject();
            return new CommonUser(user.get("username").getAsString(), user.get("password").getAsString());

        } catch (IOException e) {
            throw new RuntimeException("Failed to get user", e);
        }
    }

    @Override
    public void save(User user) {
        JsonObject newUser = new JsonObject();
        newUser.addProperty("username", user.getName());
        newUser.addProperty("password", user.getPassword());
        newUser.add("tasks", new JsonArray()); // placeholder for now
        newUser.add("tags", new JsonArray());

        String json = gson.toJson(Collections.singletonList(newUser));
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(baseUrl + "/rest/v1/users")
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=minimal,resolution=merge-duplicates")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Save failed: " + response.code() + " - " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    @Override
    public String getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean isUsernameValid(String username) {
        return !existsByName(username);
    }

    // CUSTOM TAG METHODS:

    public boolean existsByTagName(String tagName) {
        // search through all existing tags

        return false;
    }

    public boolean existsByTagIcon(ImageIcon tagIcon) {
        return false;
    }

    public void saveTag(CustomTag tag) {

    }
}
