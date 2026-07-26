package data_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.ActivityLevel;
import entity.CommonUser;
import entity.FitnessGoal;
import entity.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.profile.ProfileUserDataAccessInterface;
import use_case.recommendation.RecommendationUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class SQLiteUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        ProfileUserDataAccessInterface,
        RecommendationUserDataAccessInterface {

    private String currentUsername;

    @Override
    public boolean existsByName(String identifier) {
        final String sql = """
                SELECT username
                FROM users
                WHERE username = ?
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, identifier);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Could not check whether user exists.", exception);
        }
    }

    @Override
    public void save(User user) {
        final String sql = """
                INSERT INTO users (
                    username,
                    password,
                    height,
                    weight,
                    activity_level,
                    fitness_goal,
                    profile_picture_path
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT(username) DO UPDATE SET
                    password = excluded.password,
                    height = excluded.height,
                    weight = excluded.weight,
                    activity_level = excluded.activity_level,
                    fitness_goal = excluded.fitness_goal,
                    profile_picture_path = excluded.profile_picture_path
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setFloat(3, user.getHeight());
            statement.setFloat(4, user.getWeight());
            statement.setString(5, user.getActivityLevel().name());
            statement.setString(6, user.getGoal().name());
            statement.setString(7, user.getProfilePicturePath());

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException("Could not save user.", exception);
        }
    }

    @Override
    public User get(String username) {
        final String sql = """
                SELECT username,
                       password,
                       height,
                       weight,
                       activity_level,
                       fitness_goal,
                       profile_picture_path
                FROM users
                WHERE username = ?
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                CommonUser user = new CommonUser(
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );

                user.setHeight(resultSet.getFloat("height"));
                user.setWeight(resultSet.getFloat("weight"));
                user.setActivityLevel(
                        ActivityLevel.valueOf(resultSet.getString("activity_level"))
                );
                user.setGoal(
                        FitnessGoal.valueOf(resultSet.getString("fitness_goal"))
                );
                user.setProfilePicturePath(
                        resultSet.getString("profile_picture_path")
                );

                return user;
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Could not load user.", exception);
        }
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