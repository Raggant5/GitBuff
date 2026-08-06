package use_case.log_workout.logged_workout.prepare_edit_workout;

import entity.LoggedWorkout;

public class PrepareEditWorkoutOutputData {

    private final LoggedWorkout workout;

    public PrepareEditWorkoutOutputData(LoggedWorkout workout) {
        this.workout = workout;
    }

    public LoggedWorkout getWorkout() {
        return workout;
    }
}
