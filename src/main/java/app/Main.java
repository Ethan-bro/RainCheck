package app;

import java.io.IOException;

import javax.swing.JFrame;

/**
 * Main application entry point.
 */
public class Main {

    private static int numOfApiCallsMade;

    /**
     * Main method to start the application.
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        try {
            final JFrame application = new AppBuilder()
                    .addDatabase()
                    .addViewModels()
                    .addSignupView()
                    .addLoginView()
                    .addCctView()
                    .addEditTagView()
                    .addManageTagsView()
                    .addEditTaskView()
                    .addAddTaskView()
                    .addLoggedInView()
                    .addGmailInstructionsView()
                    .addListTasksUseCase()
                    .build();

            final int appWidth = 860;
            final double aspectRatio = 1.4;

            application.setSize(appWidth, (int) (appWidth / aspectRatio));
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        }
        catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    /**
     * Increment the count of API calls made by one.
     */
    public static void incrementNumOfApiCallsMade() {
        numOfApiCallsMade++;
    }

    /**
     * Get the current count of API calls made.
     * @return number of API calls made
     */
    public static int getNumOfApiCallsMade() {
        return numOfApiCallsMade;
    }
}
