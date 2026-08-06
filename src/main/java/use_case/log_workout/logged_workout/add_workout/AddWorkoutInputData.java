package use_case.log_workout.logged_workout.add_workout;

import java.time.LocalDate;
import java.util.List;

import entity.ExercisePerformed;

public class AddWorkoutInputData {

    private final String userId;
    private final LocalDate date;
    private final List<ExercisePerformed> exercisesForWorkout;

    public AddWorkoutInputData(String userId, LocalDate date, List<ExercisePerformed> exercisesForWorkout) {
        this.userId = userId;
        this.date = date;
        this.exercisesForWorkout = exercisesForWorkout;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }

    public List<ExercisePerformed> getExercises() {
        return exercisesForWorkout;
    }

}
