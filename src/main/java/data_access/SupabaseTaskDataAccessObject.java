package data_access;

import entity.CustomTag;
import entity.Priority;
import entity.Reminder;
import entity.Task;
import entity.TaskID;
import entity.TaskInfo;

import use_case.deleteTask.DeleteTaskDataAccessInterface;
import use_case.editTask.EditTaskDataAccessInterface;
import use_case.listTasks.TaskDataAccessInterface;
import use_case.markTaskComplete.MarkTaskCompleteDataAccessInterface;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

public class SupabaseTaskDataAccessObject implements
        TaskDataAccessInterface,
        EditTaskDataAccessInterface,
        DeleteTaskDataAccessInterface,
        MarkTaskCompleteDataAccessInterface {

    private static final String USERNAME = "username";
    private static final String USERNAME_QUERY_PARAM = "username";
    private static final String TASKS_FIELD = "tasks";
    private static final String API_KEY_HEADER = "apikey";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String APPLICATION_JSON = "application/json";

    private static final String PRIORITY = "priority";
    private static final String CUSTOM_TAG = "customTag";
    private static final String REMINDER = "reminder";
    private static final String WEATHER_DESCRIPTION = "weatherDescription";
    private static final String WEATHER_ICON_NAME = "weatherIconName";
    private static final String TEMPERATURE = "temperature";
    private static final String UV_INDEX = "uvindex";

    private final String baseUrl;
    private final String apiKey;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public SupabaseTaskDataAccessObject(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Override
    public void addTask(String username, Task task) {
        final List<Task> tasks = getTasks(username);
        tasks.add(task);
        patchTasks(username, tasks);
    }

    @Override
    public List<Task> getTasksByDateRange(String username, LocalDate start, LocalDate end) {
        final List<Task> filtered = new ArrayList<>();
        final List<Task> all = getTasks(username);

        for (final Task task : all) {
            final LocalDate date = task.getTaskInfo().getStartDateTime().toLocalDate();
            if (!date.isBefore(start) && !date.isAfter(end)) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    private List<Task> getTasks(String username) {
        final List<Task> result = new ArrayList<>();

        try {
            final JsonArray array = fetchUserData(username);

            if (array != null && !array.isEmpty()) {
                final JsonObject user = array.get(0).getAsJsonObject();

                if (user.has(TASKS_FIELD) && !user.get(TASKS_FIELD).isJsonNull()) {
                    final JsonArray taskArray = user.getAsJsonArray(TASKS_FIELD);

                    for (final JsonElement element : taskArray) {
                        result.add(jsonToTask(element.getAsJsonObject()));
                    }
                }
            }
        }
        catch (IOException ioEx) {
            ioEx.printStackTrace();
        }

        return result;
    }

    private JsonArray fetchUserData(String username) throws IOException {
        final HttpUrl url = Objects.requireNonNull(HttpUrl.parse(baseUrl + "/rest/v1/users"))
                .newBuilder()
                .addQueryParameter(USERNAME_QUERY_PARAM, "eq." + username)
                .addQueryParameter("select", TASKS_FIELD)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .addHeader(API_KEY_HEADER, apiKey)
                .addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + apiKey)
                .addHeader("Accept", APPLICATION_JSON)
                .build();

        JsonArray result = null;

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                final String body = Objects.requireNonNull(response.body()).string();
                result = JsonParser.parseString(body).getAsJsonArray();
            }
        }

        return result;
    }

    private void patchTasks(String username, List<Task> tasks) {
        final JsonArray taskArray = new JsonArray();

        for (final Task task : tasks) {
            taskArray.add(taskToJson(task));
        }

        final JsonObject update = new JsonObject();
        update.add(TASKS_FIELD, taskArray);

        final RequestBody body = RequestBody.create(gson.toJson(update), MediaType.get(APPLICATION_JSON));

        final Request request = new Request.Builder()
                .url(baseUrl + "/rest/v1/users?" + USERNAME_QUERY_PARAM + "=eq." + username)
                .addHeader(API_KEY_HEADER, apiKey)
                .addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + apiKey)
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

    @Override
    public Task getTaskById(String username, TaskID taskId) {
        Task foundTask = null;

        for (final Task task : getTasks(username)) {
            if (task.getTaskInfo().getId().equals(taskId)) {
                foundTask = task;
                break;
            }
        }

        return foundTask;
    }

    @Override
    public Task getTaskByIdAndEmail(String email, TaskID id) {
        Task foundTask = null;

        try {
            final HttpUrl url = Objects.requireNonNull(HttpUrl.parse(baseUrl + "/rest/v1/users"))
                    .newBuilder()
                    .addQueryParameter("email", "eq." + email)
                    .build();

            final Request request = new Request.Builder()
                    .url(url)
                    .addHeader(API_KEY_HEADER, apiKey)
                    .addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + apiKey)
                    .addHeader("Accept", APPLICATION_JSON)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("Failed to find user by email: " + email);
                }
                else {
                    final String body = Objects.requireNonNull(response.body()).string();
                    final JsonArray array = JsonParser.parseString(body).getAsJsonArray();

                    if (array.isEmpty()) {
                        System.err.println("No user found for email: " + email);
                    }
                    else {
                        final JsonObject user = array.get(0).getAsJsonObject();

                        if (!user.has(USERNAME) || user.get(USERNAME).isJsonNull()) {
                            System.err.println("User record missing username for email: " + email);
                        }
                        else {
                            final String username = user.get(USERNAME).getAsString();
                            foundTask = getTaskById(username, id);
                        }
                    }
                }
            }
        }
        catch (IOException ioEx) {
            ioEx.printStackTrace();
        }

        return foundTask;
    }

    @Override
    public void updateTask(String username, Task updatedTask) {
        final List<Task> tasks = getTasks(username);

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskInfo().getId().equals(updatedTask.getTaskInfo().getId())) {
                tasks.set(i, updatedTask);
                break;
            }
        }
        patchTasks(username, tasks);
    }

    @Override
    public void markAsComplete(String username, TaskID taskId) {
        final Task task = getTaskById(username, taskId);

        if (task == null) {
            throw new IllegalArgumentException("Task not found.");
        }

        task.getTaskInfo().setTaskStatus("Complete");
        updateTask(username, task);
    }

    @Override
    public void deleteTask(String username, TaskID taskId) {
        final List<Task> tasks = getTasks(username);
        tasks.removeIf(task -> task.getTaskInfo().getId().equals(taskId));
        patchTasks(username, tasks);
    }

    private JsonObject taskToJson(Task task) {
        final TaskInfo info = task.getTaskInfo();
        final JsonObject json = new JsonObject();

        json.addProperty("id", info.getId().getIDValue().toString());
        json.addProperty("taskName", info.getTaskName());
        json.addProperty("startDateTime", info.getStartDateTime().toString());
        json.addProperty("endDateTime", info.getEndDateTime().toString());
        json.addProperty("taskStatus", info.getTaskStatus());

        if (info.getPriority() != null) {
            json.addProperty(PRIORITY, info.getPriority().name());
        }

        if (info.getTag() != null) {
            final JsonObject tag = new JsonObject();
            tag.addProperty("tagName", info.getTag().getTagName());
            tag.addProperty("tagEmoji", info.getTag().getTagIcon());
            json.add(CUSTOM_TAG, tag);
        }

        if (info.getReminder() != null) {
            final JsonObject reminder = new JsonObject();
            final int minutesBefore = info.getReminder().getMinutesBefore();
            final LocalDateTime reminderTime = info.getStartDateTime().minusMinutes(minutesBefore);
            reminder.addProperty("remindDateTime", reminderTime.toString());
            json.add(REMINDER, reminder);
        }

        json.addProperty(WEATHER_DESCRIPTION, info.getWeatherDescription());
        json.addProperty(WEATHER_ICON_NAME, info.getWeatherIconName());
        json.addProperty(TEMPERATURE, info.getTemperature());
        json.addProperty(UV_INDEX, info.getUvIndex());

        return json;
    }

    private Task jsonToTask(JsonObject json) {
        final String idStr = getString(json, "id");
        final String taskName = getString(json, "taskName");
        final LocalDateTime start = LocalDateTime.parse(getString(json, "startDateTime"));
        final LocalDateTime end = LocalDateTime.parse(getString(json, "endDateTime"));
        final String status = getString(json, "taskStatus");

        Priority priority = null;

        if (json.has(PRIORITY) && !json.get(PRIORITY).isJsonNull()) {
            priority = Priority.valueOf(getString(json, PRIORITY));
        }

        CustomTag tag = null;

        if (json.has(CUSTOM_TAG) && json.get(CUSTOM_TAG).isJsonObject()) {
            final JsonObject tagJson = json.getAsJsonObject(CUSTOM_TAG);
            tag = new CustomTag(getString(tagJson, "tagName"), getString(tagJson, "tagEmoji"));
        }

        Reminder reminder = null;

        if (json.has(REMINDER) && json.get(REMINDER).isJsonObject()) {
            final JsonObject reminderJson = json.getAsJsonObject(REMINDER);
            final LocalDateTime remindTime = LocalDateTime.parse(getString(reminderJson, "remindDateTime"));
            final long minutesBefore = java.time.Duration.between(remindTime, start).toMinutes();
            reminder = new Reminder((int) minutesBefore);
        }

        final String isDeleted;

        if (json.has("isDeleted")) {
            isDeleted = getString(json, "isDeleted");
        }
        else {
            isDeleted = "No";
        }

        final TaskInfo info = new TaskInfo();

        info.setCoreDetails(
                TaskID.from(UUID.fromString(idStr)),
                taskName,
                start,
                end
        );

        info.setAdditionalDetails(
                priority,
                tag,
                reminder,
                isDeleted
        );

        info.setWeatherInfo(
                getNullableString(json, WEATHER_DESCRIPTION),
                getNullableString(json, WEATHER_ICON_NAME),
                getNullableString(json, TEMPERATURE),
                getNullableString(json, UV_INDEX)
        );

        info.setTaskStatus(status);

        return new Task(info);
    }

    /**
     * Retrieves the value of the specified member from the given JsonObject as a String.
     * If the member exists and is not null, this method returns its string representation.
     * It supports JSON primitives of type String or Number, converting numbers to their
     * string form. If the member does not exist or is null, this method returns null.
     *
     * @param json       the JsonObject from which to retrieve the value
     * @param memberName the name of the member to retrieve
     * @return the String representation of the member's value, or null if the member
     *         does not exist or is null
     */
    private String getString(JsonObject json, String memberName) {
        String returnString = null;

        if (json.has(memberName) && !json.get(memberName).isJsonNull()) {
            final JsonElement elem = json.get(memberName);
            if (elem.isJsonPrimitive()) {
                if (elem.getAsJsonPrimitive().isString()) {
                    returnString = elem.getAsString();
                }
                else if (elem.getAsJsonPrimitive().isNumber()) {
                    returnString = elem.getAsNumber().toString();
                }
            }
        }
        return returnString;
    }

    private String getNullableString(JsonObject json, String memberName) {
        return getString(json, memberName);
    }
}
