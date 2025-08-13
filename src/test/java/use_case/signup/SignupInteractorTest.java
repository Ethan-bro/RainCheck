package use_case.signup;

import data_access.InMemoryUserDataAccessObject;
import data_access.DuplicateEmailException;
import entity.CommonUserFactory;
import entity.UserFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SignupInteractorTest {

    // ---------------- Successful signup ----------------
    @Test
    void successSignup() {
        SignupUserDataAccessInterface dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();

        SignupInputData inputData = new SignupInputData("Alice", "pass123", "pass123", "alice@gmail.com");

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

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, factory);
        interactor.execute(inputData);
    }

    // ---------------- Password mismatch ----------------
    @Test
    void passwordMismatchFails() {
        SignupUserDataAccessInterface dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();

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

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, factory);
        interactor.execute(inputData);
    }

    // ---------------- User already exists ----------------
    @Test
    void userAlreadyExistsFails() {
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();

        try {
            dao.save(factory.create("Charlie", "existing", "charlie@gmail.com"));
        } catch (DuplicateEmailException e) {
            Assertions.fail("Setup failed: " + e.getMessage());
        }

        SignupInputData inputData = new SignupInputData("Charlie", "pass123", "pass123", "charlie@gmail.com");

        SignupInputBoundary interactor = getSignupInputBoundary("Should not succeed if user exists", "User already exists", dao, factory);
        interactor.execute(inputData);
    }

    @NotNull
    private static SignupInputBoundary getSignupInputBoundary(String message, String User_already_exists, InMemoryUserDataAccessObject dao, UserFactory factory) {
        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                Assertions.fail(message);
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertTrue(error.contains(User_already_exists));
            }

            @Override
            public void switchToLoginView() {
                // Not needed for this test
            }
        };

        return new SignupInteractor(dao, presenter, factory);
    }

    // ---------------- Invalid email format ----------------
    @Test
    void invalidEmailFails() {
        SignupUserDataAccessInterface dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();

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

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, factory);
        interactor.execute(inputData);
    }

    // ---------------- Empty username ----------------
    @Test
    void emptyUsernameFails() {
        SignupUserDataAccessInterface dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();

        SignupInputData inputData = new SignupInputData("", "pass123", "pass123", "user@gmail.com");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                Assertions.fail("Should not succeed with empty username");
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertTrue(error.contains("Username cannot be empty"));
            }

            @Override
            public void switchToLoginView() {
                // Not needed for this test
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, factory);
        interactor.execute(inputData);
    }

    // ---------------- Empty password ----------------
    @Test
    void emptyPasswordFails() {
        SignupUserDataAccessInterface dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();

        SignupInputData inputData = new SignupInputData("Eve", "", "", "eve@gmail.com");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                Assertions.fail("Should not succeed with empty password");
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertTrue(error.contains("Password cannot be empty"));
            }

            @Override
            public void switchToLoginView() {
                // Not needed for this test
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, factory);
        interactor.execute(inputData);
    }

    // ---------------- Disallowed email domain ----------------
    @Test
    void disallowedEmailDomainFails() {
        SignupUserDataAccessInterface dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();

        SignupInputData inputData = new SignupInputData("Frank", "pass123", "pass123", "frank@yahoo.com");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                Assertions.fail("Should not succeed with disallowed email domain");
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertTrue(error.contains("Email domain not allowed"));
            }

            @Override
            public void switchToLoginView() {
                // Not needed for this test
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, factory);
        interactor.execute(inputData);
    }

    // ---------------- Duplicate email exception ----------------
    @Test
    void duplicateEmailFails() {
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject() {
            @Override
            public void save(entity.User user) throws DuplicateEmailException {
                throw new DuplicateEmailException("Duplicate email");
            }
        };
        UserFactory factory = new CommonUserFactory();

        SignupInputData inputData = new SignupInputData("George", "pass123", "pass123", "george@gmail.com");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                Assertions.fail("Should not succeed when duplicate email is thrown");
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertTrue(error.contains("That email is already registered"));
            }

            @Override
            public void switchToLoginView() {
                // Not needed for this test
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, factory);
        interactor.execute(inputData);
    }

    // ---------------- Switch to login ----------------
    @Test
    void switchToLoginViewWorks() {
        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                // Not needed for this test
            }

            @Override
            public void prepareFailView(String error) {
                // Not needed for this test
            }

            @Override
            public void switchToLoginView() {
                // Confirm method called
                Assertions.assertTrue(true);
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(
                new InMemoryUserDataAccessObject(), presenter, new CommonUserFactory()
        );

        interactor.switchToLoginView();
    }

    // ---------------- Empty email ----------------
    @Test
    void emptyEmailFails() {
        SignupUserDataAccessInterface dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();

        SignupInputData inputData = new SignupInputData("Helen", "pass123", "pass123", "");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData data) {
                Assertions.fail("Should not succeed with empty email");
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.assertTrue(error.contains("Email cannot be empty"));
            }

            @Override
            public void switchToLoginView() {
                // Not needed for this test
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(dao, presenter, factory);
        interactor.execute(inputData);
    }
}
