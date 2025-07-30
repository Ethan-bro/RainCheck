package use_case.signup;

import entity.User;
import entity.UserFactory;

import java.util.Set;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    // Allowed email domains list (can be expanded)
    private static final Set<String> allowedDomains = Set.of(
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

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        StringBuilder errorMessages = new StringBuilder();

        String username = signupInputData.getUsername();
        String password = signupInputData.getPassword();
        String repeatPassword = signupInputData.getRepeatPassword();
        String email = signupInputData.getEmail();

        if (email == null || email.isEmpty()) {
            errorMessages.append("Email cannot be empty.\n");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errorMessages.append("Invalid email format.\n");
        } else {
            // Check domain part after '@'
            String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
            if (!allowedDomains.contains(domain)) {
                errorMessages.append("Email domain not allowed.\n");
            }
        }

        if (username == null || username.isEmpty()) {
            errorMessages.append("Username cannot be empty.\n");
        }

        if (password == null || password.isEmpty()) {
            errorMessages.append("Password cannot be empty.\n");
        } else if (!password.equals(repeatPassword)) {
            errorMessages.append("Passwords don't match.\n");
        }

        if (userDataAccessObject.existsByName(username)) {
            errorMessages.append("User already exists.\n");
        }

        if (!errorMessages.isEmpty()) {
            userPresenter.prepareFailView(errorMessages.toString());
        } else {
            User user = userFactory.create(username, password, email);
            userDataAccessObject.save(user);

            SignupOutputData signupOutputData = new SignupOutputData(user.getName(), false);
            userPresenter.prepareSuccessView(signupOutputData);
        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}
