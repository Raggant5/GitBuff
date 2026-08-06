package use_case.log_workout.logged_workout.edit_workout;

import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;

public class EditWorkoutInputData {

    private final LoggedWorkout workout;
    private final List<ExercisePerformed> exercises;
    private final List<ExercisePerformed> exercisesToDelete;

    public EditWorkoutInputData(LoggedWorkout workout, List<ExercisePerformed> exercises,
                                List<ExercisePerformed> exercisesToDelete) {
        this.workout = workout;
        this.exercises = exercises;
        this.exercisesToDelete = exercisesToDelete;
    }

    public LoggedWorkout getWorkout() {
        return workout;
    }

    public List<ExercisePerformed> getExercises() {
        return exercises;
    }

    public List<ExercisePerformed> getExercisesToDelete() {
        return exercisesToDelete;
    }
}
