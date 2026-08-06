package use_case.log_workout.logged_workout.edit_workout;

public interface EditWorkoutOutputBoundary {

    /**
     * Prepares the success response.
     * @param outputData data returned after editing the workout successfully
     */
    void prepareSuccessView(EditWorkoutOutputData outputData);

    /**
     * Prepares the failure response.
     * @param errorMessage reason the edit failed
     */
    void prepareFailView(String errorMessage);

}
