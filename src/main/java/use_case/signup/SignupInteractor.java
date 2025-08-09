package use_case.signup;

import data_access.DuplicateEmailException;

import entity.User;
import entity.UserFactory;

import java.util.Set;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {

    private static final Set<String> ALLOWED_DOMAINS = Set.of(
            "gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "aol.com",
            "icloud.com", "mail.com", "zoho.com", "protonmail.com", "live.com",
            "msn.com", "comcast.net", "verizon.net", "att.net", "me.com",
            "mail.ru", "yandex.ru", "qq.com", "gmx.com", "cox.net",
            "hotmail.co.uk", "btinternet.com", "sbcglobal.net", "bellsouth.net", "rocketmail.com",
            "aim.com", "yahoo.co.uk", "mac.com", "earthlink.net", "charter.net",
            "shaw.ca", "telus.net", "sympatico.ca", "rogers.com", "mail.utoronto.ca",
            "live.ca", "googlemail.com", "facebook.com", "verizon.com", "bt.com",
            "inbox.com", "web.de", "naver.com", "hanmail.net", "qqmail.com",
            "mailinator.com", "fastmail.com", "tutanota.com", "hushmail.com", "posteo.de"
    );

    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public SignupInteractor(final SignupUserDataAccessInterface signupDataAccessInterface,
                            final SignupOutputBoundary signupOutputBoundary,
                            final UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

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

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }

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

    private String validateUsername(final String username) {
        String usernameErrorMessage = "";
        if (username == null || username.isEmpty()) {
            usernameErrorMessage = "Username cannot be empty.\n";
        }
        return usernameErrorMessage;
    }

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

    private String checkUserExists(final String username) {
        String errorMessage = "";
        if (userDataAccessObject.existsByName(username)) {
            errorMessage = "User already exists.\n";
        }
        return errorMessage;
    }
}
