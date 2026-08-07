package use_case.log_workout.exercise_performed.prepare_edit_exercise;

import entity.ExercisePerformed;

public class PrepareEditExerciseInputData {

    private final ExercisePerformed exercisePerformed;

    public PrepareEditExerciseInputData(ExercisePerformed exercisePerformed) {
        this.exercisePerformed = exercisePerformed;
    }

    public ExercisePerformed getExercisePerformed() {
        return exercisePerformed;
    }
}
