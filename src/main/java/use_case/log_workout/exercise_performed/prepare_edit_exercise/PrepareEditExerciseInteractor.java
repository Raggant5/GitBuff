package use_case.log_workout.exercise_performed.prepare_edit_exercise;

import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.StrengthDetailsInput;

public class PrepareEditExerciseInteractor implements PrepareEditExerciseInputBoundary {

    private final PrepareEditExerciseOutputBoundary presenter;

    public PrepareEditExerciseInteractor(PrepareEditExerciseOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(PrepareEditExerciseInputData inputData) {
        final StrengthDetailsInput input = inputData.getStrengthDetailsInput();
        final StrengthDetailsData data = new StrengthDetailsData(parseInt(input.getSets()),
                parseInt(input.getReps()), parseDouble(input.getWeight()));
        presenter.prepareSuccessView(new PrepareEditExerciseOutputData(inputData.getId(),
                inputData.getExerciseName(), data, inputData.getDurationMins(), inputData.getDistanceKm(),
                inputData.getIsCardio()));
    }

    @Override
    public void switchToAddExerciseEditor() {
        presenter.switchToAddExerciseEditor();
    }

    private Integer parseInt(String value) {
        Integer result;
        try {
            result = Integer.parseInt(value);
        }
        catch (NumberFormatException exc) {
            result = null;
        }
        return result;
    }

    private Double parseDouble(String value) {
        Double result;
        try {
            result = Double.parseDouble(value);
        }
        catch (NumberFormatException exc) {
            result = null;
        }
        return result;
    }
}
