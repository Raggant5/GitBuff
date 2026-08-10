package use_case.signup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import entity.CommonUserFactory;
import entity.User;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;

class SignupInteractorTest {

    @Test
    void executeWithExistingUsernameFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        dataAccessObject.existingUsernames.add("amir");
        final CapturingPresenter presenter = new CapturingPresenter();

        new SignupInteractor(dataAccessObject, presenter, new CommonUserFactory())
                .execute(new SignupInputData("amir", "password", "password"));

        assertEquals("User already exists.", presenter.failMessage);
    }

    @Test
    void executeWithMismatchedPasswordsFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final CapturingPresenter presenter = new CapturingPresenter();

        new SignupInteractor(dataAccessObject, presenter, new CommonUserFactory())
                .execute(new SignupInputData("amir", "password", "different"));

        assertEquals("Passwords don't match.", presenter.failMessage);
    }

    @Test
    void executeWithValidDataSucceeds() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final CapturingPresenter presenter = new CapturingPresenter();

        new SignupInteractor(dataAccessObject, presenter, new CommonUserFactory())
                .execute(new SignupInputData("amir", "password", "password"));

        assertEquals("amir", presenter.successData.getUsername());
        assertTrue(dataAccessObject.savedUsers.containsKey("amir"));
        assertEquals("amir", dataAccessObject.currentUsername);
    }

    @Test
    void executeWhenDataAccessThrowsFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject() {
            @Override
            public boolean existsByName(final String username) {
                throw new DataAccessException("Database unavailable");
            }
        };
        final CapturingPresenter presenter = new CapturingPresenter();

        new SignupInteractor(dataAccessObject, presenter, new CommonUserFactory())
                .execute(new SignupInputData("amir", "password", "password"));

        assertEquals("Unable to sign up right now. Please try again.", presenter.failMessage);
    }

    @Test
    void switchToLoginViewDelegatesToPresenter() {
        final CapturingPresenter presenter = new CapturingPresenter();
        new SignupInteractor(new FakeDataAccessObject(), presenter, new CommonUserFactory())
                .switchToLoginView("amir", "password");

        assertEquals("amir", presenter.switchedUsername);
        assertEquals("password", presenter.switchedPassword);
    }

    private static class FakeDataAccessObject implements SignupUserDataAccessInterface {
        private final java.util.Set<String> existingUsernames = new java.util.HashSet<>();
        private final Map<String, User> savedUsers = new HashMap<>();
        private String currentUsername;

        @Override
        public boolean existsByName(final String username) {
            return existingUsernames.contains(username);
        }

        @Override
        public void save(final User user) {
            savedUsers.put(user.getName(), user);
        }

        @Override
        public void setCurrentUsername(final String username) {
            this.currentUsername = username;
        }
    }

    private static final class CapturingPresenter implements SignupOutputBoundary {
        private String failMessage;
        private SignupOutputData successData;
        private String switchedUsername;
        private String switchedPassword;

        @Override
        public void prepareSuccessView(final SignupOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(final String errorMessage) {
            this.failMessage = errorMessage;
        }

        @Override
        public void switchToLoginView(final String username, final String password) {
            this.switchedUsername = username;
            this.switchedPassword = password;
        }
    }
}
