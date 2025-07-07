package data_access;

import entity.CommonUser;
import entity.User;
import use_case.login.LoginUserDataAccessInterface;

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
public class FileUserDataAccessObject implements LoginUserDataAccessInterface {

    private static final String USER_DATA_FOLDER = "userdata";
    private String currentUser = null;

    /**
     * Checks if a user file exists for the given username.
     * @param username the username to check
     * @return true if the user JSON file exists, false otherwise
     */
    @Override
    public boolean existsByName(String username) {
        Path userFile = Paths.get(USER_DATA_FOLDER, username + ".json");
        return Files.exists(userFile);
    }

    /**
     * Loads a User object from its JSON file.
     * @param username the username of the user to load
     * @return a User object if found and parsed, null otherwise
     */
    @Override
    public User get(String username) {
        Path userFilePath = Paths.get(USER_DATA_FOLDER, username + ".json");

        if (!Files.exists(userFilePath)) {
            return null;
        }

        try {
            // Read entire JSON content as a String
            String content = Files.readString(userFilePath);

            // Parse JSON string into JSONObject
            JSONObject json = new JSONObject(content);

            // Extract fields from JSON
            String userName = json.getString("username");
            String passwordHash = json.getString("passwordHash");

            // TODO: Extend here to read the json and all that is needed

            // Create and return a User instance from JSON data
            return new CommonUser(userName, passwordHash);

        } catch (IOException e) {
            System.err.println("Failed to read file for user '" + username + "': " + e.getMessage());
            return null;
        } catch (org.json.JSONException _) {
            // Malformed JSON file, print error and return null
            System.err.println("Malformed JSON for user: " + username);
            return null;
        }
    }

    /**
     * Saves or updates the User data into a JSON file.
     * Currently not implemented — to be added later.
     * @param user the User object to save
     */
    @Override
    public void save(User user) {
        // TODO: Implement writing the User object back to JSON file
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
}
