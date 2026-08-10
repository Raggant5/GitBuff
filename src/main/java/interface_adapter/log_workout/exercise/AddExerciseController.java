package interface_adapter.log_workout.exercise;

import use_case.log_workout.StrengthDetailsInput;
import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedInputBoundary;
import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedInputData;

public class AddExerciseController {

    private final AddExercisePerformedInputBoundary addExerciseInteractor;

    public AddExerciseController(AddExercisePerformedInputBoundary addExerciseInteractor) {
        this.addExerciseInteractor = addExerciseInteractor;
    }

    /**
     * Executes the Add Exercise Use Case.
     * @param exerciseName the name given for the exercise
     * @param strengthDetailsDisplayData the sets, reps, and weight performed if weightlifting
     * @param isCardio if the exercise was cardio or not
     * @param distanceKm distance in km if a cardio exercise
     * @param durationMins minutes of exercise being performed
     */
    public void execute(String exerciseName, StrengthDetailsDisplayData strengthDetailsDisplayData, boolean isCardio,
                        String distanceKm, String durationMins) {
        final StrengthDetailsInput strengthDetailsInput = new StrengthDetailsInput(
                strengthDetailsDisplayData.getSets(), strengthDetailsDisplayData.getReps(),
                strengthDetailsDisplayData.getWeight());
        addExerciseInteractor.execute(new AddExercisePerformedInputData(exerciseName, strengthDetailsInput,
                isCardio, distanceKm, durationMins));
    }

}
