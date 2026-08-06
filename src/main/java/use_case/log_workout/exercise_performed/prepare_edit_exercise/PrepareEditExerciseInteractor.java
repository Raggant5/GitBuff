package use_case.log_workout.exercise_performed.prepare_edit_exercise;

import entity.ExercisePerformed;

public class PrepareEditExerciseInteractor implements PrepareEditExerciseInputBoundary {

    private final PrepareEditExerciseOutputBoundary presenter;

    public PrepareEditExerciseInteractor(PrepareEditExerciseOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(PrepareEditExerciseInputData inputData) {
        final ExercisePerformed exercise = inputData.getExercisePerformed();
        presenter.prepareSuccessView(new PrepareEditExerciseOutputData(exercise));
    }

    @Override
    public void switchToAddExerciseEditor() {
        presenter.switchToAddExerciseEditor();
    }
}
