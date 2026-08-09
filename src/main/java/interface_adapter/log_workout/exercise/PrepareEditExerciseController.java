package interface_adapter.log_workout.exercise;

import use_case.log_workout.StrengthDetailsInput;
import use_case.log_workout.exercise_performed.prepare_edit_exercise.PrepareEditExerciseInputBoundary;
import use_case.log_workout.exercise_performed.prepare_edit_exercise.PrepareEditExerciseInputData;

public class PrepareEditExerciseController {
    private final PrepareEditExerciseInputBoundary prepareEditExerciseInteractor;

    public PrepareEditExerciseController(PrepareEditExerciseInputBoundary prepareEditExerciseInteractor) {
        this.prepareEditExerciseInteractor = prepareEditExerciseInteractor;
    }

    /**
     * Executes the Prepare Edit Exercise Use Case.
     * @param exercisePerformed the exercise row selected for editing
     */
    public void execute(ExercisePerformedDisplayData exercisePerformed) {
        final StrengthDetailsInput strengthDetailsInput = new StrengthDetailsInput(exercisePerformed.getSets(),
                exercisePerformed.getReps(), exercisePerformed.getWeight());
        prepareEditExerciseInteractor.execute(new PrepareEditExerciseInputData(exercisePerformed.getId(),
                exercisePerformed.getExerciseName(), strengthDetailsInput, exercisePerformed.getDurationMins(),
                exercisePerformed.getDistanceKm(), exercisePerformed.getIsCardio()));
    }

    /**
     * Switches the exercise editor view to adding exercises.
     */
    public void switchToAddExerciseEditor() {
        prepareEditExerciseInteractor.switchToAddExerciseEditor();
    }
}
