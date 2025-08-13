package use_case.signup;

/**
 * Input Boundary for actions which are related to signing up.
 */
public interface SignupInputBoundary {

    /**
     * Executes the signup use case.
     * @param data the input data
     */
    void execute(SignupInputData data);

    /**
     * Executes the switch to login view use case.
     */
    void switchToLoginView();
}
