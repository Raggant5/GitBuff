package use_case.log_workout.exercise_performed.delete_exercise;

import entity.ExercisePerformed;

public class DeleteExerciseInteractor implements DeleteExerciseInputBoundary {

    private final DeleteExerciseOutputBoundary deleteExercisePresenter;

    public DeleteExerciseInteractor(DeleteExerciseOutputBoundary deleteExercisePresenter) {
        this.deleteExercisePresenter = deleteExercisePresenter;
    }

    @Override
    public void execute(DeleteExerciseInputData deleteExerciseInputData) {
        final ExercisePerformed exercise = deleteExerciseInputData.getExercisePerformed();
        deleteExercisePresenter.prepareSuccessView(new DeleteExerciseOutputData(exercise));
    }
}
