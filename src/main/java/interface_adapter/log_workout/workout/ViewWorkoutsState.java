package interface_adapter.log_workout.workout;

import java.util.ArrayList;
import java.util.List;

import entity.LoggedWorkout;

public class ViewWorkoutsState {

    private List<LoggedWorkout> workouts = new ArrayList<>();
    private String error = "";

    public List<LoggedWorkout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<LoggedWorkout> workouts) {
        this.workouts = workouts;
    }

    /**
     * Removes the workout from the history if it matches the workout id.
     * @param workoutId workout id of workout to be removed.
     */
    public void removeWorkout(int workoutId) {
        workouts.removeIf(workout -> workout.getId() == workoutId);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
