package use_case.log_workout.logged_workout.prepare_edit_workout;

import entity.LoggedWorkout;

public class PrepareEditWorkoutInputData {

    private final LoggedWorkout workout;

    public PrepareEditWorkoutInputData(LoggedWorkout workout) {
        this.workout = workout;
    }

    public LoggedWorkout getWorkout() {
        return workout;
    }
}
