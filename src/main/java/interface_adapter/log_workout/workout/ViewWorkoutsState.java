package interface_adapter.log_workout.workout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewWorkoutsState {

    private List<LoggedWorkoutDisplayData> workouts = new ArrayList<>();
    private String error = "";

    public List<LoggedWorkoutDisplayData> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<LoggedWorkoutDisplayData> workouts) {
        this.workouts = workouts;
    }

    /**
     * Adds a workout to the existing list.
     * @param loggedWorkoutDisplayData the workout display data to be added
     */
    public void addWorkout(LoggedWorkoutDisplayData loggedWorkoutDisplayData) {
        this.workouts.add(loggedWorkoutDisplayData);
    }

    /**
     * Replaces the workout matching the given workout's id with the given workout.
     * @param loggedWorkoutDisplayData the workout display data to replace the existing data with
     */
    public void replaceWorkout(LoggedWorkoutDisplayData loggedWorkoutDisplayData) {
        for (int i = 0; i < workouts.size(); i++) {
            if (Objects.equals(workouts.get(i).getId(), loggedWorkoutDisplayData.getId())) {
                workouts.set(i, loggedWorkoutDisplayData);
                break;
            }
        }
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
