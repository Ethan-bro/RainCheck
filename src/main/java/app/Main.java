package app;

import javax.swing.*;

public class Main {

    public static int numOfAPIcallsMade = 0;

    public static void main(String[] args) {
        try {
            JFrame application = new AppBuilder()
                    .addDatabase()
                    .addViewModels()
                    .addSignupView()
                    .addLoginView()
                    .addCCTView()
                    .addEditTagView()
                    .addManageTagsView()
                    .addEditTaskView()
                    .addListTasksUseCase()
                    .addAddTaskView()
                    .addLoggedInView()
                    .build();

            int appWidth = 860;
            // Making the app proportional based on it's width
            application.setSize(appWidth, (int) (appWidth / 1.4));
            // centers window
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void incrementNumOfApiCallsMade() {
        numOfAPIcallsMade++;
    }

    public static int getNumOfAPIcallsMade() {
        return numOfAPIcallsMade;
    }
}
