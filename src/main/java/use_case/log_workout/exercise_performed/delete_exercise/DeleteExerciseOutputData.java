package use_case.log_workout.exercise_performed.delete_exercise;

import entity.ExercisePerformed;

public class DeleteExerciseOutputData {

    private final ExercisePerformed exercisePerformed;

    public DeleteExerciseOutputData(ExercisePerformed exercisePerformed) {
        this.exercisePerformed = exercisePerformed;
    }

    public ExercisePerformed getExercisePerformed() {
        return exercisePerformed;
    }
}
