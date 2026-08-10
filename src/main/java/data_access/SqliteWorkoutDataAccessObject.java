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
import entity.StrengthDetails;
import use_case.DataAccessException;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.get_workouts.ViewWorkoutDataAccessInterface;

/**
 * SQLite data access object for logged workouts and exercises performed.
 */
public final class SqliteWorkoutDataAccessObject
        implements AddWorkoutDataAccessInterface,
        ViewWorkoutDataAccessInterface,
        EditWorkoutDataAccessInterface,
        DeleteWorkoutDataAccessInterface {

    private static final int SETS_PARAMETER_INDEX = 3;
    private static final int REPS_PARAMETER_INDEX = 4;
    private static final int WEIGHT_PARAMETER_INDEX = 5;
    private static final int DURATION_PARAMETER_INDEX = 6;
    private static final int DISTANCE_PARAMETER_INDEX = 7;
    private static final int CARDIO_PARAMETER_INDEX = 8;
    private static final int ID_PARAMETER_INDEX = 9;
    private static final int RECENT_WORKOUT_DAYS = 6;
    private static final String USER_ID_COLUMN = "user_id";

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
                final SqliteUserDataAccessObject userDao = new SqliteUserDataAccessObject();
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
            throw new DataAccessException(
                    "Failed to save workout.",
                    exception
            );
        }

        throw new IllegalStateException(
                "Workout ID was not generated."
        );
    }

    @Override
    public int saveExercisePerformed(
            final ExercisePerformed exercisePerformed
    ) {

        try (Connection connection = Database.connect()) {
            return saveExerciseInTransaction(
                    connection,
                    exercisePerformed
            );
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to save exercise.",
                    exception
            );
        }
    }

    private static int saveExerciseInTransaction(
            final Connection connection,
            final ExercisePerformed exercisePerformed
    ) throws SQLException {
        connection.setAutoCommit(false);
        try {
            final int exerciseId =
                    insertExercisePerformedRow(connection, exercisePerformed);
            final String userId =
                    getUserIdForWorkout(
                            connection,
                            exercisePerformed.getWorkoutId()
                    );
            updateTotalWorkoutMinutes(connection, userId);
            connection.commit();
            return exerciseId;
        }
        catch (final SQLException exception) {
            connection.rollback();
            throw exception;
        }
        finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Inserts a performed exercise row using the given connection, without committing.
     *
     * @param connection shared database connection
     * @param exercisePerformed exercise to insert
     * @return generated exercise ID
     * @throws SQLException if the insert fails
     * @throws IllegalStateException if no exercise ID is generated
     */
    private static int insertExercisePerformedRow(
            final Connection connection,
            final ExercisePerformed exercisePerformed
    ) throws SQLException {

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
                    SETS_PARAMETER_INDEX,
                    exercisePerformed.getSets()
            );

            setNullableInteger(
                    statement,
                    REPS_PARAMETER_INDEX,
                    exercisePerformed.getReps()
            );

            setNullableDouble(
                    statement,
                    WEIGHT_PARAMETER_INDEX,
                    exercisePerformed.getWeight()
            );

            statement.setDouble(
                    DURATION_PARAMETER_INDEX,
                    exercisePerformed.getDurationMins()
            );

            setNullableDouble(
                    statement,
                    DISTANCE_PARAMETER_INDEX,
                    exercisePerformed.getDistanceKm()
            );

            statement.setInt(
                    CARDIO_PARAMETER_INDEX,
                    getCardioValue(exercisePerformed)
            );

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    final int exerciseId =
                            generatedKeys.getInt(1);

                    exercisePerformed.setId(exerciseId);

                    return exerciseId;
                }
            }
        }

        throw new IllegalStateException(
                "Exercise ID was not generated."
        );
    }

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
                                    new StrengthDetails(sets, reps, weight),
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
            throw new DataAccessException(
                    "Failed to load exercises.",
                    exception
            );
        }

        return exercises;
    }

    @Override
    public List<LoggedWorkout> getWorkoutsForUser(
            final String userId
    ) {

        final List<LoggedWorkout> workouts =
                new ArrayList<>();

        final LocalDate cutoff =
                LocalDate.now().minusDays(RECENT_WORKOUT_DAYS);

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
                                            USER_ID_COLUMN
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
            throw new DataAccessException(
                    "Failed to load workouts.",
                    exception
            );
        }

        return workouts;
    }

    @Override
    public LoggedWorkout getWorkoutById(
            final int workoutId
    ) {

        final String sql = """
                SELECT
                    id,
                    user_id,
                    workout_date
                FROM logged_workouts
                WHERE id = ?
                """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, workoutId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    final LoggedWorkout workout =
                            new LoggedWorkout(
                                    resultSet.getString(
                                            USER_ID_COLUMN
                                    ),
                                    LocalDate.parse(
                                            resultSet.getString(
                                                    "workout_date"
                                            )
                                    )
                            );

                    workout.setId(workoutId);

                    workout.setExercises(
                            getExercisesForWorkout(
                                    workoutId
                            )
                    );

                    return workout;
                }
            }
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to load workout.",
                    exception
            );
        }

        throw new DataAccessException(
                "Workout not found: " + workoutId
        );
    }

    @Override
    public LoggedWorkout editWorkout(
            final LoggedWorkout workout,
            final List<Integer> exerciseIdsToDelete
    ) {

        try (Connection connection = Database.connect()) {
            editWorkoutInTransaction(
                    connection,
                    workout,
                    exerciseIdsToDelete
            );
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to edit workout.",
                    exception
            );
        }

        return workout;
    }

    private static void editWorkoutInTransaction(
            final Connection connection,
            final LoggedWorkout workout,
            final List<Integer> exerciseIdsToDelete
    ) throws SQLException {
        connection.setAutoCommit(false);
        try {
            deleteExercises(connection, exerciseIdsToDelete);
            updateWorkoutDate(connection, workout);
            saveExercises(connection, workout);

            final String userId = workout.getUserId();
            if (userId != null && !userId.isBlank()) {
                updateTotalWorkoutMinutes(connection, userId);
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

    private static void deleteExercises(
            final Connection connection,
            final List<Integer> exerciseIdsToDelete
    ) throws SQLException {
        for (Integer exerciseId : exerciseIdsToDelete) {
            if (exerciseId != null && exerciseId > 0) {
                deleteExercisePerformedRow(connection, exerciseId);
            }
        }
    }

    private static void updateWorkoutDate(
            final Connection connection,
            final LoggedWorkout workout
    ) throws SQLException {
        final String sql = """
                UPDATE logged_workouts
                SET workout_date = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, workout.getDate().toString());
            statement.setInt(2, workout.getId());
            statement.executeUpdate();
        }
    }

    private static void saveExercises(
            final Connection connection,
            final LoggedWorkout workout
    ) throws SQLException {
        for (ExercisePerformed exercise : workout.getExercises()) {
            if (exercise.getWorkoutId() == null) {
                exercise.setWorkoutId(workout.getId());
            }

            if (exercise.getId() == null) {
                insertExercisePerformedRow(connection, exercise);
            }
            else {
                updateExercisePerformedRow(connection, exercise);
            }
        }
    }

    private static void updateExercisePerformedRow(
            final Connection connection,
            final ExercisePerformed exercisePerformed
    ) throws SQLException {

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
                    SETS_PARAMETER_INDEX,
                    exercisePerformed.getSets()
            );

            setNullableInteger(
                    statement,
                    REPS_PARAMETER_INDEX,
                    exercisePerformed.getReps()
            );

            setNullableDouble(
                    statement,
                    WEIGHT_PARAMETER_INDEX,
                    exercisePerformed.getWeight()
            );

            statement.setDouble(
                    DURATION_PARAMETER_INDEX,
                    exercisePerformed.getDurationMins()
            );

            setNullableDouble(
                    statement,
                    DISTANCE_PARAMETER_INDEX,
                    exercisePerformed.getDistanceKm()
            );

            statement.setInt(
                    CARDIO_PARAMETER_INDEX,
                    getCardioValue(exercisePerformed)
            );

            statement.setInt(
                    ID_PARAMETER_INDEX,
                    exercisePerformed.getId()
            );

            statement.executeUpdate();
        }
    }

    private static void deleteExercisePerformedRow(
            final Connection connection,
            final int exercisePerformedId
    ) throws SQLException {

        final String sql = """
                DELETE FROM exercises_performed
                WHERE id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    exercisePerformedId
            );

            statement.executeUpdate();
        }
    }

    @Override
    public void deleteWorkout(
            final int workoutId
    ) {
        try (Connection connection = Database.connect()) {
            deleteWorkoutInTransaction(connection, workoutId);
        }
        catch (final SQLException exception) {
            throw new DataAccessException(
                    "Failed to delete workout.",
                    exception
            );
        }
    }

    private static void deleteWorkoutInTransaction(
            final Connection connection,
            final int workoutId
    ) throws SQLException {
        connection.setAutoCommit(false);
        try {
            final String userId = getUserIdForWorkout(connection, workoutId);
            deleteExercisesForWorkout(connection, workoutId);
            deleteWorkoutRow(connection, workoutId);
            updateTotalWorkoutMinutes(connection, userId);
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

    private static void deleteExercisesForWorkout(
            final Connection connection,
            final int workoutId
    ) throws SQLException {
        final String sql = """
                DELETE FROM exercises_performed
                WHERE workout_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, workoutId);
            statement.executeUpdate();
        }
    }

    private static void deleteWorkoutRow(
            final Connection connection,
            final int workoutId
    ) throws SQLException {
        final String sql = """
                DELETE FROM logged_workouts
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, workoutId);
            statement.executeUpdate();
        }
    }

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
                            USER_ID_COLUMN
                    );
                }
            }
        }

        throw new SQLException(
                "Could not find user for workout."
        );
    }

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

        final int value = resultSet.getInt(column);

        Integer result = value;
        if (resultSet.wasNull()) {
            result = null;
        }

        return result;
    }

    private static Double getNullableDouble(
            final ResultSet resultSet,
            final String column
    ) throws SQLException {

        final double value = resultSet.getDouble(column);

        Double result = value;
        if (resultSet.wasNull()) {
            result = null;
        }

        return result;
    }

    private static int getCardioValue(
            final ExercisePerformed exercisePerformed
    ) {
        int cardioValue = 0;
        if (exercisePerformed.getIsCardio()) {
            cardioValue = 1;
        }
        return cardioValue;
    }
}

