package use_case.log_workout.logged_workout.prepare_edit_workout;

import entity.LoggedWorkout;

public class PrepareEditWorkoutInteractor implements PrepareEditWorkoutInputBoundary {

    private final PrepareEditWorkoutOutputBoundary presenter;

    public PrepareEditWorkoutInteractor(PrepareEditWorkoutOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(PrepareEditWorkoutInputData inputData) {
        final LoggedWorkout workout = inputData.getWorkout();
        presenter.prepareSuccessView(new PrepareEditWorkoutOutputData(workout));
    }
}
