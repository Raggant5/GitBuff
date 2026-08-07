package use_case.log_workout.logged_workout.edit_workout;

import entity.LoggedWorkout;

public class EditWorkoutOutputData {

    private final LoggedWorkout workout;

    public EditWorkoutOutputData(LoggedWorkout workout) {
        this.workout = workout;
    }

    public LoggedWorkout getWorkout() {
        return workout;
    }

}
