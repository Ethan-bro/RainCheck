package data_access;

import java.io.IOException;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import entity.CommonUser;
import entity.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class SupabaseUserDataAccessObject implements
        LoginUserDataAccessInterface,
        SignupUserDataAccessInterface,
        LogoutUserDataAccessInterface {

    private static final String API_KEY_HEADER = "apikey";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String PREFER_HEADER = "Prefer";
    private static final String PREFER_VALUE = "return=minimal,resolution=merge-duplicates";
    private static final String EMAIL_FIELD = "email";
    private static final int CONFLICT_CODE = 409;

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final String baseUrl;
    private final String apiKey;
    private String currentUser;

    /**
     * Constructs a SupabaseUserDataAccessObject with the given base URL and API key.
     *
     * @param baseUrl the base URL of the Supabase instance
     * @param apiKey the API key for authentication
     * @throws IllegalArgumentException if the baseUrl does not start with http or https
     */
    public SupabaseUserDataAccessObject(String baseUrl, String apiKey) {
        if (!baseUrl.startsWith("http")) {
            throw new IllegalArgumentException("Supabase base URL must start with http or https");
        }
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Override
    /**
     * Checks if a user exists by username.
     *
     * @param username the username to check
     * @return true if the user exists, false otherwise
     * @throws RuntimeException if the check fails due to an IO error
     */
    public boolean existsByName(String username) {
        final String url = baseUrl + "/rest/v1/users?username=eq." + username + "&select=username";
        final Request request = new Request.Builder()
                .url(url)
                .addHeader(API_KEY_HEADER, apiKey)
                .addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + apiKey)
                .build();

        boolean exists = false;
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                final String responseBody;
                if (response.body() != null) {
                    responseBody = response.body().string();
                }
                else {
                    responseBody = "";
                }
                final JsonArray result = JsonParser.parseString(responseBody).getAsJsonArray();
                exists = !result.isEmpty();
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to check if user exists", ex);
        }
        return exists;
    }

    @Override
    /**
     * Retrieves a user by username.
     *
     * @param username the username of the user to retrieve
     * @return the User object if found, null otherwise
     * @throws RuntimeException if the retrieval fails due to an IO error
     */
    public User get(String username) {
        final String url = baseUrl + "/rest/v1/users?username=eq." + username + "&select=username,password,email";
        final Request request = new Request.Builder()
                .url(url)
                .addHeader(API_KEY_HEADER, apiKey)
                .addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + apiKey)
                .build();

        User user = null;
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                final String body = response.body().string();
                final JsonArray users = JsonParser.parseString(body).getAsJsonArray();
                if (!users.isEmpty()) {
                    final JsonObject userJson = users.get(0).getAsJsonObject();
                    user = new CommonUser(
                            userJson.get("username").getAsString(),
                            userJson.get("password").getAsString(),
                            userJson.get(EMAIL_FIELD).getAsString()
                    );
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to get user", ex);
        }
        return user;
    }

    @Override
    /**
     * Saves a new user to the database.
     *
     * @param user the User object to save
     * @throws DuplicateEmailException if the email already exists
     * @throws RuntimeException if the save fails due to an IO error or other error
     */
    public void save(User user) throws DuplicateEmailException {
        final JsonObject newUser = new JsonObject();
        newUser.addProperty("username", user.getName());
        newUser.addProperty("password", user.getPassword());
        newUser.addProperty("email", user.getEmail());
        newUser.add("tasks", new JsonArray());
        newUser.add("custom_tags", new JsonObject());

        final String json = gson.toJson(Collections.singletonList(newUser));
        final RequestBody body = RequestBody.create(json, MediaType.get(CONTENT_TYPE_JSON));

        final Request request = new Request.Builder()
                .url(baseUrl + "/rest/v1/users")
                .addHeader(API_KEY_HEADER, apiKey)
                .addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + apiKey)
                .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                .addHeader(PREFER_HEADER, PREFER_VALUE)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                final String errorDetails;
                if (response.body() != null) {
                    errorDetails = response.body().string();
                }
                else {
                    errorDetails = "No response body";
                }

                if (response.code() == CONFLICT_CODE
                        && errorDetails.contains("duplicate key")
                        && errorDetails.contains(EMAIL_FIELD)) {
                    throw new DuplicateEmailException("Email already exists.");
                }

                throw new RuntimeException(
                        "Save failed: " + response.code() + " - " + response.message()
                                + " - " + errorDetails
                );
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to save user", ex);
        }
    }

    @Override
    /**
     * Sets the current user by username.
     *
     * @param username the username to set as current user
     */
    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    @Override
    /**
     * Gets the current user's username.
     *
     * @return the current user's username
     */
    public String getCurrentUser() {
        return currentUser;
    }

    @Override
    /**
     * Checks if a username is valid (not already taken).
     *
     * @param username the username to validate
     * @return true if the username is valid, false otherwise
     */
    public boolean isUsernameValid(String username) {
        return !existsByName(username);
    }

    @Override
    /**
     * Sets the current username (alias for setCurrentUser).
     *
     * @param username the username to set as current
     */
    public void setCurrentUsername(String username) {
        setCurrentUser(username);
    }

    @Override
    /**
     * Gets the current username (alias for getCurrentUser).
     *
     * @return the current user's username
     */
    public String getCurrentUsername() {
        return getCurrentUser();
    }
}
