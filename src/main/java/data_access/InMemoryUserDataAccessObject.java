package data_access;

import java.util.HashMap;
import java.util.Map;

import entity.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.profile.ProfileUserDataAccessInterface;
import use_case.recommendation.RecommendationUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * SQLite implementation of the DAO for storing user data.
 * This implementation persists user data in the GitBuff database,
 * allowing users to log in across application sessions.
 */

public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface, LogoutUserDataAccessInterface, ProfileUserDataAccessInterface,
        RecommendationUserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();

    private String currentUsername;

    @Override
    public boolean existsByName(String identifier) {
        return users.containsKey(identifier);
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        this.currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return this.currentUsername;
    }
}
