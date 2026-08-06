package use_case.log_workout.exercise_performed.edit_exercise;

import entity.ExercisePerformed;

public class EditExerciseOutputData {

    private final ExercisePerformed exercisePerformed;

    public EditExerciseOutputData(ExercisePerformed exercisePerformed) {
        this.exercisePerformed = exercisePerformed;
    }

    public ExercisePerformed getExercisePerformed() {
        return exercisePerformed;
    }

}
