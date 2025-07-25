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
import java.io.IOException;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private SupabaseUserDataAccessObject userDao;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private SignupViewModel signupViewModel;
    private LoginView loginView;
    private SignupView signupView;
    private LoggedInView loggedInView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addDatabase() throws Exception {
        JsonObject config = JsonParser.parseReader(new FileReader("config/secrets.json")).getAsJsonObject();
        String dbUrl = config.get("database_url").getAsString();
        String dbAnonKey = config.get("database_anon_key").getAsString();
        userDao = new SupabaseUserDataAccessObject(dbUrl, dbAnonKey);
        return this;
    }

    public AppBuilder addViewModels() {
        loginViewModel = new LoginViewModel();
        loggedInViewModel = new LoggedInViewModel();
        signupViewModel = new SignupViewModel();
        return this;
    }

    public AppBuilder addSignupView() {
        signupView = SignupUseCaseFactory.create(viewManagerModel, loginViewModel, signupViewModel, userDao);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginView = LoginUseCaseFactory.create(viewManagerModel, loginViewModel, loggedInViewModel, signupViewModel, userDao);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() throws IOException {
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("RainCheck");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
