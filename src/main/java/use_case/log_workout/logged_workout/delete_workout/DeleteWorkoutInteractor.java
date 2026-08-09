package use_case.log_workout.logged_workout.delete_workout;

import use_case.DataAccessException;

public class DeleteWorkoutInteractor implements DeleteWorkoutInputBoundary {

    private final DeleteWorkoutOutputBoundary deleteWorkoutPresenter;
    private final DeleteWorkoutDataAccessInterface workoutDataAccessObject;

    public DeleteWorkoutInteractor(DeleteWorkoutOutputBoundary deleteWorkoutPresenter,
                                   DeleteWorkoutDataAccessInterface workoutDataAccessObject) {
        this.deleteWorkoutPresenter = deleteWorkoutPresenter;
        this.workoutDataAccessObject = workoutDataAccessObject;
    }

    @Override
    public void execute(DeleteWorkoutInputData deleteWorkoutInputData) {
        try {
            workoutDataAccessObject.deleteWorkout(deleteWorkoutInputData.getWorkoutId());
            deleteWorkoutPresenter.prepareSuccessView(
                    new DeleteWorkoutOutputData(deleteWorkoutInputData.getWorkoutId()));
        }
        catch (DataAccessException exc) {
            deleteWorkoutPresenter.prepareFailView("Unable to delete workout. Please try again.");
        }
    }
}
