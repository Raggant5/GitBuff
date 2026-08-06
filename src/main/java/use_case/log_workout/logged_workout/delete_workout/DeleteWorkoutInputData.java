package use_case.log_workout.logged_workout.delete_workout;

public class DeleteWorkoutInputData {
    private final int workoutId;

    public DeleteWorkoutInputData(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

}
