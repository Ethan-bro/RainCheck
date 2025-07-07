package data_access;

import entity.CommonUser;
import entity.User;
import entity.Task;

import services.PasswordHashingService;

import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File-based implementation of LoginUserDataAccessInterface.
 * Handles loading and saving User data as JSON files in the userdata directory.
 * This implementation reads user data from JSON files on disk.
 */
public class FileUserDataAccessObject implements LoginUserDataAccessInterface, SignupUserDataAccessInterface {

    private final String userDataFolder;
    private String currentUser = null;

    /**
     * Creates a FileUserDataAccessObject that loads user data from the given folder.
     *
     * @param userDataFolder the folder where user JSON files are stored (e.g., "userdata")
     */
    public FileUserDataAccessObject(String userDataFolder) {
        this.userDataFolder = userDataFolder;
    }

    /**
     * Checks if a user file exists for the given username.
     * @param username the username to check
     * @return true if the user JSON file exists, false otherwise
     */
    @Override
    public boolean existsByName(String username) {
        Path userFile = Paths.get(userDataFolder, username + ".json");
        return Files.exists(userFile);
    }

    /**
     * Loads a User object from its JSON file.
     * @param username the username of the user to load
     * @return a User object if found and parsed, null otherwise
     */
    @Override
    public User get(String username) {
        Path userFilePath = Paths.get(userDataFolder, username + ".json");

        if (!Files.exists(userFilePath)) {
            return null;
        }

        try {
            String content = Files.readString(userFilePath);
            JSONObject json = new JSONObject(content);

            String userName = json.getString("username");
            String passwordHash = json.getString("passwordHash");

            return new CommonUser(userName, passwordHash);

        } catch (IOException e) {
            System.err.println("Failed to read file for user '" + username + "': " + e.getMessage());
            return null;
        } catch (org.json.JSONException e) {
            System.err.println("Malformed JSON for user: " + username);
            return null;
        }
    }

    /**
     * Saves or updates the User data into a JSON file.
     * @param user the User object to save
     */
    @Override
    public void save(User user) {
        Path userFilePath = Paths.get(userDataFolder, user.getName() + ".json");
        JSONObject json = new JSONObject();
        json.put("username", user.getName());
        json.put("passwordHash", user.getPasswordHash());

        try {
            // Ensure the directory exists
            Files.createDirectories(userFilePath.getParent());
            Files.writeString(userFilePath, json.toString(4)); // pretty print JSON with indentation
        } catch (IOException e) {
            System.err.println("Failed to save user '" + user.getName() + "': " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the currently logged-in user's username.
     * @param username the username to set as current
     */
    @Override
    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    /**
     * Returns the username of the current logged-in user.
     * @return the current user's username, or null if no user logged in
     */
    @Override
    public String getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks if the username is valid (i.e., not already taken).
     * @param username the username to check
     * @return true if the username is available for registration; false otherwise
     */
    @Override
    public boolean isUsernameValid(String username) {
        return !existsByName(username);
    }

    /**
     * Adds a task for the specified user.
     * @param username the username to add the task for
     * @param task the Task object representing the task to be added
     */
    public void addTask(String username, Task task) {
        // TODO: Implement this method to save the task for the given user
    }
}