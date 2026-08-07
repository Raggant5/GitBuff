package use_case.log_workout.exercise_performed.prepare_edit_exercise;

import entity.ExercisePerformed;

public class PrepareEditExerciseOutputData {

    private final ExercisePerformed exercisePerformed;

    public PrepareEditExerciseOutputData(ExercisePerformed exercisePerformed) {
        this.exercisePerformed = exercisePerformed;
    }

    public ExercisePerformed getExercisePerformed() {
        return exercisePerformed;
    }
}
