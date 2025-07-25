package app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import data_access.SupabaseUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

import javax.swing.*;
import java.awt.CardLayout;
import java.io.FileReader;

public class MainWithDBStorage {
    public static void main(String[] args) throws Exception {

        // Load db config
        JsonObject config = JsonParser.parseReader(new FileReader("config/secrets.json")).getAsJsonObject();
        String dbUrl = config.get("database_url").getAsString();
        String dbAnonKey = config.get("database_anon_key").getAsString();

        SupabaseUserDataAccessObject userDao = new SupabaseUserDataAccessObject(dbUrl, dbAnonKey);

        // --- GUI setup ---
        final JFrame application = new JFrame("RainCheck");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final CardLayout cardLayout = new CardLayout();
        final JPanel views = new JPanel(cardLayout);
        application.add(views);

        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        final LoginViewModel loginViewModel = new LoginViewModel();
        final LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        final SignupViewModel signupViewModel = new SignupViewModel();

        final SignupView signupView = SignupUseCaseFactory.create(
                viewManagerModel, loginViewModel, signupViewModel, userDao);
        views.add(signupView, signupView.getViewName());

        final LoginView loginView = LoginUseCaseFactory.create(
                viewManagerModel, loginViewModel, loggedInViewModel, signupViewModel, userDao);
        views.add(loginView, loginView.getViewName());

        final LoggedInView loggedInView = new LoggedInView(loggedInViewModel);
        views.add(loggedInView, loggedInView.getViewName());

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setVisible(true);
    }
}
