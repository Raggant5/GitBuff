package use_case.log_workout.logged_workout.get_workouts;

import entity.LoggedWorkout;
import use_case.DataAccessException;
import use_case.log_workout.logged_workout.LoggedWorkoutData;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for fetching a user's logged workout history on demand.
 */
public class GetWorkoutsInteractor implements GetWorkoutsInputBoundary {

    private final GetWorkoutsOutputBoundary presenter;
    private final ViewWorkoutDataAccessInterface workoutDataAccessObject;

    public GetWorkoutsInteractor(final GetWorkoutsOutputBoundary presenter,
                                 final ViewWorkoutDataAccessInterface workoutDataAccessObject) {
        this.presenter = presenter;
        this.workoutDataAccessObject = workoutDataAccessObject;
    }

    @Override
    public void execute(final GetWorkoutsInputData inputData) {
        try {
            final List<LoggedWorkout> workouts = this.workoutDataAccessObject.getWorkoutsForUser(
                    inputData.getUserId());
            final List<LoggedWorkoutData> workoutsData = new ArrayList<>();
            for (final LoggedWorkout workout : workouts) {
                workoutsData.add(LoggedWorkoutData.from(workout));
            }
            this.presenter.prepareSuccessView(new GetWorkoutsOutputData(workoutsData));
        }
        catch (final DataAccessException exception) {
            this.presenter.prepareFailView("Unable to load workout history. Please try again.");
        }
    }
}
