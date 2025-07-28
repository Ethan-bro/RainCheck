package data_access;

import com.google.gson.*;
import entity.CommonUser;
import entity.CustomTag;
import entity.User;
import entity.Task;
import okhttp3.*;
import use_case.DeleteTask.DeleteTaskDataAccessInterface;
import use_case.MarkTaskComplete.MarkTaskCompleteDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import javax.swing.*;
import java.io.IOException;
import java.util.Collections;

public class SupabaseUserDataAccessObject implements LoginUserDataAccessInterface,
        SignupUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        MarkTaskCompleteDataAccessInterface,
        DeleteTaskDataAccessInterface {

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
          
    @Override
    public void setCurrentUsername(String username) {
        setCurrentUser(username);
    }

    @Override
    public String getCurrentUsername() {
        return getCurrentUser();
    }

    public JsonArray getUsersTasks(String username) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/rest/v1/users?username=eq." + username + "&select=tasks")
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to fetch tasks");
            }

            String body = response.body().string();
            JsonArray users = JsonParser.parseString(body).getAsJsonArray();
            if (users.isEmpty()) return new JsonArray();

            JsonObject user = users.get(0).getAsJsonObject();

            return user.getAsJsonArray("tasks");
        }
    }

    // will implement once TaskInfo and related classes are ready
    private Task convertJsonToTask(JsonObject json) {
        return null;
    }

    // will implement once TaskInfo and related classes are ready
    private JsonObject convertTaskToJson(Task task) {
        return null;
    }

    @Override
    public Task getTaskById(String username, int taskId) throws IOException{

        JsonArray tasks = getUsersTasks(username);
        for (JsonElement element : tasks) {
            JsonObject jsonTask = element.getAsJsonObject();
            if (jsonTask.has("id") && jsonTask.get("id").getAsInt() == taskId) {
                return convertJsonToTask(jsonTask);
            }
        }
        return null;
    }

    public void updateUsersTasks(String username, Task updatedTask) {
        try {
            // Getting all users tasks
            JsonArray tasks = getUsersTasks(username);

            // Looping through tasks, and updating the task if it has same id as updated task
            for (int i = 0; i < tasks.size(); i++) {
                JsonObject task = tasks.get(i).getAsJsonObject();
                if (task.get("id").getAsInt() == updatedTask.getId()) {
                    tasks.set(i, convertTaskToJson(updatedTask));
                    break;
                }
            }


            JsonObject userUpdate = new JsonObject();
            userUpdate.add("tasks", tasks);

            RequestBody body = RequestBody.create(
                    userUpdate.toString(),
                    MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                    .url(baseUrl + "/rest/v1/users?username=eq." + username)
                    .addHeader("apikey", apiKey)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .patch(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Update failed: " + response.message());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error updating task", e);
        }
    }


    @Override
    public void markAsComplete(String username, int taskId) throws IOException {

        // Retrieving task by id if it exists
        Task task = getTaskById(username, taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found.");
        }

        // Setting task as completed
        task.setCompleted(true);

        // Updating status of the task completion in database
        updateUsersTasks(username, task);
    }


    @Override
    public void deleteTask(String username, int taskId) throws IOException {

        // Deleting task locally
        JsonArray tasks = getUsersTasks(username);
        for (int i = 0; i < tasks.size(); i++) {
            JsonObject task = tasks.get(i).getAsJsonObject();
            if (task.get("id").getAsInt() == taskId) {
                tasks.remove(i);
                break;
            }
        }

        //  Updating tasks array in Supabase
        JsonObject userUpdate = new JsonObject();
        userUpdate.add("tasks", tasks);

        RequestBody body = RequestBody.create(
                userUpdate.toString(),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(baseUrl + "/rest/v1/users?username=eq." + username)
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .patch(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Delete failed: " + response.message());
            }
        }
    }
}
