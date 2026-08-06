package use_case.log_workout.exercise_performed.create_exercise;

/**
 * Input Boundary for actions which are related to adding exercise performed within a logged workout.
 */
public interface AddExercisePerformedInputBoundary {

    /**
     * Executes the add exercise performed use case.
     * @param addExercisePerformedInputData the input data
     */
    void execute(AddExercisePerformedInputData addExercisePerformedInputData);
}
