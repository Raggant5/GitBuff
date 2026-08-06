package interface_adapter.log_workout.workout;

import java.time.LocalDate;

import entity.ExercisePerformed;
import entity.LoggedWorkout;

/**
 * Display-only view of a LoggedWorkout, for rendering in panels/lists without the panel depending on the
 * entity directly.
 */
public class LoggedWorkoutDisplayData {

    private final LoggedWorkout workout;

    public LoggedWorkoutDisplayData(LoggedWorkout workout) {
        this.workout = workout;
    }

    public LocalDate getDate() {
        return workout.getDate();
    }

    public int getId() {
        return workout.getId();
    }

    public int getExerciseCount() {
        return workout.getExercises().size();
    }

    public double getTotalDurationMins() {
        double total = 0;
        for (ExercisePerformed exercise : workout.getExercises()) {
            total += exercise.getDurationMins();
        }
        return total;
    }

    /**
     * Gets the underlying entity for the Edit controller.
     * @return the wrapped workout
     */
    public LoggedWorkout getEntity() {
        return workout;
    }
}
