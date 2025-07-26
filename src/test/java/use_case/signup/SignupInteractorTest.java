package use_case.signup;

import data_access.InMemoryUserDataAccessObject;
import entity.CommonUserFactory;
import entity.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SignupInteractorTest {

    @Test
    void successSignup() {
        SignupUserDataAccessInterface dao = new InMemoryUserDataAccessObject();
        UserFactory userFactory = new CommonUserFactory();

        SignupInputData inputData = new SignupInputData("Alice", "pass123", "pass123");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                Assertions.assertEquals("Alice", data.getUsername());
                Assertions.assertTrue(dao.existsByName("Alice"));
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.fail("Unexpected failure: " + error);
            }

            @Override
            public void switchToLoginView() {
                // we do not need to provide an implementation for this method
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, userFactory);
        interactor.execute(inputData);
    }

    @Test
    void passwordMismatchFails() {
        SignupUserDataAccessInterface dao = new InMemoryUserDataAccessObject();
        UserFactory userFactory = new CommonUserFactory();

        SignupInputData inputData = new SignupInputData("Alice", "pass123", "wrong");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                Assertions.fail("Should not succeed with mismatched passwords");
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertEquals("Passwords don't match.", error.trim());
            }

            @Override
            public void switchToLoginView() {
                // we do not need to provide an implementation for this method
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, userFactory);
        interactor.execute(inputData);
    }

    @Test
    void userAlreadyExistsFails() {
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();
        dao.save(factory.create("Alice", "existing"));

        SignupInputData inputData = new SignupInputData("Alice", "pass123", "pass123");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                Assertions.fail("Should not succeed if user exists");
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertEquals("User already exists.", error.trim());
            }

            @Override
            public void switchToLoginView() {
                // we do not need to provide an implementation for this method
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, factory);
        interactor.execute(inputData);
    }
}
