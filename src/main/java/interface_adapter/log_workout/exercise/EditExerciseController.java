package interface_adapter.log_workout.exercise;

import use_case.log_workout.StrengthDetailsInput;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseInputBoundary;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseInputData;

public class EditExerciseController {

    private final EditExerciseInputBoundary editExerciseInteractor;

    public EditExerciseController(EditExerciseInputBoundary editExerciseInteractor) {
        this.editExerciseInteractor = editExerciseInteractor;
    }

    /**
     * Executes the Edit Exercise Use Case.
     * @param id the id of the exercise performed being edited (temporary local id if not yet saved)
     * @param exerciseName the updated exercise name
     * @param strengthDetailsDisplayData the updated sets, reps, and weight if weightlifting
     * @param isCardio whether the exercise is cardio
     * @param distanceKm the updated distance
     * @param durationMins the updated duration
     */
    public void execute(Integer id, String exerciseName, StrengthDetailsDisplayData strengthDetailsDisplayData,
                        boolean isCardio, String distanceKm, String durationMins) {
        final StrengthDetailsInput strengthDetailsInput = new StrengthDetailsInput(
                strengthDetailsDisplayData.getSets(), strengthDetailsDisplayData.getReps(),
                strengthDetailsDisplayData.getWeight());
        editExerciseInteractor.execute(new EditExerciseInputData(id, exerciseName, strengthDetailsInput, isCardio,
                distanceKm, durationMins));
    }

}
