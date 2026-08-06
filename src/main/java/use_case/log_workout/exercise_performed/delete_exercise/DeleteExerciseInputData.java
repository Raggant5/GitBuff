package use_case.log_workout.exercise_performed.delete_exercise;

import entity.ExercisePerformed;

public class DeleteExerciseInputData {
    private final ExercisePerformed exercisePerformed;

    public DeleteExerciseInputData(ExercisePerformed exercisePerformed) {
        this.exercisePerformed = exercisePerformed;
    }

    public ExercisePerformed getExercisePerformed() {
        return exercisePerformed;
    }

}
