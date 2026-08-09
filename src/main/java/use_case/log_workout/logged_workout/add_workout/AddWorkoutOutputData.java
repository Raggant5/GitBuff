package use_case.log_workout.logged_workout.add_workout;

import java.time.LocalDate;
import java.util.List;

import use_case.log_workout.exercise_performed.ExercisePerformedData;

/**
 * Output Data for the Add Workout Use Case.
 */
public class AddWorkoutOutputData {

    private final int id;
    private final LocalDate date;
    private final List<ExercisePerformedData> exercises;

    public AddWorkoutOutputData(int id, LocalDate date, List<ExercisePerformedData> exercises) {
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

    public List<ExercisePerformedData> getExercises() {
        return exercises;
    }
}
