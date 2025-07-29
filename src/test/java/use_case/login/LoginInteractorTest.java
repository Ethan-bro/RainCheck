package use_case.login;

import data_access.InMemoryUserDataAccessObject;
import entity.CommonUserFactory;
import entity.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LoginInteractorTest {

    @Test
    void successfulLogin() {
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();
        dao.save(factory.create("Bob", "abc123"));

        LoginInputData input = new LoginInputData("Bob", "abc123");

        LoginOutputBoundary presenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData data) {
                Assertions.assertEquals("Bob", data.getUsername());
                Assertions.assertEquals("Bob", dao.getCurrentUser());
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.fail("Should not fail: " + error);
            }

            @Override
            public void switchToSignupView() {
                // we do not need to provide an implementation for this method
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(dao, presenter);
        interactor.execute(input);
    }

    @Test
    void wrongPasswordFails() {
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();
        dao.save(factory.create("Bob", "abc123"));

        LoginInputData input = new LoginInputData("Bob", "wrong");

        LoginOutputBoundary presenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData data) {
                Assertions.fail("Should fail on wrong password");
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertEquals("Incorrect password for \"Bob\".", error);
            }

            @Override
            public void switchToSignupView() {
                // we do not need to provide an implementation for this method
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(dao, presenter);
        interactor.execute(input);
    }

    @Test
    void userNotFoundFails() {
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        LoginInputData input = new LoginInputData("Ghost", "123");

        LoginOutputBoundary presenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData data) {
                Assertions.fail("Should fail: user does not exist");
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertEquals("Ghost: Account does not exist.", error);
            }

            @Override
            public void switchToSignupView() {
                // we do not need to provide an implementation for this method
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(dao, presenter);
        interactor.execute(input);
    }
}
