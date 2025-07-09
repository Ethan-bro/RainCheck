package use_case.signup;

import entity.User;
import entity.UserFactory;
import services.PasswordHashingService;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;
    private final PasswordHashingService passwordHashingService;

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory,
                            PasswordHashingService passwordHashingService) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
        this.passwordHashingService = passwordHashingService;
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        StringBuilder errorMessages = new StringBuilder();

        if (signupInputData.getUsername().isEmpty()) {
            errorMessages.append("Username cannot be empty.\n");
        }

        if (signupInputData.getPassword().isEmpty()) {
            errorMessages.append("Password cannot be empty.\n");
        }

        if (userDataAccessObject.existsByName(signupInputData.getUsername())) {
            errorMessages.append("User already exists.\n");
        }

        if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())) {
            errorMessages.append("Passwords don't match.\n");
        }

        if (!errorMessages.isEmpty()) {
            userPresenter.prepareFailView(errorMessages.toString());
        } else {
            final String hashedPassword = passwordHashingService.hash(signupInputData.getPassword());
            final User user = userFactory.create(signupInputData.getUsername(), hashedPassword);
            userDataAccessObject.save(user);

            final SignupOutputData signupOutputData = new SignupOutputData(user.getName(), false);
            userPresenter.prepareSuccessView(signupOutputData);
        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}