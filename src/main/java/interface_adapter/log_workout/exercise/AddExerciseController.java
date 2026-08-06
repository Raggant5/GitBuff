package interface_adapter.log_workout.exercise;

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
     * @param sets the number of sets performed if weightlifting
     * @param weight weight in kg if weightlifting
     * @param reps number of reps performed if weightlifting
     * @param isCardio if the exercise was cardio or not
     * @param distanceKm distance in km if a cardio exercise
     * @param durationMins minutes of exercise being performed
     */
    public void execute(String exerciseName, String sets, String weight, String reps, boolean isCardio,
                        String distanceKm, String durationMins) {
        addExerciseInteractor.execute(new AddExercisePerformedInputData(exerciseName, sets, weight, reps,
                isCardio, distanceKm, durationMins));
    }

}
