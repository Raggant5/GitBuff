package data_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import entity.ActivityLevel;
import entity.CommonUser;
import entity.DietaryRestriction;
import entity.Equipment;
import entity.FitnessGoal;
import entity.Gender;
import entity.PrivacySetting;
import entity.UnitSystem;
import entity.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.profile.ProfileUserDataAccessInterface;
import use_case.recommendation.RecommendationUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * SQLite implementation for storing and loading user information.
 */
public class SQLiteUserDataAccessObject
        implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        ProfileUserDataAccessInterface,
        RecommendationUserDataAccessInterface {

    private static final String SELECT_EXISTS_SQL = """
            SELECT username
            FROM users
            WHERE username = ?
            """;

    private static final String SAVE_USER_SQL = """
            INSERT INTO users (
                username, password, height, weight, activity_level, fitness_goal, profile_picture_path
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(username) DO UPDATE SET
                password = excluded.password, height = excluded.height, weight = excluded.weight,
                activity_level = excluded.activity_level, fitness_goal = excluded.fitness_goal,
                profile_picture_path = excluded.profile_picture_path
            """;

    private static final String GET_USER_SQL = """
            SELECT username, password, height, weight, activity_level, fitness_goal, profile_picture_path
            FROM users
            WHERE username = ?
            """;

    private String currentUsername;

    @Override
    public boolean existsByName(final String identifier) {
        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(SELECT_EXISTS_SQL)) {
            statement.setString(1, identifier);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
        catch (final SQLException exception) {
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
                    profile_picture_path,
                    date_of_birth,
                    gender,
                    bio,
                    preferred_unit_system,
                    preferred_workout_duration_minutes
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT(username) DO UPDATE SET
                    password = excluded.password,
                    height = excluded.height,
                    weight = excluded.weight,
                    activity_level = excluded.activity_level,
                    fitness_goal = excluded.fitness_goal,
                    profile_picture_path =
                        excluded.profile_picture_path,
                    date_of_birth = excluded.date_of_birth,
                    gender = excluded.gender,
                    bio = excluded.bio,
                    preferred_unit_system =
                        excluded.preferred_unit_system,
                    preferred_workout_duration_minutes =
                        excluded.preferred_workout_duration_minutes
                """;

        try (Connection connection = Database.connect()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement statement =
                             connection.prepareStatement(sql)) {

                    statement.setString(1, user.getName());
                    statement.setString(2, user.getPassword());
                    statement.setFloat(3, user.getHeight());
                    statement.setFloat(4, user.getWeight());
                    statement.setString(
                            5,
                            user.getActivityLevel().name()
                    );
                    statement.setString(
                            6,
                            user.getGoal().name()
                    );
                    statement.setString(
                            7,
                            user.getProfilePicturePath()
                    );

                    if (user.getDateOfBirth() == null) {
                        statement.setNull(8, Types.VARCHAR);
                    }
                    else {
                        statement.setString(
                                8,
                                user.getDateOfBirth().toString()
                        );
                    }

                    statement.setString(
                            9,
                            user.getGender().name()
                    );
                    statement.setString(10, user.getBio());
                    statement.setString(
                            11,
                            user.getPreferredUnitSystem().name()
                    );
                    statement.setInt(
                            12,
                            user.getPreferredWorkoutDurationMinutes()
                    );

                    statement.executeUpdate();
                }

                saveEquipment(connection, user);
                saveDietaryRestrictions(connection, user);
                saveWorkoutDays(connection, user);
                savePrivacySettings(connection, user);

                connection.commit();
            }
            catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
            finally {
                connection.setAutoCommit(true);
            }
        }
        catch (SQLException exception) {
            throw new RuntimeException(
                    "Could not save user.",
                    exception
            );
        }
    }

    @Override
    public User get(String username) {
        final String sql = """
                SELECT
                    username,
                    password,
                    height,
                    weight,
                    activity_level,
                    fitness_goal,
                    profile_picture_path,
                    date_of_birth,
                    gender,
                    bio,
                    preferred_unit_system,
                    preferred_workout_duration_minutes
                FROM users
                WHERE username = ?
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                final CommonUser user = new CommonUser(
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );

                user.setHeight(
                        resultSet.getFloat("height")
                );
                user.setWeight(
                        resultSet.getFloat("weight")
                );
                user.setActivityLevel(
                        ActivityLevel.valueOf(
                                resultSet.getString(
                                        "activity_level"
                                )
                        )
                );
                user.setGoal(
                        FitnessGoal.valueOf(
                                resultSet.getString(
                                        "fitness_goal"
                                )
                        )
                );
                user.setProfilePicturePath(
                        resultSet.getString(
                                "profile_picture_path"
                        )
                );

                final String dateOfBirth =
                        resultSet.getString("date_of_birth");

                if (dateOfBirth != null
                        && !dateOfBirth.isBlank()) {
                    user.setDateOfBirth(
                            LocalDate.parse(dateOfBirth)
                    );
                }

                user.setGender(
                        Gender.valueOf(
                                resultSet.getString("gender")
                        )
                );
                user.setBio(
                        resultSet.getString("bio")
                );
                user.setPreferredUnitSystem(
                        UnitSystem.valueOf(
                                resultSet.getString(
                                        "preferred_unit_system"
                                )
                        )
                );
                user.setPreferredWorkoutDurationMinutes(
                        resultSet.getInt(
                                "preferred_workout_duration_minutes"
                        )
                );

                user.setEquipment(
                        loadEquipment(connection, username)
                );
                user.setDietaryRestrictions(
                        loadDietaryRestrictions(
                                connection,
                                username
                        )
                );
                user.setPreferredWorkoutDays(
                        loadWorkoutDays(connection, username)
                );
                user.setPrivacySettings(
                        loadPrivacySettings(
                                connection,
                                username
                        )
                );

                return user;
            }
        }
        catch (SQLException exception) {
            throw new RuntimeException(
                    "Could not load user.",
                    exception
            );
        }
    }

    private void saveEquipment(
            Connection connection,
            User user
    ) throws SQLException {
        final String deleteSql = """
                DELETE FROM user_equipment
                WHERE username = ?
                """;

        final String insertSql = """
                INSERT INTO user_equipment (
                    username,
                    equipment
                )
                VALUES (?, ?)
                """;

        try (PreparedStatement deleteStatement =
                     connection.prepareStatement(deleteSql)) {
            deleteStatement.setString(1, user.getName());
            deleteStatement.executeUpdate();
        }

        try (PreparedStatement insertStatement =
                     connection.prepareStatement(insertSql)) {

            for (Equipment equipment : user.getEquipment()) {
                insertStatement.setString(1, user.getName());
                insertStatement.setString(
                        2,
                        equipment.name()
                );
                insertStatement.addBatch();
            }

            insertStatement.executeBatch();
        }
    }

    private void saveDietaryRestrictions(
            Connection connection,
            User user
    ) throws SQLException {
        final String deleteSql = """
                DELETE FROM user_dietary_restrictions
                WHERE username = ?
                """;

        final String insertSql = """
                INSERT INTO user_dietary_restrictions (
                    username,
                    dietary_restriction
                )
                VALUES (?, ?)
                """;

        try (PreparedStatement deleteStatement =
                     connection.prepareStatement(deleteSql)) {
            deleteStatement.setString(1, user.getName());
            deleteStatement.executeUpdate();
        }

        try (PreparedStatement insertStatement =
                     connection.prepareStatement(insertSql)) {

            for (DietaryRestriction restriction
                    : user.getDietaryRestrictions()) {

                insertStatement.setString(1, user.getName());
                insertStatement.setString(
                        2,
                        restriction.name()
                );
                insertStatement.addBatch();
            }

            insertStatement.executeBatch();
        }
    }

    private void saveWorkoutDays(
            Connection connection,
            User user
    ) throws SQLException {
        final String deleteSql = """
                DELETE FROM user_workout_days
                WHERE username = ?
                """;

        final String insertSql = """
                INSERT INTO user_workout_days (
                    username,
                    workout_day
                )
                VALUES (?, ?)
                """;

        try (PreparedStatement deleteStatement =
                     connection.prepareStatement(deleteSql)) {
            deleteStatement.setString(1, user.getName());
            deleteStatement.executeUpdate();
        }

        try (PreparedStatement insertStatement =
                     connection.prepareStatement(insertSql)) {

            for (DayOfWeek day
                    : user.getPreferredWorkoutDays()) {

                insertStatement.setString(1, user.getName());
                insertStatement.setString(2, day.name());
                insertStatement.addBatch();
            }

            insertStatement.executeBatch();
        }
    }

    private void savePrivacySettings(
            Connection connection,
            User user
    ) throws SQLException {
        final String deleteSql = """
                DELETE FROM user_privacy_settings
                WHERE username = ?
                """;

        final String insertSql = """
                INSERT INTO user_privacy_settings (
                    username,
                    privacy_setting
                )
                VALUES (?, ?)
                """;

        try (PreparedStatement deleteStatement =
                     connection.prepareStatement(deleteSql)) {
            deleteStatement.setString(1, user.getName());
            deleteStatement.executeUpdate();
        }

        try (PreparedStatement insertStatement =
                     connection.prepareStatement(insertSql)) {

            for (PrivacySetting setting
                    : user.getPrivacySettings()) {

                insertStatement.setString(1, user.getName());
                insertStatement.setString(
                        2,
                        setting.name()
                );
                insertStatement.addBatch();
            }

            insertStatement.executeBatch();
        }
    }

    private Set<Equipment> loadEquipment(
            Connection connection,
            String username
    ) throws SQLException {
        final Set<Equipment> equipmentSet = new HashSet<>();

        final String sql = """
                SELECT equipment
                FROM user_equipment
                WHERE username = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    equipmentSet.add(
                            Equipment.valueOf(
                                    resultSet.getString(
                                            "equipment"
                                    )
                            )
                    );
                }
            }
        }

        return equipmentSet;
    }

    private Set<DietaryRestriction>
    loadDietaryRestrictions(
            Connection connection,
            String username
    ) throws SQLException {

        final Set<DietaryRestriction> restrictions =
                new HashSet<>();

        final String sql = """
                SELECT dietary_restriction
                FROM user_dietary_restrictions
                WHERE username = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    restrictions.add(
                            DietaryRestriction.valueOf(
                                    resultSet.getString(
                                            "dietary_restriction"
                                    )
                            )
                    );
                }
            }
        }

        return restrictions;
    }

    private Set<DayOfWeek> loadWorkoutDays(
            Connection connection,
            String username
    ) throws SQLException {
        final Set<DayOfWeek> workoutDays =
                new HashSet<>();

        final String sql = """
                SELECT workout_day
                FROM user_workout_days
                WHERE username = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    workoutDays.add(
                            DayOfWeek.valueOf(
                                    resultSet.getString(
                                            "workout_day"
                                    )
                            )
                    );
                }
            }
        }

        return workoutDays;
    }

    private Set<PrivacySetting> loadPrivacySettings(
            Connection connection,
            String username
    ) throws SQLException {
        final Set<PrivacySetting> settings =
                new HashSet<>();

        final String sql = """
                SELECT privacy_setting
                FROM user_privacy_settings
                WHERE username = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    settings.add(
                            PrivacySetting.valueOf(
                                    resultSet.getString(
                                            "privacy_setting"
                                    )
                            )
                    );
                }
            }
        }

        return settings;
    }

    @Override
    public void setCurrentUsername(final String name) {
        this.currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }
}