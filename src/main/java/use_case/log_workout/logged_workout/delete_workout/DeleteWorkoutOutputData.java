package use_case.log_workout.logged_workout.delete_workout;

public class DeleteWorkoutOutputData {

    private final int workoutId;

    public DeleteWorkoutOutputData(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getWorkoutId() {
        return workoutId;
    }
}
