package use_case.log_workout.exercise_performed.delete_exercise;

public class DeleteExerciseInputData {
    private final Integer id;

    public DeleteExerciseInputData(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
