package interface_adapter.log_workout.exercise;

import entity.ExercisePerformed;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseInputBoundary;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseInputData;

public class EditExerciseController {

    private final EditExerciseInputBoundary editExerciseInteractor;

    public EditExerciseController(EditExerciseInputBoundary editExerciseInteractor) {
        this.editExerciseInteractor = editExerciseInteractor;
    }

    /**
     * Executes the Edit Exercise Use Case.
     * @param exercisePerformed the exercise that is being edited
     * @param exerciseName the new exercise name
     * @param sets the new number of sets performed if weightlifting
     * @param weight the new weight in kg if weightlifting
     * @param reps the new number of reps performed if weightlifting
     * @param isCardio if the exercise was cardio or not
     * @param distanceKm the new distance in km if a cardio exercise
     * @param durationMins the new minutes of exercise being performed
     */
    public void execute(ExercisePerformed exercisePerformed, String exerciseName, String sets, String weight,
                        String reps, boolean isCardio, String distanceKm, String durationMins) {
        editExerciseInteractor.execute(new EditExerciseInputData(exercisePerformed, exerciseName, sets, reps,
                weight, isCardio, distanceKm, durationMins));
    }
}
