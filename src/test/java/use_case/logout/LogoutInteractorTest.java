package use_case.logout;

import data_access.DuplicateEmailException;
import data_access.InMemoryUserDataAccessObject;
import entity.CommonUserFactory;
import entity.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LogoutInteractorTest {

    @Test
    void successfulLogout() throws DuplicateEmailException {
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        UserFactory factory = new CommonUserFactory();
        dao.save(factory.create("Carol", "pass", "caroline.stevens@gmail.com"));
        dao.setCurrentUsername("Carol");

        LogoutInputData input = new LogoutInputData("Carol");

        LogoutOutputBoundary presenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData data) {
                Assertions.assertEquals("Carol", data.getUsername());
            }

            @Override
            public void prepareFailView(String error) {
                Assertions.fail("Unexpected failure: " + error);
            }
        };

        LogoutInputBoundary interactor = new LogoutInteractor(dao, presenter);
        interactor.execute(input);

        Assertions.assertNull(dao.getCurrentUsername());
    }
}
