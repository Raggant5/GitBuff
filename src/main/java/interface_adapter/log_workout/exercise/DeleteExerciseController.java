package interface_adapter.log_workout.exercise;

import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseInputBoundary;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseInputData;

public class DeleteExerciseController {
    private final DeleteExerciseInputBoundary deleteExerciseInputInteractor;

    public DeleteExerciseController(DeleteExerciseInputBoundary deleteExerciseInputInteractor) {
        this.deleteExerciseInputInteractor = deleteExerciseInputInteractor;
    }

    /**
     * Executes the Delete Exercise Use Case.
     * @param id id of the exercise performed to delete
     */
    public void execute(Integer id) {
        deleteExerciseInputInteractor.execute(new DeleteExerciseInputData(id));
    }

}
