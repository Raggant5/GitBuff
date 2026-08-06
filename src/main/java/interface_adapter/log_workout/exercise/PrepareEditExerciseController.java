package interface_adapter.log_workout.exercise;

import entity.ExercisePerformed;
import use_case.log_workout.exercise_performed.prepare_edit_exercise.PrepareEditExerciseInputBoundary;
import use_case.log_workout.exercise_performed.prepare_edit_exercise.PrepareEditExerciseInputData;

public class PrepareEditExerciseController {

    private final PrepareEditExerciseInputBoundary prepareEditExerciseInteractor;

    public PrepareEditExerciseController(PrepareEditExerciseInputBoundary prepareEditExerciseInteractor) {
        this.prepareEditExerciseInteractor = prepareEditExerciseInteractor;
    }

    /**
     * Executes the Prepare Edit Exercise Use Case.
     * @param exercisePerformed the exercise to be edited
     */
    public void execute(ExercisePerformed exercisePerformed) {
        prepareEditExerciseInteractor.execute(new PrepareEditExerciseInputData(exercisePerformed));
    }

    /**
     * Executes the "Switch To Add Exercise" Use Case.
     */
    public void switchToAddExerciseEditor() {
        prepareEditExerciseInteractor.switchToAddExerciseEditor();
    }
}
