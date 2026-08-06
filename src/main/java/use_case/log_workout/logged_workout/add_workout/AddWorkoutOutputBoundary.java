package use_case.log_workout.logged_workout.add_workout;

/**
 * The output boundary for the Add Workout Use Case.
 */
public interface AddWorkoutOutputBoundary {
    /**
     * Prepares the success view for the Add Workout Case.
     * @param outputData the output data
     */
    void prepareSuccessView(AddWorkoutOutputData outputData);

    /**
     * Prepares the failure view for the Add Workout Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
