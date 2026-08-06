package use_case.log_workout.logged_workout.delete_workout;

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
        workoutDataAccessObject.deleteWorkout(deleteWorkoutInputData.getWorkoutId());
        deleteWorkoutPresenter.prepareSuccessView(new DeleteWorkoutOutputData(deleteWorkoutInputData.getWorkoutId()));
    }
}
