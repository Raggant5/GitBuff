package use_case.log_workout.logged_workout.prepare_edit_workout;

public class PrepareEditWorkoutInputData {

    private final int workoutId;

    public PrepareEditWorkoutInputData(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getWorkoutId() {
        return workoutId;
    }
}
