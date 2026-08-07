package use_case.log_workout.exercise_performed.create_exercise;

import entity.ExercisePerformed;

/**
 * Output Data for the Add Exercise Performed Use Case.
 */
public class AddExercisePerformedOutputData {

    private final ExercisePerformed exercisePerformed;

    public AddExercisePerformedOutputData(ExercisePerformed exercisePerformed) {
        this.exercisePerformed = exercisePerformed;
    }

    public ExercisePerformed getExercisePerformed() {
        return this.exercisePerformed;
    }

}
