package use_case.log_workout.exercise_performed.edit_exercise;

import entity.ExercisePerformed;
import entity.ExercisePerformedFactory;
import entity.StrengthDetails;
import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.ExerciseValidationErrors;

public class EditExerciseInteractor implements EditExerciseInputBoundary {

    private final EditExerciseOutputBoundary presenter;
    private final ExercisePerformedFactory exercisePerformedFactory;

    public EditExerciseInteractor(EditExerciseOutputBoundary presenter,
                                  ExercisePerformedFactory exercisePerformedFactory) {
        this.presenter = presenter;
        this.exercisePerformedFactory = exercisePerformedFactory;
    }

    @Override
    public void execute(EditExerciseInputData inputData) {
        final ExerciseValidationErrors errors = new ExerciseValidationErrors();

        if (inputData.getExerciseName() == null || inputData.getExerciseName().isBlank()) {
            errors.setGeneralError("Exercise name is required.");
        }

        final boolean isCardio = inputData.isCardio();
        final ParsedExercise parsed = validateAndParse(inputData, errors, isCardio);

        if (errors.hasErrors()) {
            presenter.prepareFailView(errors);
        }
        else {
            final Integer id = inputData.getId();
            final ExercisePerformed exercise = exercisePerformedFactory.create(inputData.getExerciseName(),
                    parsed.strengthDetails(), parsed.durationMins(), parsed.distanceKm(), isCardio);

            if (id != null && id > 0) {
                exercise.setId(id);
            }
            final StrengthDetailsData savedDetails = new StrengthDetailsData(exercise.getSets(),
                    exercise.getReps(), exercise.getWeight());
            presenter.prepareSuccessView(new EditExerciseOutputData(id, exercise.getExerciseName(),
                    savedDetails, exercise.getDurationMins(), exercise.getDistanceKm(), exercise.getIsCardio()));
        }
    }

    private ParsedExercise validateAndParse(EditExerciseInputData inputData, ExerciseValidationErrors errors,
                                            boolean isCardio) {
        final Double durationMins = parsePositiveDouble(inputData.getDuration());
        if (durationMins == null) {
            errors.setDurationError("Duration must be a positive number");
        }

        Integer sets = null;
        Integer reps = null;
        Double weight = null;
        Double distanceKm = null;

        if (isCardio) {
            distanceKm = parsePositiveDouble(inputData.getDistance());
            if (distanceKm == null) {
                errors.setDistanceError("Distance must be a positive number");
            }
        }
        else {
            sets = parsePositiveInt(inputData.getSets());
            if (sets == null) {
                errors.setSetsError("Sets must be a positive number");
            }

            reps = parsePositiveInt(inputData.getReps());
            if (reps == null) {
                errors.setRepsError("Reps must be a positive number");
            }

            weight = parseNonNegativeDouble(inputData.getWeight());
            if (weight == null) {
                errors.setWeightError("Weight must be a non-negative number");
            }
        }

        return new ParsedExercise(new StrengthDetails(sets, reps, weight), durationMins, distanceKm);
    }

    private Integer parsePositiveInt(String value) {
        Integer result = null;
        try {
            final int parsed = Integer.parseInt(value);
            if (parsed > 0) {
                result = parsed;
            }
        }
        catch (NumberFormatException exc) {
            result = null;
        }
        return result;
    }

    private Double parseNonNegativeDouble(String value) {
        Double result = null;
        try {
            final double parsed = Double.parseDouble(value);
            if (parsed >= 0) {
                result = parsed;
            }
        }
        catch (NumberFormatException exc) {
            result = null;
        }
        return result;
    }

    private Double parsePositiveDouble(String value) {
        Double result = null;
        try {
            final double parsed = Double.parseDouble(value);
            if (parsed > 0) {
                result = parsed;
            }
        }
        catch (NumberFormatException exc) {
            result = null;
        }
        return result;
    }

    private record ParsedExercise(StrengthDetails strengthDetails, Double durationMins, Double distanceKm) {
    }
}
