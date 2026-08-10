package use_case.log_workout.logged_workout.get_workouts;

import java.util.List;

import use_case.log_workout.logged_workout.LoggedWorkoutData;

/**
 * Output Data for the Get Workouts Use Case.
 */
public class GetWorkoutsOutputData {

    private final List<LoggedWorkoutData> workouts;

    public GetWorkoutsOutputData(final List<LoggedWorkoutData> workouts) {
        this.workouts = workouts;
    }

    public List<LoggedWorkoutData> getWorkouts() {
        return this.workouts;
    }
}
