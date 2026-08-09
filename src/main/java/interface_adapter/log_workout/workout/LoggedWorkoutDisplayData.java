package interface_adapter.log_workout.workout;

import java.time.LocalDate;
import java.util.List;

import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;

/**
 * Display-only view of a logged workout, so panels/state in this layer don't need
 * to depend on the use case DTO directly.
 */
public class LoggedWorkoutDisplayData {

    private final int id;
    private final LocalDate date;
    private final List<ExercisePerformedDisplayData> exercises;

    public LoggedWorkoutDisplayData(int id, LocalDate date, List<ExercisePerformedDisplayData> exercises) {
        this.id = id;
        this.date = date;
        this.exercises = exercises;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<ExercisePerformedDisplayData> getExercises() {
        return exercises;
    }
}
