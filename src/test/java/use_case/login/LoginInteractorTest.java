package use_case.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.CommonUser;
import entity.User;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;
import use_case.EventPublisher;
import use_case.session.UserLoggedInEvent;

class LoginInteractorTest {

    @Test
    void executeWithUnknownUsernameFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        final CapturingPresenter presenter = new CapturingPresenter();
        final FakeEventPublisher eventPublisher = new FakeEventPublisher();

        new LoginInteractor(dataAccessObject, presenter, eventPublisher)
                .execute(new LoginInputData("amir", "password"));

        assertEquals("amir: Account does not exist.", presenter.failMessage);
        assertTrue(eventPublisher.publishedEvents.isEmpty());
    }

    @Test
    void executeWithWrongPasswordFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        dataAccessObject.save(new CommonUser("amir", "correct-password"));
        final CapturingPresenter presenter = new CapturingPresenter();
        final FakeEventPublisher eventPublisher = new FakeEventPublisher();

        new LoginInteractor(dataAccessObject, presenter, eventPublisher)
                .execute(new LoginInputData("amir", "wrong-password"));

        assertEquals("Incorrect password for \"amir\".", presenter.failMessage);
        assertTrue(eventPublisher.publishedEvents.isEmpty());
    }

    @Test
    void executeWithCorrectCredentialsSucceedsAndPublishesEvent() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject();
        dataAccessObject.save(new CommonUser("amir", "correct-password"));
        final CapturingPresenter presenter = new CapturingPresenter();
        final FakeEventPublisher eventPublisher = new FakeEventPublisher();

        new LoginInteractor(dataAccessObject, presenter, eventPublisher)
                .execute(new LoginInputData("amir", "correct-password"));

        assertEquals("amir", presenter.successData.getUsername());
        assertEquals("amir", dataAccessObject.getCurrentUsername());
        assertEquals(1, eventPublisher.publishedEvents.size());
        assertEquals("amir", eventPublisher.publishedEvents.get(0).getData().getUsername());
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
        final FakeEventPublisher eventPublisher = new FakeEventPublisher();

        new LoginInteractor(dataAccessObject, presenter, eventPublisher)
                .execute(new LoginInputData("amir", "password"));

        assertEquals("Unable to log in right now. Please try again.", presenter.failMessage);
    }

    @Test
    void switchToSignupViewDelegatesToPresenter() {
        final CapturingPresenter presenter = new CapturingPresenter();
        new LoginInteractor(new FakeDataAccessObject(), presenter, new FakeEventPublisher())
                .switchToSignupView();

        assertTrue(presenter.switchedToSignup);
    }

    private static class FakeDataAccessObject implements LoginUserDataAccessInterface {
        private final Map<String, User> users = new HashMap<>();
        private String currentUsername;

        @Override
        public boolean existsByName(final String username) {
            return users.containsKey(username);
        }

        @Override
        public void save(final User user) {
            users.put(user.getName(), user);
        }

        @Override
        public User get(final String username) {
            return users.get(username);
        }

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }

        @Override
        public void setCurrentUsername(final String username) {
            this.currentUsername = username;
        }
    }

    private static final class CapturingPresenter implements LoginOutputBoundary {
        private String failMessage;
        private LoginOutputData successData;
        private boolean switchedToSignup;

        @Override
        public void prepareSuccessView(final LoginOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(final String errorMessage) {
            this.failMessage = errorMessage;
        }

        @Override
        public void switchToSignupView() {
            this.switchedToSignup = true;
        }
    }

    private static final class FakeEventPublisher implements EventPublisher<UserLoggedInEvent> {
        private final List<UserLoggedInEvent> publishedEvents = new ArrayList<>();

        @Override
        public void publish(final UserLoggedInEvent event) {
            publishedEvents.add(event);
        }
    }
}
