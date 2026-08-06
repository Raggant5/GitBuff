package use_case.log_workout.exercise_performed.create_exercise;

import entity.ExercisePerformed;
import entity.ExercisePerformedFactory;

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
        if (inputData.getName() == null || inputData.getName().isBlank()) {
            addExercisePresenter.prepareFailView("Exercise name is required.");
        }
        else {
            try {
                final boolean isCardio = inputData.isCardio();
                final double duration = parsePositiveDouble(inputData.getDuration(), "Duration");
                Double distance;
                Integer sets;
                Integer reps;
                Double weight;
                if (isCardio) {
                    distance = parsePositiveDouble(inputData.getDistance(), "Distance");
                    sets = null;
                    reps = null;
                    weight = null;
                }

                else {
                    sets = parsePositiveInt(inputData.getSets(), "Sets");
                    reps = parsePositiveInt(inputData.getReps(), "Reps");
                    weight = parseNonNegativeDouble(inputData.getWeight(), "Weight");
                    distance = null;
                }

                final ExercisePerformed exercisePerformed = exercisePerformedFactory.create(inputData.getName(),
                        sets, reps, weight, duration, distance, isCardio);

                addExercisePresenter.prepareSuccessView(new AddExercisePerformedOutputData(exercisePerformed));
            }
            catch (NumberFormatException exc) {
                addExercisePresenter.prepareFailView("Please ensure all fields are valid.");
            }
            catch (IllegalArgumentException exc) {
                addExercisePresenter.prepareFailView("Please ensure all fields are valid");
            }
        }
    }

    private Integer parsePositiveInt(String value, String fieldName) {
        if (Integer.parseInt(value) > 0) {
            return Integer.parseInt(value);
        }
        else {
            throw new IllegalArgumentException(fieldName + " cannot be negative/zero.");
        }
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
