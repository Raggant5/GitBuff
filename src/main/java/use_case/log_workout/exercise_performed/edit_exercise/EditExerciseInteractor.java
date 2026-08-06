package use_case.log_workout.exercise_performed.edit_exercise;

import entity.ExercisePerformed;

public class EditExerciseInteractor implements EditExerciseInputBoundary {

    private final EditExerciseOutputBoundary presenter;
    private final EditExerciseDataAccessInterface dataAccess;

    public EditExerciseInteractor(EditExerciseOutputBoundary presenter, EditExerciseDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(EditExerciseInputData inputData) {
        if (inputData.getExerciseName() == null || inputData.getExerciseName().isBlank()) {
            presenter.prepareFailView("Exercise name is required.");
        }
        else {
            try {
                final ExercisePerformed exercise = inputData.getExercisePerformed();
                exercise.setExerciseName(inputData.getExerciseName());
                exercise.setDurationMins(parsePositiveDouble(inputData.getDuration(), "Duration"));
                if (inputData.isCardio()) {
                    exercise.setDistanceKm(parsePositiveDouble(inputData.getDistance(), "Distance"));
                    exercise.setSets(null);
                    exercise.setReps(null);
                    exercise.setWeight(null);
                    exercise.setIsCardio(true);
                }
                else {
                    exercise.setSets(parsePositiveInt(inputData.getSets(), "Sets"));
                    exercise.setReps(parsePositiveInt(inputData.getReps(), "Reps"));
                    exercise.setWeight(parseNonNegativeDouble(inputData.getWeight(), "Weight"));
                    exercise.setDistanceKm(null);
                    exercise.setIsCardio(false);
                }
                if (exercise.getId() != null) {
                    dataAccess.editExercisePerformed(exercise);
                }

                presenter.prepareSuccessView(new EditExerciseOutputData(exercise));
            }
            catch (NumberFormatException exc) {
                presenter.prepareFailView("Please ensure all fields are valid.");
            }
            catch (IllegalArgumentException exc) {
                presenter.prepareFailView("Please ensure all fields are valid.");
            }
            catch (RuntimeException exc) {
                presenter.prepareFailView("Unable to save exercise. Please try again.");
            }
        }
    }

    private Integer parsePositiveInt(String value, String fieldName) {
        final int parsed = Integer.parseInt(value);
        if (parsed > 0) {
            return parsed;
        }
        throw new IllegalArgumentException(fieldName + " must be positive.");
    }

    private Double parseNonNegativeDouble(String value, String fieldName) {
        if (Double.parseDouble(value) >= 0) {
            return Double.parseDouble(value);
        }
        else {
            throw new IllegalArgumentException(fieldName + " cannot be negative.");
        }

    }

    private double parsePositiveDouble(String value, String fieldName) {
        final double parsed = Double.parseDouble(value);
        if (parsed <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive.");
        }
        return parsed;
    }
}
