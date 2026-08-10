package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.ExercisePerformed;
import entity.LoggedWorkout;
import entity.StrengthDetails;
import use_case.DataAccessException;

class SqliteWorkoutDataAccessObjectTest {

    private SqliteWorkoutDataAccessObject dao;
    private String username;

    @BeforeEach
    void setUp() throws SQLException {
        dao = new SqliteWorkoutDataAccessObject();

        username = "test_" + UUID.randomUUID();

        final String sql =
                "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, "password");

            statement.executeUpdate();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = Database.connect()) {

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 "DELETE FROM exercises_performed "
                                         + "WHERE workout_id IN "
                                         + "(SELECT id FROM logged_workouts "
                                         + "WHERE user_id = ?)")) {

                statement.setString(1, username);
                statement.executeUpdate();
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 "DELETE FROM logged_workouts "
                                         + "WHERE user_id = ?")) {

                statement.setString(1, username);
                statement.executeUpdate();
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 "DELETE FROM users "
                                         + "WHERE username = ?")) {

                statement.setString(1, username);
                statement.executeUpdate();
            }
        }
    }

    @Test
    void saveWorkoutSetsId() {
        final LoggedWorkout workout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        final int id = dao.saveWorkout(workout);

        assertTrue(id > 0);
        assertEquals(id, workout.getId());
    }

    @Test
    void saveCardioExerciseAndLoadIt() {
        final LoggedWorkout workout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        final int workoutId =
                dao.saveWorkout(workout);

        final ExercisePerformed exercise =
                new ExercisePerformed(
                        "Running",
                        null,
                        20.0,
                        3.0,
                        true
                );

        exercise.setWorkoutId(workoutId);

        final int exerciseId =
                dao.saveExercisePerformed(exercise);

        assertTrue(exerciseId > 0);
        assertEquals(exerciseId, exercise.getId());

        final List<ExercisePerformed> exercises =
                dao.getExercisesForWorkout(workoutId);

        assertEquals(1, exercises.size());

        final ExercisePerformed loaded =
                exercises.get(0);

        assertEquals("Running", loaded.getExerciseName());
        assertEquals(20.0, loaded.getDurationMins(), 0.001);
        assertEquals(3.0, loaded.getDistanceKm(), 0.001);
        assertTrue(loaded.getIsCardio());
    }

    @Test
    void saveStrengthExerciseAndLoadIt() {
        final LoggedWorkout workout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        final int workoutId =
                dao.saveWorkout(workout);

        final StrengthDetails details =
                new StrengthDetails(
                        3,
                        8,
                        80.0
                );

        final ExercisePerformed exercise =
                new ExercisePerformed(
                        "Bench Press",
                        details,
                        15.0,
                        null,
                        false
                );

        exercise.setWorkoutId(workoutId);

        dao.saveExercisePerformed(exercise);

        final List<ExercisePerformed> exercises =
                dao.getExercisesForWorkout(workoutId);

        assertEquals(1, exercises.size());

        final ExercisePerformed loaded =
                exercises.get(0);

        assertEquals("Bench Press", loaded.getExerciseName());
        assertEquals(3, loaded.getSets());
        assertEquals(8, loaded.getReps());
        assertEquals(80.0, loaded.getWeight(), 0.001);
        assertEquals(15.0, loaded.getDurationMins(), 0.001);
        assertFalse(loaded.getIsCardio());
    }

    @Test
    void getWorkoutsForUserLoadsWorkoutAndExercises() {
        final LoggedWorkout workout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        final int workoutId =
                dao.saveWorkout(workout);

        final ExercisePerformed exercise =
                new ExercisePerformed(
                        "Running",
                        null,
                        25.0,
                        4.0,
                        true
                );

        exercise.setWorkoutId(workoutId);
        dao.saveExercisePerformed(exercise);

        final List<LoggedWorkout> workouts =
                dao.getWorkoutsForUser(username);

        assertEquals(1, workouts.size());

        final LoggedWorkout loadedWorkout =
                workouts.get(0);

        assertEquals(workoutId, loadedWorkout.getId());
        assertEquals(username, loadedWorkout.getUserId());
        assertEquals(LocalDate.now(), loadedWorkout.getDate());

        assertEquals(
                1,
                loadedWorkout.getExercises().size()
        );

        assertEquals(
                "Running",
                loadedWorkout
                        .getExercises()
                        .get(0)
                        .getExerciseName()
        );
    }

    @Test
    void workoutHistoryOnlyReturnsLastSevenDays() {
        final LoggedWorkout recentWorkout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        final LoggedWorkout boundaryWorkout =
                new LoggedWorkout(
                        username,
                        LocalDate.now().minusDays(6)
                );

        final LoggedWorkout oldWorkout =
                new LoggedWorkout(
                        username,
                        LocalDate.now().minusDays(7)
                );

        dao.saveWorkout(recentWorkout);
        dao.saveWorkout(boundaryWorkout);
        dao.saveWorkout(oldWorkout);

        final List<LoggedWorkout> workouts =
                dao.getWorkoutsForUser(username);

        assertEquals(2, workouts.size());

        assertTrue(
                workouts.stream()
                        .anyMatch(workout ->
                                workout.getId()
                                        .equals(recentWorkout.getId()))
        );

        assertTrue(
                workouts.stream()
                        .anyMatch(workout ->
                                workout.getId()
                                        .equals(boundaryWorkout.getId()))
        );
    }

    @Test
    void getWorkoutByIdReturnsWorkout() {
        final LoggedWorkout workout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        final int workoutId =
                dao.saveWorkout(workout);

        final ExercisePerformed exercise =
                new ExercisePerformed(
                        "Cycling",
                        null,
                        30.0,
                        8.0,
                        true
                );

        exercise.setWorkoutId(workoutId);
        dao.saveExercisePerformed(exercise);

        final LoggedWorkout loaded =
                dao.getWorkoutById(workoutId);

        assertNotNull(loaded);

        assertEquals(
                workoutId,
                loaded.getId()
        );

        assertEquals(
                username,
                loaded.getUserId()
        );

        assertEquals(
                LocalDate.now(),
                loaded.getDate()
        );

        assertEquals(
                1,
                loaded.getExercises().size()
        );

        assertEquals(
                "Cycling",
                loaded.getExercises()
                        .get(0)
                        .getExerciseName()
        );
    }

    @Test
    void getWorkoutByIdThrowsWhenWorkoutDoesNotExist() {
        assertThrows(
                DataAccessException.class,
                () -> dao.getWorkoutById(-999999)
        );
    }

    @Test
    void editWorkoutUpdatesDateAndAddsExercise() {
        final LoggedWorkout workout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        final int workoutId =
                dao.saveWorkout(workout);

        final LocalDate newDate =
                LocalDate.now().minusDays(1);

        final LoggedWorkout editedWorkout =
                new LoggedWorkout(
                        username,
                        newDate
                );

        editedWorkout.setId(workoutId);

        final ExercisePerformed newExercise =
                new ExercisePerformed(
                        "Cycling",
                        null,
                        45.0,
                        10.0,
                        true
                );

        editedWorkout.setExercises(
                List.of(newExercise)
        );

        final LoggedWorkout result =
                dao.editWorkout(
                        editedWorkout,
                        List.of()
                );

        assertEquals(
                workoutId,
                result.getId()
        );

        assertNotNull(
                newExercise.getWorkoutId()
        );

        assertNotNull(
                newExercise.getId()
        );

        final LoggedWorkout loaded =
                dao.getWorkoutById(workoutId);

        assertEquals(
                newDate,
                loaded.getDate()
        );

        assertEquals(
                1,
                loaded.getExercises().size()
        );

        assertEquals(
                "Cycling",
                loaded.getExercises()
                        .get(0)
                        .getExerciseName()
        );
    }

    @Test
    void editWorkoutUpdatesExistingExercise() {
        final LoggedWorkout workout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        final int workoutId =
                dao.saveWorkout(workout);

        final ExercisePerformed original =
                new ExercisePerformed(
                        "Running",
                        null,
                        20.0,
                        3.0,
                        true
                );

        original.setWorkoutId(workoutId);

        final int exerciseId =
                dao.saveExercisePerformed(original);

        final ExercisePerformed editedExercise =
                new ExercisePerformed(
                        "Fast Running",
                        null,
                        40.0,
                        7.0,
                        true
                );

        editedExercise.setId(exerciseId);
        editedExercise.setWorkoutId(workoutId);

        final LoggedWorkout editedWorkout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        editedWorkout.setId(workoutId);

        editedWorkout.setExercises(
                List.of(editedExercise)
        );

        dao.editWorkout(
                editedWorkout,
                List.of()
        );

        final LoggedWorkout loaded =
                dao.getWorkoutById(workoutId);

        assertEquals(
                1,
                loaded.getExercises().size()
        );

        final ExercisePerformed loadedExercise =
                loaded.getExercises().get(0);

        assertEquals(
                exerciseId,
                loadedExercise.getId()
        );

        assertEquals(
                "Fast Running",
                loadedExercise.getExerciseName()
        );

        assertEquals(
                40.0,
                loadedExercise.getDurationMins(),
                0.001
        );

        assertEquals(
                7.0,
                loadedExercise.getDistanceKm(),
                0.001
        );
    }

    @Test
    void editWorkoutDeletesRemovedExercise() {
        final LoggedWorkout workout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        final int workoutId =
                dao.saveWorkout(workout);

        final ExercisePerformed firstExercise =
                new ExercisePerformed(
                        "Running",
                        null,
                        20.0,
                        3.0,
                        true
                );

        firstExercise.setWorkoutId(workoutId);

        final int firstExerciseId =
                dao.saveExercisePerformed(firstExercise);

        final ExercisePerformed secondExercise =
                new ExercisePerformed(
                        "Cycling",
                        null,
                        30.0,
                        5.0,
                        true
                );

        secondExercise.setWorkoutId(workoutId);
        dao.saveExercisePerformed(secondExercise);

        final LoggedWorkout editedWorkout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        editedWorkout.setId(workoutId);
        editedWorkout.setExercises(
                List.of(secondExercise)
        );

        final List<Integer> idsToDelete =
                new ArrayList<>();

        idsToDelete.add(firstExerciseId);

        /*
         * These cover the branches where editWorkout
         * ignores invalid exercise IDs.
         */
        idsToDelete.add(null);
        idsToDelete.add(0);
        idsToDelete.add(-1);

        dao.editWorkout(
                editedWorkout,
                idsToDelete
        );

        final List<ExercisePerformed> exercises =
                dao.getExercisesForWorkout(workoutId);

        assertEquals(1, exercises.size());

        assertEquals(
                "Cycling",
                exercises.get(0).getExerciseName()
        );
    }

    @Test
    void saveExerciseUpdatesTotalWorkoutMinutes()
            throws SQLException {

        final LoggedWorkout workout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        final int workoutId =
                dao.saveWorkout(workout);

        final ExercisePerformed first =
                new ExercisePerformed(
                        "Running",
                        null,
                        20.0,
                        3.0,
                        true
                );

        first.setWorkoutId(workoutId);
        dao.saveExercisePerformed(first);

        final ExercisePerformed second =
                new ExercisePerformed(
                        "Cycling",
                        null,
                        30.0,
                        5.0,
                        true
                );

        second.setWorkoutId(workoutId);
        dao.saveExercisePerformed(second);

        assertEquals(
                50.0,
                getTotalWorkoutMinutes(),
                0.001
        );
    }

    @Test
    void deleteWorkoutDeletesWorkoutAndExercises()
            throws SQLException {

        final LoggedWorkout workout =
                new LoggedWorkout(
                        username,
                        LocalDate.now()
                );

        final int workoutId =
                dao.saveWorkout(workout);

        final ExercisePerformed exercise =
                new ExercisePerformed(
                        "Running",
                        null,
                        25.0,
                        4.0,
                        true
                );

        exercise.setWorkoutId(workoutId);

        dao.saveExercisePerformed(exercise);

        assertEquals(
                25.0,
                getTotalWorkoutMinutes(),
                0.001
        );

        dao.deleteWorkout(workoutId);

        final List<LoggedWorkout> workouts =
                dao.getWorkoutsForUser(username);

        assertTrue(workouts.isEmpty());

        final List<ExercisePerformed> exercises =
                dao.getExercisesForWorkout(workoutId);

        assertTrue(exercises.isEmpty());

        assertEquals(
                0.0,
                getTotalWorkoutMinutes(),
                0.001
        );
    }

    private double getTotalWorkoutMinutes()
            throws SQLException {

        final String sql =
                "SELECT total_workout_minutes "
                        + "FROM users "
                        + "WHERE username = ?";

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                assertTrue(resultSet.next());

                return resultSet.getDouble(
                        "total_workout_minutes"
                );
            }
        }
    }
}