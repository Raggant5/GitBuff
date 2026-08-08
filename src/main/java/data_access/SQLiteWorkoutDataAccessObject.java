package data_access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseDataAccessInterface;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseDataAccessInterface;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.get_workouts.ViewWorkoutDataAccessInterface;

/**
 * SQLite data access object for logged workouts
 * and exercises performed.
 */
public final class SQLiteWorkoutDataAccessObject
        implements AddWorkoutDataAccessInterface,
        ViewWorkoutDataAccessInterface,
        EditWorkoutDataAccessInterface,
        EditExerciseDataAccessInterface,
        DeleteWorkoutDataAccessInterface,
        DeleteExerciseDataAccessInterface {

    /**
     * Saves a logged workout.
     *
     * @param workout workout to save
     * @return generated workout ID
     */
    @Override
    public int saveWorkout(final LoggedWorkout workout) {

        final String sql = """
                INSERT INTO logged_workouts (
                    user_id,
                    workout_date
                )
                VALUES (?, ?)
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            String userId = workout.getUserId();
            if (userId == null || userId.isBlank()) {
                final SQLiteUserDataAccessObject userDao = new SQLiteUserDataAccessObject();
                userId = userDao.getCurrentUsername();
            }

            statement.setString(1, userId);
            statement.setString(2, workout.getDate().toString());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    final int workoutId = generatedKeys.getInt(1);
                    workout.setId(workoutId);

                    return workoutId;
                }
            }
        }
        catch (final SQLException exception) {
            throw new RuntimeException(
                    "Failed to save workout.",
                    exception
            );
        }

        throw new IllegalStateException(
                "Workout ID was not generated."
        );
    }

    /**
     * Saves an exercise performed.
     *
     * @param exercisePerformed exercise to save
     * @return generated exercise ID
     */
    @Override
    public int saveExercisePerformed(
            final ExercisePerformed exercisePerformed
    ) {

        final String sql = """
                INSERT INTO exercises_performed (
                    workout_id,
                    exercise_name,
                    sets,
                    reps,
                    weight,
                    duration_mins,
                    distance_km,
                    is_cardio
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = Database.connect()) {

            connection.setAutoCommit(false);

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS
                         )) {

                statement.setInt(
                        1,
                        exercisePerformed.getWorkoutId()
                );

                statement.setString(
                        2,
                        exercisePerformed.getExerciseName()
                );

                setNullableInteger(
                        statement,
                        3,
                        exercisePerformed.getSets()
                );

                setNullableInteger(
                        statement,
                        4,
                        exercisePerformed.getReps()
                );

                setNullableDouble(
                        statement,
                        5,
                        exercisePerformed.getWeight()
                );

                statement.setDouble(
                        6,
                        exercisePerformed.getDurationMins()
                );

                setNullableDouble(
                        statement,
                        7,
                        exercisePerformed.getDistanceKm()
                );

                statement.setInt(
                        8,
                        exercisePerformed.getIsCardio()
                                ? 1 : 0
                );

                statement.executeUpdate();

                try (ResultSet generatedKeys =
                             statement.getGeneratedKeys()) {

                    if (generatedKeys.next()) {

                        final int exerciseId =
                                generatedKeys.getInt(1);

                        exercisePerformed.setId(exerciseId);

                        final String userId =
                                getUserIdForWorkout(
                                        connection,
                                        exercisePerformed.getWorkoutId()
                                );

                        updateTotalWorkoutMinutes(
                                connection,
                                userId
                        );

                        connection.commit();

                        return exerciseId;
                    }
                }

                connection.rollback();
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
                    "Failed to save exercise.",
                    exception
            );
        }

        throw new IllegalStateException(
                "Exercise ID was not generated."
        );
    }

    /**
     * Gets every exercise belonging to a workout.
     *
     * @param workoutId workout ID
     * @return exercises for the workout
     */
    @Override
    public List<ExercisePerformed> getExercisesForWorkout(
            final int workoutId
    ) {

        final List<ExercisePerformed> exercises =
                new ArrayList<>();

        final String sql = """
                SELECT
                    id,
                    workout_id,
                    exercise_name,
                    sets,
                    reps,
                    weight,
                    duration_mins,
                    distance_km,
                    is_cardio
                FROM exercises_performed
                WHERE workout_id = ?
                ORDER BY id
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, workoutId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    final Integer sets =
                            getNullableInteger(
                                    resultSet,
                                    "sets"
                            );

                    final Integer reps =
                            getNullableInteger(
                                    resultSet,
                                    "reps"
                            );

                    final Double weight =
                            getNullableDouble(
                                    resultSet,
                                    "weight"
                            );

                    final Double distanceKm =
                            getNullableDouble(
                                    resultSet,
                                    "distance_km"
                            );

                    final ExercisePerformed exercise =
                            new ExercisePerformed(
                                    resultSet.getString(
                                            "exercise_name"
                                    ),
                                    sets,
                                    reps,
                                    weight,
                                    resultSet.getDouble(
                                            "duration_mins"
                                    ),
                                    distanceKm,
                                    resultSet.getInt(
                                            "is_cardio"
                                    ) == 1
                            );

                    exercise.setId(
                            resultSet.getInt("id")
                    );

                    exercise.setWorkoutId(
                            resultSet.getInt(
                                    "workout_id"
                            )
                    );

                    exercises.add(exercise);
                }
            }
        }
        catch (final SQLException exception) {
            throw new RuntimeException(
                    "Failed to load exercises.",
                    exception
            );
        }

        return exercises;
    }

    /**
     * Gets logged workouts belonging to a user.
     *
     * @param userId user ID
     * @return user's logged workouts
     */
    @Override
    public List<LoggedWorkout> getWorkoutsForUser(
            final String userId
    ) {

        final List<LoggedWorkout> workouts =
                new ArrayList<>();

        final LocalDate cutoff =
                LocalDate.now().minusDays(6);

        final String sql = """
                SELECT
                    id,
                    user_id,
                    workout_date
                FROM logged_workouts
                WHERE user_id = ?
                  AND workout_date >= ?
                ORDER BY workout_date DESC, id DESC
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    userId
            );

            statement.setString(
                    2,
                    cutoff.toString()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    final LoggedWorkout workout =
                            new LoggedWorkout(
                                    resultSet.getString(
                                            "user_id"
                                    ),
                                    LocalDate.parse(
                                            resultSet.getString(
                                                    "workout_date"
                                            )
                                    )
                            );

                    final int workoutId =
                            resultSet.getInt("id");

                    workout.setId(workoutId);

                    workout.setExercises(
                            getExercisesForWorkout(
                                    workoutId
                            )
                    );

                    workouts.add(workout);
                }
            }
        }
        catch (final SQLException exception) {
            throw new RuntimeException(
                    "Failed to load workouts.",
                    exception
            );
        }

        return workouts;
    }

    /**
     * Updates an existing workout.
     *
     * @param workout workout to update
     */
    @Override
    public void editWorkout(
            final LoggedWorkout workout
    ) {

        final String sql = """
                UPDATE logged_workouts
                SET workout_date = ?
                WHERE id = ?
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    workout.getDate().toString()
            );

            statement.setInt(
                    2,
                    workout.getId()
            );

            statement.executeUpdate();
        }
        catch (final SQLException exception) {
            throw new RuntimeException(
                    "Failed to edit workout.",
                    exception
            );
        }

        for (ExercisePerformed exercise
                : workout.getExercises()) {

            if (exercise.getWorkoutId() == null) {
                exercise.setWorkoutId(
                        workout.getId()
                );
            }

            if (exercise.getId() == null) {
                saveExercisePerformed(exercise);
            }
            else {
                editExercisePerformed(exercise);
            }
        }
    }

    /**
     * Updates an existing performed exercise.
     *
     * @param exercisePerformed exercise to update
     */
    @Override
    public void editExercisePerformed(
            final ExercisePerformed exercisePerformed
    ) {

        final String sql = """
                UPDATE exercises_performed
                SET
                    workout_id = ?,
                    exercise_name = ?,
                    sets = ?,
                    reps = ?,
                    weight = ?,
                    duration_mins = ?,
                    distance_km = ?,
                    is_cardio = ?
                WHERE id = ?
                """;

        try (Connection connection = Database.connect()) {

            connection.setAutoCommit(false);

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setInt(
                        1,
                        exercisePerformed.getWorkoutId()
                );

                statement.setString(
                        2,
                        exercisePerformed.getExerciseName()
                );

                setNullableInteger(
                        statement,
                        3,
                        exercisePerformed.getSets()
                );

                setNullableInteger(
                        statement,
                        4,
                        exercisePerformed.getReps()
                );

                setNullableDouble(
                        statement,
                        5,
                        exercisePerformed.getWeight()
                );

                statement.setDouble(
                        6,
                        exercisePerformed.getDurationMins()
                );

                setNullableDouble(
                        statement,
                        7,
                        exercisePerformed.getDistanceKm()
                );

                statement.setInt(
                        8,
                        exercisePerformed.getIsCardio()
                                ? 1 : 0
                );

                statement.setInt(
                        9,
                        exercisePerformed.getId()
                );

                statement.executeUpdate();

                final String userId =
                        getUserIdForWorkout(
                                connection,
                                exercisePerformed.getWorkoutId()
                        );

                updateTotalWorkoutMinutes(
                        connection,
                        userId
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
                    "Failed to edit exercise.",
                    exception
            );
        }
    }

    /**
     * Deletes a workout and all exercises belonging
     * to that workout.
     *
     * @param workoutId workout ID
     */
    @Override
    public void deleteWorkout(
            final int workoutId
    ) {

        final String deleteExercisesSql = """
                DELETE FROM exercises_performed
                WHERE workout_id = ?
                """;

        final String deleteWorkoutSql = """
                DELETE FROM logged_workouts
                WHERE id = ?
                """;

        try (Connection connection = Database.connect()) {

            connection.setAutoCommit(false);

            try {
                final String userId =
                        getUserIdForWorkout(
                                connection,
                                workoutId
                        );

                try (PreparedStatement exerciseStatement =
                             connection.prepareStatement(
                                     deleteExercisesSql
                             )) {

                    exerciseStatement.setInt(
                            1,
                            workoutId
                    );

                    exerciseStatement.executeUpdate();
                }

                try (PreparedStatement workoutStatement =
                             connection.prepareStatement(
                                     deleteWorkoutSql
                             )) {

                    workoutStatement.setInt(
                            1,
                            workoutId
                    );

                    workoutStatement.executeUpdate();
                }

                updateTotalWorkoutMinutes(
                        connection,
                        userId
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
                    "Failed to delete workout.",
                    exception
            );
        }
    }

    /**
     * Deletes one performed exercise.
     *
     * @param exercisePerformedId exercise ID
     */
    @Override
    public void deleteExercisePerformed(
            final int exercisePerformedId
    ) {

        final String findUserSql = """
                SELECT logged_workouts.user_id
                FROM exercises_performed
                JOIN logged_workouts
                    ON exercises_performed.workout_id =
                       logged_workouts.id
                WHERE exercises_performed.id = ?
                """;

        final String deleteSql = """
                DELETE FROM exercises_performed
                WHERE id = ?
                """;

        try (Connection connection = Database.connect()) {

            connection.setAutoCommit(false);

            try {
                String userId = null;

                try (PreparedStatement findStatement =
                             connection.prepareStatement(
                                     findUserSql
                             )) {

                    findStatement.setInt(
                            1,
                            exercisePerformedId
                    );

                    try (ResultSet resultSet =
                                 findStatement.executeQuery()) {

                        if (resultSet.next()) {
                            userId =
                                    resultSet.getString(
                                            "user_id"
                                    );
                        }
                    }
                }

                try (PreparedStatement deleteStatement =
                             connection.prepareStatement(
                                     deleteSql
                             )) {

                    deleteStatement.setInt(
                            1,
                            exercisePerformedId
                    );

                    deleteStatement.executeUpdate();
                }

                if (userId != null) {
                    updateTotalWorkoutMinutes(
                            connection,
                            userId
                    );
                }

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
                    "Failed to delete exercise.",
                    exception
            );
        }
    }

    /**
     * Finds the user that owns a workout.
     *
     * @param connection database connection
     * @param workoutId workout ID
     * @return username belonging to the workout
     * @throws SQLException if the user cannot be loaded
     */
    private static String getUserIdForWorkout(
            final Connection connection,
            final int workoutId
    ) throws SQLException {

        final String sql = """
                SELECT user_id
                FROM logged_workouts
                WHERE id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    workoutId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getString(
                            "user_id"
                    );
                }
            }
        }

        throw new SQLException(
                "Could not find user for workout."
        );
    }

    /**
     * Recalculates and stores a user's total workout
     * minutes.
     *
     * @param connection database connection
     * @param userId username
     * @throws SQLException if the update fails
     */
    private static void updateTotalWorkoutMinutes(
            final Connection connection,
            final String userId
    ) throws SQLException {

        final String sql = """
                UPDATE users
                SET total_workout_minutes = (
                    SELECT COALESCE(
                        SUM(exercises_performed.duration_mins),
                        0
                    )
                    FROM logged_workouts
                    LEFT JOIN exercises_performed
                        ON logged_workouts.id =
                           exercises_performed.workout_id
                    WHERE logged_workouts.user_id = ?
                )
                WHERE username = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    userId
            );

            statement.setString(
                    2,
                    userId
            );

            statement.executeUpdate();
        }
    }

    private static void setNullableInteger(
            final PreparedStatement statement,
            final int parameterIndex,
            final Integer value
    ) throws SQLException {

        if (value == null) {
            statement.setNull(
                    parameterIndex,
                    Types.INTEGER
            );
        }
        else {
            statement.setInt(
                    parameterIndex,
                    value
            );
        }
    }

    private static void setNullableDouble(
            final PreparedStatement statement,
            final int parameterIndex,
            final Double value
    ) throws SQLException {

        if (value == null) {
            statement.setNull(
                    parameterIndex,
                    Types.REAL
            );
        }
        else {
            statement.setDouble(
                    parameterIndex,
                    value
            );
        }
    }

    private static Integer getNullableInteger(
            final ResultSet resultSet,
            final String column
    ) throws SQLException {

        final int value =
                resultSet.getInt(column);

        if (resultSet.wasNull()) {
            return null;
        }

        return value;
    }

    private static Double getNullableDouble(
            final ResultSet resultSet,
            final String column
    ) throws SQLException {

        final double value =
                resultSet.getDouble(column);

        if (resultSet.wasNull()) {
            return null;
        }

        return value;
    }
}