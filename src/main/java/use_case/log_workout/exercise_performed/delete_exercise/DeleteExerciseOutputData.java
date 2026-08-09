package use_case.log_workout.exercise_performed.delete_exercise;

public class DeleteExerciseOutputData {

    private final Integer id;

    public DeleteExerciseOutputData(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
