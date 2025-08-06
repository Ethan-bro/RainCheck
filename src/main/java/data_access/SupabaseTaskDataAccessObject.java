package data_access;

import com.google.gson.*;
import entity.*;
import okhttp3.*;
import use_case.deleteTask.DeleteTaskDataAccessInterface;
import use_case.editTask.EditTaskDataAccessInterface;
import use_case.listTasks.TaskDataAccessInterface;
import use_case.markTaskComplete.MarkTaskCompleteDataAccessInterface;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SupabaseTaskDataAccessObject implements
        TaskDataAccessInterface,
        EditTaskDataAccessInterface,
        DeleteTaskDataAccessInterface,
        MarkTaskCompleteDataAccessInterface {

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
        List<Task> tasks = getTasks(username);
        tasks.add(task);
        patchTasks(username, tasks);
    }

    @Override
    public List<Task> getTasksByDateRange(String username, LocalDate start, LocalDate end) {
        List<Task> all = getTasks(username);
        List<Task> filtered = new ArrayList<>();
        for (Task t : all) {
            LocalDate date = t.getTaskInfo().getStartDateTime().toLocalDate();
            if (!date.isBefore(start) && !date.isAfter(end)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    private List<Task> getTasks(String username) {
        try {
            HttpUrl url = Objects.requireNonNull(HttpUrl.parse(baseUrl + "/rest/v1/users"))
                    .newBuilder()
                    .addQueryParameter("username", "eq." + username)
                    .addQueryParameter("select", "tasks")
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("apikey", apiKey)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Accept", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) return new ArrayList<>();
                String body = Objects.requireNonNull(response.body()).string();
                JsonArray array = JsonParser.parseString(body).getAsJsonArray();
                if (array.isEmpty()) return new ArrayList<>();

                JsonObject user = array.get(0).getAsJsonObject();
                if (!user.has("tasks") || user.get("tasks").isJsonNull()) return new ArrayList<>();

                JsonArray taskArray = user.getAsJsonArray("tasks");
                List<Task> result = new ArrayList<>();
                for (JsonElement el : taskArray) {
                    result.add(jsonToTask(el.getAsJsonObject()));
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void patchTasks(String username, List<Task> tasks) {

        try {
            JsonArray taskArray = new JsonArray();
            for (Task task : tasks) {
                taskArray.add(taskToJson(task));
            }

            JsonObject update = new JsonObject();
            update.add("tasks", taskArray);

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

    @Override
    public Task getTaskById(String username, TaskID taskId) {
        for (Task task : getTasks(username)) {
            if (task.getTaskInfo().getId().equals(taskId)) {
                return task;
            }
        }
        return null;
    }

    @Override
    public Task getTaskByIdAndEmail(String email, TaskID id) {
        try {
            // Step 1: Find the user by email to get the username
            HttpUrl url = Objects.requireNonNull(HttpUrl.parse(baseUrl + "/rest/v1/users"))
                    .newBuilder()
                    .addQueryParameter("email", "eq." + email)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("apikey", apiKey)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Accept", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("Failed to find user by email: " + email);
                    return null;
                }

                String body = Objects.requireNonNull(response.body()).string();
                JsonArray array = JsonParser.parseString(body).getAsJsonArray();
                if (array.isEmpty()) {
                    System.err.println("No user found for email: " + email);
                    return null;
                }

                JsonObject user = array.get(0).getAsJsonObject();
                if (!user.has("username") || user.get("username").isJsonNull()) {
                    System.err.println("User record missing username for email: " + email);
                    return null;
                }

                String username = user.get("username").getAsString();

                // Step 2: Get tasks for username
                List<Task> tasks = getTasks(username);

                // Step 3: Find task by id
                for (Task task : tasks) {
                    if (task.getTaskInfo().getId().equals(id)) {
                        return task;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void updateTask(String username, Task updatedTask) {
        List<Task> tasks = getTasks(username);
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
        Task task = getTaskById(username, taskId);
        if (task == null) throw new IllegalArgumentException("Task not found.");
        task.getTaskInfo().setTaskStatus("Complete");
        updateTask(username, task);
    }

    @Override
    public void deleteTask(String username, TaskID taskId) {
        List<Task> tasks = getTasks(username);
        tasks.removeIf(t -> t.getTaskInfo().getId().equals(taskId));
        patchTasks(username, tasks);
    }

    // ---------------------- Helper Conversion ----------------------

    private JsonObject taskToJson(Task task) {
        TaskInfo info = task.getTaskInfo();
        JsonObject json = new JsonObject();

        json.addProperty("id", info.getId().getIDValue().toString());
        json.addProperty("taskName", info.getTaskName());
        json.addProperty("startDateTime", info.getStartDateTime().toString());
        json.addProperty("endDateTime", info.getEndDateTime().toString());
        json.addProperty("taskStatus", info.getTaskStatus());

        if (info.getPriority() != null)
            json.addProperty("priority", info.getPriority().name());

        if (info.getTag() != null) {
            JsonObject tag = new JsonObject();
            tag.addProperty("tagName", info.getTag().getTagName());
            tag.addProperty("tagEmoji", info.getTag().getTagEmoji());
            json.add("customTag", tag);
        }

        if (info.getReminder() != null) {
            JsonObject reminder = new JsonObject();

            int minutesBefore = info.getReminder().getMinutesBefore();

            LocalDateTime reminderTime = info.getStartDateTime()
                    .minusMinutes(minutesBefore);

            reminder.addProperty("remindDateTime", reminderTime.toString());
            json.add("reminder", reminder);
        }

        json.addProperty("weatherDescription", info.getWeatherDescription());
        json.addProperty("weatherIconName", info.getWeatherIconName());
        json.addProperty("temperature", info.getTemperature());
        json.addProperty("uvIndex", info.getUvIndex());

        return json;
    }

    private Task jsonToTask(JsonObject json) {
        String idStr = json.get("id").getAsString();
        String taskName = json.get("taskName").getAsString();
        LocalDateTime start = LocalDateTime.parse(json.get("startDateTime").getAsString());
        LocalDateTime end = LocalDateTime.parse(json.get("endDateTime").getAsString());
        String status = json.get("taskStatus").getAsString();

        Priority priority = null;
        if (json.has("priority") && !json.get("priority").isJsonNull()) {
            priority = Priority.valueOf(json.get("priority").getAsString());
        }

        CustomTag tag = null;
        if (json.has("customTag") && json.get("customTag").isJsonObject()) {
            JsonObject tagJson = json.getAsJsonObject("customTag");
            tag = new CustomTag(tagJson.get("tagName").getAsString(), tagJson.get("tagEmoji").getAsString());
        }

        Reminder reminder = null;
        if (json.has("reminder") && json.get("reminder").isJsonObject()) {
            JsonObject reminderJson = json.getAsJsonObject("reminder");
            LocalDateTime remindTime = LocalDateTime.parse(reminderJson.get("remindDateTime").getAsString());

            long minutesBefore = java.time.Duration.between(remindTime, start).toMinutes();
            reminder = new Reminder((int) minutesBefore);
        }

        String isDeleted = "No";
        if (json.has("isDeleted")) {
            isDeleted = json.get("isDeleted").getAsString();
        }
        String weatherDescription = json.has("weatherDescription") ? json.get("weatherDescription")
                .getAsString() : null;
        String weatherIconName = json.has("weatherIconName") ? json.get("weatherIconName")
                .getAsString() : null;
        String temperature = json.has("temperature") ? json.get("temperature").getAsString() : null;
        String uvIndex = json.has("uvIndex") ? json.get("uvIndex").getAsString() : null;

        TaskInfo info = new TaskInfo(
                TaskID.from(UUID.fromString(idStr)),
                taskName,
                start,
                end,
                priority,
                tag,
                reminder,
                isDeleted,
                weatherDescription,
                weatherIconName,
                temperature,
                uvIndex
        );
        info.setTaskStatus(status);

        return new Task(info);
    }
}
