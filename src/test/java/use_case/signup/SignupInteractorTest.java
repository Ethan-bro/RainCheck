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

        SignupInputData inputData = new SignupInputData("Alice", "pass123", "pass123", "alice@mail.utoronto.ca");

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
                // Not needed for this test
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, userFactory);
        interactor.execute(inputData);
    }

    @Test
    void passwordMismatchFails() {
        SignupUserDataAccessInterface dao = new InMemoryUserDataAccessObject();
        UserFactory userFactory = new CommonUserFactory();

        SignupInputData inputData = new SignupInputData("Bob", "pass123", "wrong", "bob@gmail.com");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                Assertions.fail("Should not succeed with mismatched passwords");
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertTrue(error.contains("Passwords don't match"));
            }

            @Override
            public void switchToLoginView() {
                // Not needed for this test
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, userFactory);
        interactor.execute(inputData);
    }

    @Test
    void userAlreadyExistsFails() {
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();
        dao.save(factory.create("Charlie", "existing", "charlie@mail.utoronto.ca"));

        SignupInputData inputData = new SignupInputData("Charlie", "pass123", "pass123", "charlie@mail.utoronto.ca");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                Assertions.fail("Should not succeed if user exists");
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertTrue(error.contains("User already exists"));
            }

            @Override
            public void switchToLoginView() {
                // Not needed for this test
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, factory);
        interactor.execute(inputData);
    }

    @Test
    void invalidEmailFails() {
        SignupUserDataAccessInterface dao = new InMemoryUserDataAccessObject();
        UserFactory userFactory = new CommonUserFactory();

        SignupInputData inputData = new SignupInputData("David", "pass123", "pass123", "invalid-email");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                Assertions.fail("Should not succeed with invalid email");
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertTrue(error.contains("Invalid email format"));
            }

            @Override
            public void switchToLoginView() {
                // Not needed for this test
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, userFactory);
        interactor.execute(inputData);
    }
}
