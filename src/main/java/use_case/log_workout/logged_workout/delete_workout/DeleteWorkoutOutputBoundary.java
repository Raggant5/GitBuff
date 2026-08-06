package use_case.log_workout.logged_workout.delete_workout;

/**
 * The output boundary for the Delete Workout Use Case.
 */
public interface DeleteWorkoutOutputBoundary {
    /**
     * Prepares the success view for the Delete Workout Case.
     * @param deleteWorkoutOutputData contains the workout id to be deleted
     */
    void prepareSuccessView(DeleteWorkoutOutputData deleteWorkoutOutputData);
}
