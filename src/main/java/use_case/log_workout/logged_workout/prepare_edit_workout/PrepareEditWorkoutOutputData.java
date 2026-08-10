package use_case.log_workout.logged_workout.prepare_edit_workout;

import java.time.LocalDate;
import java.util.List;

import use_case.log_workout.exercise_performed.ExercisePerformedData;

public class PrepareEditWorkoutOutputData {

    private final int workoutId;
    private final LocalDate date;
    private final List<ExercisePerformedData> exercises;

    public PrepareEditWorkoutOutputData(int workoutId, LocalDate date, List<ExercisePerformedData> exercises) {
        this.workoutId = workoutId;
        this.date = date;
        this.exercises = exercises;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<ExercisePerformedData> getExercises() {
        return exercises;
    }
}
