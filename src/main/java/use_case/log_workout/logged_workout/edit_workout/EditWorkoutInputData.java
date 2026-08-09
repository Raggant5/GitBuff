package use_case.log_workout.logged_workout.edit_workout;

import java.util.List;

import use_case.log_workout.exercise_performed.ExercisePerformedInputData;

public class EditWorkoutInputData {

    private final int workoutId;
    private final List<ExercisePerformedInputData> exercises;
    private final List<Integer> exerciseIdsToDelete;

    public EditWorkoutInputData(int workoutId, List<ExercisePerformedInputData> exercises,
                                List<Integer> exerciseIdsToDelete) {
        this.workoutId = workoutId;
        this.exercises = exercises;
        this.exerciseIdsToDelete = exerciseIdsToDelete;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public List<ExercisePerformedInputData> getExercises() {
        return exercises;
    }

    public List<Integer> getExerciseIdsToDelete() {
        return exerciseIdsToDelete;
    }
}
