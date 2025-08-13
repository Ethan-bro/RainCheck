package use_case.signup;

import java.util.Set;

import data_access.DuplicateEmailException;
import entity.User;
import entity.UserFactory;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {

    /*
     * Changing the allowed email domains set to only include "gmail.com" because:
     *
     * The email configuration currently supports only Gmail accounts using a 16-character
     * Google App Password. This specialized password setup is specific to Google accounts,
     * and other domains do not support or require this kind of app-specific password.
     */
    private static final Set<String> ALLOWED_DOMAINS = Set.of("gmail.com");

    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    /**
     * Constructs a SignupInteractor.
     *
     * @param signupDataAccessInterface the data access interface for user data
     * @param signupOutputBoundary the output boundary to present results
     * @param userFactory the factory for creating User objects
     */
    public SignupInteractor(final SignupUserDataAccessInterface signupDataAccessInterface,
                            final SignupOutputBoundary signupOutputBoundary,
                            final UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    /**
     * Executes the use case for user signup.
     *
     * @param signupInputData the input data containing username, password, and email
     */
    @Override
    public void execute(final SignupInputData signupInputData) {
        final StringBuilder errorMessages = new StringBuilder();

        final String username = signupInputData.getUsername();
        final String password = signupInputData.getPassword();
        final String repeatPassword = signupInputData.getRepeatPassword();
        final String email = signupInputData.getEmail();

        errorMessages.append(validateEmail(email));
        errorMessages.append(validateUsername(username));
        errorMessages.append(validatePassword(password, repeatPassword));
        errorMessages.append(checkUserExists(username));

        if (!errorMessages.isEmpty()) {
            userPresenter.prepareFailView(errorMessages.toString());
        }
        else {
            final User user = userFactory.create(username, password, email);
            try {
                userDataAccessObject.save(user);
                final SignupOutputData signupOutputData = new SignupOutputData(user.getName(), false);
                userPresenter.prepareSuccessView(signupOutputData);
            }
            catch (DuplicateEmailException duplicateEmailEx) {
                userPresenter.prepareFailView("That email is already registered.");
            }
        }
    }

    /**
     * Switches the view to the login screen.
     */
    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }

    /**
     * Validates the email address for format and allowed domain.
     *
     * @param email the email address to validate
     * @return an error message if invalid, otherwise an empty string
     */
    private String validateEmail(final String email) {
        String errorMessage = "";

        if (email == null || email.isEmpty()) {
            errorMessage = "Email cannot be empty.\n";
        }
        else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errorMessage = "Invalid email format.\n";
        }
        else {
            final String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
            if (!ALLOWED_DOMAINS.contains(domain)) {
                errorMessage = "Email domain not allowed.\n";
            }
        }

        return errorMessage;
    }

    /**
     * Validates the username for non-emptiness.
     *
     * @param username the username to validate
     * @return an error message if invalid, otherwise an empty string
     */
    private String validateUsername(final String username) {
        String usernameErrorMessage = "";
        if (username == null || username.isEmpty()) {
            usernameErrorMessage = "Username cannot be empty.\n";
        }
        return usernameErrorMessage;
    }

    /**
     * Validates the password and repeated password for non-emptiness and match.
     *
     * @param password the password to validate
     * @param repeatPassword the repeated password to validate
     * @return an error message if invalid, otherwise an empty string
     */
    private String validatePassword(final String password, final String repeatPassword) {
        String passwordsErrorMessage = "";
        if (password == null || password.isEmpty()) {
            passwordsErrorMessage = "Password cannot be empty.\n";
        }
        else if (!password.equals(repeatPassword)) {
            passwordsErrorMessage = "Passwords don't match.\n";
        }
        return passwordsErrorMessage;
    }

    /**
     * Checks if a user already exists by username.
     *
     * @param username the username to check
     * @return an error message if user exists, otherwise an empty string
     */
    private String checkUserExists(final String username) {
        String errorMessage = "";
        if (userDataAccessObject.existsByName(username)) {
            errorMessage = "User already exists.\n";
        }
        return errorMessage;
    }
}
