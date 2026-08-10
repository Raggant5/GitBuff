package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.ActivityLevel;
import entity.CommonUser;
import entity.DietaryRestriction;
import entity.Equipment;
import entity.FitnessGoal;
import entity.Gender;
import entity.PrivacySetting;
import entity.UnitSystem;
import entity.User;

class SqliteUserDataAccessObjectTest {

    private SqliteUserDataAccessObject dao;
    private String username;

    @BeforeEach
    void setUp() {
        dao = new SqliteUserDataAccessObject();
        username = "test_" + UUID.randomUUID();
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = Database.connect()) {

            deleteByUsername(
                    connection,
                    "DELETE FROM user_equipment WHERE username = ?"
            );

            deleteByUsername(
                    connection,
                    "DELETE FROM user_dietary_restrictions "
                            + "WHERE username = ?"
            );

            deleteByUsername(
                    connection,
                    "DELETE FROM user_workout_days WHERE username = ?"
            );

            deleteByUsername(
                    connection,
                    "DELETE FROM user_privacy_settings "
                            + "WHERE username = ?"
            );

            deleteByUsername(
                    connection,
                    "DELETE FROM users WHERE username = ?"
            );
        }
    }

    @Test
    void existsByNameReturnsFalseWhenUserDoesNotExist() {
        assertFalse(
                dao.existsByName(username)
        );
    }

    @Test
    void existsByNameReturnsTrueWhenUserExists() {
        final CommonUser user =
                createUserWithProfile();

        dao.save(user);

        assertTrue(
                dao.existsByName(username)
        );
    }

    @Test
    void saveAndGetUserPersistsBasicFields() {
        final CommonUser user =
                createUserWithProfile();

        dao.save(user);

        final User loaded =
                dao.get(username);

        assertEquals(
                username,
                loaded.getName()
        );

        assertEquals(
                "password123",
                loaded.getPassword()
        );

        assertEquals(
                1.75f,
                loaded.getHeight(),
                0.001f
        );

        assertEquals(
                75.0f,
                loaded.getWeight(),
                0.001f
        );

        assertEquals(
                ActivityLevel.values()[0],
                loaded.getActivityLevel()
        );

        assertEquals(
                FitnessGoal.values()[0],
                loaded.getGoal()
        );

        assertEquals(
                "profile.png",
                loaded.getProfilePicturePath()
        );

        assertEquals(
                LocalDate.of(2004, 1, 15),
                loaded.getDateOfBirth()
        );

        assertEquals(
                Gender.values()[0],
                loaded.getGender()
        );

        assertEquals(
                "Test bio",
                loaded.getBio()
        );

        assertEquals(
                UnitSystem.values()[0],
                loaded.getPreferredUnitSystem()
        );

        assertEquals(
                45,
                loaded.getPreferredWorkoutDurationMinutes()
        );
    }

    @Test
    void saveAndGetUserPersistsCollections() {
        final CommonUser user =
                createUserWithProfile();

        final Set<Equipment> equipment =
                new HashSet<>();

        equipment.add(
                Equipment.values()[0]
        );

        if (Equipment.values().length > 1) {
            equipment.add(
                    Equipment.values()[1]
            );
        }

        final Set<DietaryRestriction> restrictions =
                new HashSet<>();

        restrictions.add(
                DietaryRestriction.values()[0]
        );

        final Set<DayOfWeek> workoutDays =
                new HashSet<>();

        workoutDays.add(
                DayOfWeek.MONDAY
        );

        workoutDays.add(
                DayOfWeek.FRIDAY
        );

        final Set<PrivacySetting> privacySettings =
                new HashSet<>();

        privacySettings.add(
                PrivacySetting.values()[0]
        );

        user.setEquipment(equipment);
        user.setDietaryRestrictions(restrictions);
        user.setPreferredWorkoutDays(workoutDays);
        user.setPrivacySettings(privacySettings);

        dao.save(user);

        final User loaded =
                dao.get(username);

        assertEquals(
                equipment,
                loaded.getEquipment()
        );

        assertEquals(
                restrictions,
                loaded.getDietaryRestrictions()
        );

        assertEquals(
                workoutDays,
                loaded.getPreferredWorkoutDays()
        );

        assertEquals(
                privacySettings,
                loaded.getPrivacySettings()
        );
    }

    @Test
    void saveUserWithNullDateOfBirthLoadsNullDate() {
        final CommonUser user =
                createUserWithProfile();

        user.setDateOfBirth(null);

        dao.save(user);

        final User loaded =
                dao.get(username);

        assertNull(
                loaded.getDateOfBirth()
        );
    }

    @Test
    void getReturnsNullWhenUserDoesNotExist() {
        final User loaded =
                dao.get(username);

        assertNull(loaded);
    }

    @Test
    void saveSameUsernameUpdatesExistingUser() {
        final CommonUser original =
                createUserWithProfile();

        dao.save(original);

        final CommonUser updated =
                createUserWithProfile();

        updated.setWeight(90.0f);
        updated.setHeight(1.80f);
        updated.setBio("Updated bio");
        updated.setPreferredWorkoutDurationMinutes(60);

        dao.save(updated);

        final User loaded =
                dao.get(username);

        assertEquals(
                90.0f,
                loaded.getWeight(),
                0.001f
        );

        assertEquals(
                1.80f,
                loaded.getHeight(),
                0.001f
        );

        assertEquals(
                "Updated bio",
                loaded.getBio()
        );

        assertEquals(
                60,
                loaded.getPreferredWorkoutDurationMinutes()
        );
    }

    @Test
    void savingAgainReplacesEquipment() {
        final CommonUser user =
                createUserWithProfile();

        final Set<Equipment> firstEquipment =
                new HashSet<>();

        firstEquipment.add(
                Equipment.values()[0]
        );

        user.setEquipment(firstEquipment);

        dao.save(user);

        final Set<Equipment> replacement =
                new HashSet<>();

        if (Equipment.values().length > 1) {
            replacement.add(
                    Equipment.values()[1]
            );
        }

        user.setEquipment(replacement);

        dao.save(user);

        final User loaded =
                dao.get(username);

        assertEquals(
                replacement,
                loaded.getEquipment()
        );
    }

    @Test
    void savingAgainReplacesDietaryRestrictions() {
        final CommonUser user =
                createUserWithProfile();

        final Set<DietaryRestriction> first =
                new HashSet<>();

        first.add(
                DietaryRestriction.values()[0]
        );

        user.setDietaryRestrictions(first);

        dao.save(user);

        final Set<DietaryRestriction> replacement =
                new HashSet<>();

        if (DietaryRestriction.values().length > 1) {
            replacement.add(
                    DietaryRestriction.values()[1]
            );
        }

        user.setDietaryRestrictions(replacement);

        dao.save(user);

        final User loaded =
                dao.get(username);

        assertEquals(
                replacement,
                loaded.getDietaryRestrictions()
        );
    }

    @Test
    void savingAgainReplacesWorkoutDays() {
        final CommonUser user =
                createUserWithProfile();

        user.setPreferredWorkoutDays(
                Set.of(
                        DayOfWeek.MONDAY,
                        DayOfWeek.WEDNESDAY
                )
        );

        dao.save(user);

        user.setPreferredWorkoutDays(
                Set.of(
                        DayOfWeek.SATURDAY
                )
        );

        dao.save(user);

        final User loaded =
                dao.get(username);

        assertEquals(
                Set.of(DayOfWeek.SATURDAY),
                loaded.getPreferredWorkoutDays()
        );
    }

    @Test
    void savingAgainReplacesPrivacySettings() {
        final CommonUser user =
                createUserWithProfile();

        user.setPrivacySettings(
                Set.of(
                        PrivacySetting.values()[0]
                )
        );

        dao.save(user);

        final Set<PrivacySetting> replacement =
                new HashSet<>();

        if (PrivacySetting.values().length > 1) {
            replacement.add(
                    PrivacySetting.values()[1]
            );
        }

        user.setPrivacySettings(replacement);

        dao.save(user);

        final User loaded =
                dao.get(username);

        assertEquals(
                replacement,
                loaded.getPrivacySettings()
        );
    }

    @Test
    void emptyCollectionsAreSavedAndLoaded() {
        final CommonUser user =
                createUserWithProfile();

        user.setEquipment(
                new HashSet<>()
        );

        user.setDietaryRestrictions(
                new HashSet<>()
        );

        user.setPreferredWorkoutDays(
                new HashSet<>()
        );

        user.setPrivacySettings(
                new HashSet<>()
        );

        dao.save(user);

        final User loaded =
                dao.get(username);

        assertTrue(
                loaded.getEquipment().isEmpty()
        );

        assertTrue(
                loaded.getDietaryRestrictions().isEmpty()
        );

        assertTrue(
                loaded.getPreferredWorkoutDays().isEmpty()
        );

        assertTrue(
                loaded.getPrivacySettings().isEmpty()
        );
    }

    @Test
    void getCurrentUserReturnsNullWhenNobodyLoggedIn() {
        assertNull(
                dao.getCurrentUser()
        );
    }

    @Test
    void setAndGetCurrentUsernameWorks() {
        dao.setCurrentUsername(username);

        assertEquals(
                username,
                dao.getCurrentUsername()
        );
    }

    @Test
    void getCurrentUserReturnsSavedUser() {
        final CommonUser user =
                createUserWithProfile();

        dao.save(user);

        dao.setCurrentUsername(username);

        final User currentUser =
                dao.getCurrentUser();

        assertEquals(
                username,
                currentUser.getName()
        );
    }

    private CommonUser createUserWithProfile() {
        final CommonUser user =
                new CommonUser(
                        username,
                        "password123"
                );

        user.setHeight(1.75f);
        user.setWeight(75.0f);

        user.setActivityLevel(
                ActivityLevel.values()[0]
        );

        user.setGoal(
                FitnessGoal.values()[0]
        );

        user.setProfilePicturePath(
                "profile.png"
        );

        user.setDateOfBirth(
                LocalDate.of(
                        2004,
                        1,
                        15
                )
        );

        user.setGender(
                Gender.values()[0]
        );

        user.setBio(
                "Test bio"
        );

        user.setPreferredUnitSystem(
                UnitSystem.values()[0]
        );

        user.setPreferredWorkoutDurationMinutes(
                45
        );

        user.setEquipment(
                new HashSet<>()
        );

        user.setDietaryRestrictions(
                new HashSet<>()
        );

        user.setPreferredWorkoutDays(
                new HashSet<>()
        );

        user.setPrivacySettings(
                new HashSet<>()
        );

        return user;
    }

    private void deleteByUsername(
            final Connection connection,
            final String sql
    ) throws SQLException {

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    username
            );

            statement.executeUpdate();
        }
    }
}