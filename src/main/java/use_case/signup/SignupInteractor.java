package use_case.signup;

import entity.User;
import entity.UserFactory;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

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

        if (username.isEmpty()) {
            errorMessages.append("Username cannot be empty.\n");
        }

        if (password.isEmpty()) {
            errorMessages.append("Password cannot be empty.\n");
        }

        if (userDataAccessObject.existsByName(username)) {
            errorMessages.append("User already exists.\n");
        }

        if (!password.equals(repeatPassword)) {
            errorMessages.append("Passwords don't match.\n");
        }

        if (!errorMessages.isEmpty()) {
            userPresenter.prepareFailView(errorMessages.toString());
        } else {
            User user = userFactory.create(username, password);
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
