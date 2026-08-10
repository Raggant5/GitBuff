package data_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import entity.ActivityLevel;
import entity.CommonUser;
import entity.DietaryRestriction;
import entity.Equipment;
import entity.FitnessGoal;
import entity.Gender;
import entity.PrivacySetting;
import entity.UnitSystem;

/**
 * Shared setup and cleanup for DAO integration tests.
 */
final class DaoTestSupport {

    private static final String TEST_PASSWORD = "test-password";

    private DaoTestSupport() {
    }

    static String newUsername() {
        return "dao_test_" + UUID.randomUUID().toString().replace("-", "");
    }

    static CommonUser createSavedUser(final String username) {
        final CommonUser user = new CommonUser(username, TEST_PASSWORD);

        user.setHeight(1.75F);
        user.setWeight(75.0F);
        user.setActivityLevel(ActivityLevel.values()[0]);
        user.setGoal(FitnessGoal.values()[0]);
        user.setProfilePicturePath("test-profile.png");
        user.setDateOfBirth(LocalDate.of(2004, 1, 15));
        user.setGender(Gender.values()[0]);
        user.setBio("DAO integration-test user");
        user.setPreferredUnitSystem(UnitSystem.values()[0]);
        user.setEquipment(Set.of(Equipment.values()[0]));
        user.setDietaryRestrictions(Set.of(DietaryRestriction.values()[0]));
        user.setPreferredWorkoutDays(Set.of(DayOfWeek.MONDAY));
        user.setPreferredWorkoutDurationMinutes(45);
        user.setPrivacySettings(Set.of(PrivacySetting.values()[0]));

        new SQLiteUserDataAccessObject().save(user);
        return user;
    }

    static void cleanupUser(final String username) {
        try (Connection connection = Database.connect()) {
            connection.setAutoCommit(false);

            try {
                execute(
                        connection,
                        """
                        DELETE FROM exercises_performed
                        WHERE workout_id IN (
                            SELECT id
                            FROM logged_workouts
                            WHERE user_id = ?
                        )
                        """,
                        username
                );

                execute(
                        connection,
                        "DELETE FROM logged_workouts WHERE user_id = ?",
                        username
                );

                execute(
                        connection,
                        """
                        DELETE FROM food_entries
                        WHERE meal_id IN (
                            SELECT id
                            FROM meals
                            WHERE user_id = ?
                        )
                        """,
                        username
                );

                execute(
                        connection,
                        "DELETE FROM meals WHERE user_id = ?",
                        username
                );

                execute(
                        connection,
                        "DELETE FROM user_equipment WHERE username = ?",
                        username
                );

                execute(
                        connection,
                        """
                        DELETE FROM user_dietary_restrictions
                        WHERE username = ?
                        """,
                        username
                );

                execute(
                        connection,
                        "DELETE FROM user_workout_days WHERE username = ?",
                        username
                );

                execute(
                        connection,
                        "DELETE FROM user_privacy_settings WHERE username = ?",
                        username
                );

                execute(
                        connection,
                        "DELETE FROM users WHERE username = ?",
                        username
                );

                connection.commit();
            }
            catch (final SQLException exception) {
                connection.rollback();
                throw exception;
            }
            finally {
                connection.setAutoCommit(true);
            }
        }
        catch (final SQLException exception) {
            throw new RuntimeException(
                    "Failed to clean DAO test data.",
                    exception
            );
        }
    }

    private static void execute(
            final Connection connection,
            final String sql,
            final String username
    ) throws SQLException {
        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        }
    }
}