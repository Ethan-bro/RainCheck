package interface_adapter.signup;

import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

/**
 * Controller for the Signup Use Case.
 */
public class SignupController {

    private final SignupInputBoundary userSignupUseCaseInteractor;


    /**
     * Constructs a SignupController with the given use case interactor.
     *
     * @param userSignupUseCaseInteractor the interactor for the signup use case
     */
    public SignupController(SignupInputBoundary userSignupUseCaseInteractor) {
        this.userSignupUseCaseInteractor = userSignupUseCaseInteractor;
    }


    /**
     * Executes the signup use case.
     *
     * @param username the username to sign up
     * @param password1 the password
     * @param password2 the password repeated
     * @param email the user's email
     */
    public void execute(String username, String password1, String password2, String email) {
        final SignupInputData signupInputData = new SignupInputData(
                username, password1, password2, email);
        userSignupUseCaseInteractor.execute(signupInputData);
    }


    /**
     * Executes the switch to login view use case.
     */
    public void switchToLoginView() {
        userSignupUseCaseInteractor.switchToLoginView();
    }
}
