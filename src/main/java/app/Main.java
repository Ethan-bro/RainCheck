package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            JFrame application = new AppBuilder()
                    .addDatabase()
                    .addViewModels()
                    .addSignupView()
                    .addLoginView()
                    .addLoggedInView()
                    .build();

            application.pack();
            application.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
