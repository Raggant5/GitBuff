package use_case.log_workout.exercise_performed.delete_exercise;

public class DeleteExerciseInteractor implements DeleteExerciseInputBoundary {

    private final DeleteExerciseOutputBoundary deleteExercisePresenter;

    public DeleteExerciseInteractor(DeleteExerciseOutputBoundary deleteExercisePresenter) {
        this.deleteExercisePresenter = deleteExercisePresenter;
    }

    @Override
    public void execute(DeleteExerciseInputData deleteExerciseInputData) {
        deleteExercisePresenter.prepareSuccessView(new DeleteExerciseOutputData(deleteExerciseInputData.getId()));
    }
}
