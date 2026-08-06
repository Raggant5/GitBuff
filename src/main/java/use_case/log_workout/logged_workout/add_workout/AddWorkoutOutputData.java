package use_case.log_workout.logged_workout.add_workout;

import java.time.LocalDate;

import entity.LoggedWorkout;

/**
 * Output Data for the Add Workout Use Case.
 */
public class AddWorkoutOutputData {

    private final LoggedWorkout workout;

    public AddWorkoutOutputData(LoggedWorkout workout) {
        this.workout = workout;
    }

    public LoggedWorkout getWorkout() {
        return workout;
    }

    public int getWorkoutId() {
        return workout.getId();
    }

    public LocalDate getDate() {
        return workout.getDate();
    }

}
