package use_case.log_workout.exercise_performed.create_exercise;

import entity.ExercisePerformed;
import entity.ExercisePerformedFactory;
import entity.StrengthDetails;
import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.ExerciseValidationErrors;

public class AddExercisePerformedInteractor implements AddExercisePerformedInputBoundary {

    private final AddExercisePerformedOutputBoundary addExercisePresenter;
    private final ExercisePerformedFactory exercisePerformedFactory;

    public AddExercisePerformedInteractor(AddExercisePerformedOutputBoundary addExercisePresenter,
                                  ExercisePerformedFactory exercisePerformedFactory) {
        this.addExercisePresenter = addExercisePresenter;
        this.exercisePerformedFactory = exercisePerformedFactory;
    }

    @Override
    public void execute(AddExercisePerformedInputData inputData) {
        final ExerciseValidationErrors errors = new ExerciseValidationErrors();
        if (inputData.getName() == null || inputData.getName().isBlank()) {
            errors.setGeneralError("Exercise name is required.");
        }

        final boolean isCardio = inputData.isCardio();
        final Double duration = parsePositiveDouble(inputData.getDuration());
        if (duration == null) {
            errors.setDurationError("Duration must be a positive number");
        }

        final Double distance;
        final Integer sets;
        final Integer reps;
        final Double weight;
        if (isCardio) {
            distance = parsePositiveDouble(inputData.getDistance());
            if (distance == null) {
                errors.setDistanceError("Distance must be a positive number");
            }
            sets = null;
            reps = null;
            weight = null;
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
            distance = null;
        }

        if (errors.hasErrors()) {
            addExercisePresenter.prepareFailView(errors);
        }
        else {
            final ExercisePerformed exercisePerformed = exercisePerformedFactory.create(inputData.getName(),
                    new StrengthDetails(sets, reps, weight), duration, distance, isCardio);

            final StrengthDetailsData savedDetails = new StrengthDetailsData(exercisePerformed.getSets(),
                    exercisePerformed.getReps(), exercisePerformed.getWeight());
            addExercisePresenter.prepareSuccessView(new AddExercisePerformedOutputData(
                    exercisePerformed.getId(), exercisePerformed.getExerciseName(),
                    savedDetails, exercisePerformed.getDurationMins(),
                    exercisePerformed.getDistanceKm(), exercisePerformed.getIsCardio()));
        }
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
}
