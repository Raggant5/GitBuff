package use_case.log_workout.logged_workout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;
import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.ExercisePerformedData;

/**
 * Use case boundary DTO mirroring {@code entity.LoggedWorkout}.
 */
public class LoggedWorkoutData {

    private final int id;
    private final LocalDate date;
    private final List<ExercisePerformedData> exercises;

    public LoggedWorkoutData(final int id, final LocalDate date, final List<ExercisePerformedData> exercises) {
        this.id = id;
        this.date = date;
        this.exercises = exercises;
    }

    /**
     * Converts a {@code entity.LoggedWorkout} into its boundary DTO form.
     *
     * @param workout the entity to convert
     * @return the equivalent DTO
     */
    public static LoggedWorkoutData from(final LoggedWorkout workout) {
        final List<ExercisePerformedData> exercises = new ArrayList<>();
        for (final ExercisePerformed exercise : workout.getExercises()) {
            exercises.add(toExercisePerformedData(exercise));
        }
        return new LoggedWorkoutData(workout.getId(), workout.getDate(), exercises);
    }

    private static ExercisePerformedData toExercisePerformedData(final ExercisePerformed exercise) {
        final StrengthDetailsData strengthDetailsData = new StrengthDetailsData(
                exercise.getSets(), exercise.getReps(), exercise.getWeight());
        return new ExercisePerformedData(exercise.getId(), exercise.getExerciseName(), strengthDetailsData,
                exercise.getDurationMins(), exercise.getDistanceKm(), exercise.getIsCardio());
    }

    public int getId() {
        return this.id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public List<ExercisePerformedData> getExercises() {
        return this.exercises;
    }
}
