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
                    .addListTasksUseCase()
                    .addLoggedInView()
                    .addAddTaskView()
                    .build();

            int appWidth = 860;
            application.setSize(appWidth, (int) (appWidth / 1.4)); // Making the app proportional based on it's width
            application.setLocationRelativeTo(null); // centers window
            application.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void incrementNumOfAPIcallsMade() {
        numOfAPIcallsMade ++;
    }

    public static int getNumOfAPIcallsMade() {
        return numOfAPIcallsMade;
    }
}
