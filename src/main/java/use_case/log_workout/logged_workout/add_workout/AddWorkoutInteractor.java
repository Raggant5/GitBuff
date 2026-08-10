package use_case.log_workout.logged_workout.add_workout;

import java.util.ArrayList;
import java.util.List;

import entity.ExercisePerformed;
import entity.ExercisePerformedFactory;
import entity.LoggedWorkout;
import entity.LoggedWorkoutFactory;
import entity.StrengthDetails;
import use_case.DataAccessException;
import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.ExercisePerformedData;
import use_case.log_workout.exercise_performed.ExercisePerformedInputData;

public class AddWorkoutInteractor implements AddWorkoutInputBoundary {

    private final AddWorkoutOutputBoundary addWorkoutPresenter;
    private final AddWorkoutDataAccessInterface workoutDataAccessObject;
    private final LoggedWorkoutFactory loggedWorkoutFactory;
    private final ExercisePerformedFactory exercisePerformedFactory;

    public AddWorkoutInteractor(AddWorkoutOutputBoundary addWorkoutPresenter,
                                AddWorkoutDataAccessInterface workoutDataAccessObject,
                                LoggedWorkoutFactory loggedWorkoutFactory,
                                ExercisePerformedFactory exercisePerformedFactory) {
        this.addWorkoutPresenter = addWorkoutPresenter;
        this.workoutDataAccessObject = workoutDataAccessObject;
        this.loggedWorkoutFactory = loggedWorkoutFactory;
        this.exercisePerformedFactory = exercisePerformedFactory;
    }

    @Override
    public void execute(AddWorkoutInputData addWorkoutInputData) {
        try {
            final LoggedWorkout workout = loggedWorkoutFactory.create(addWorkoutInputData.getUserId(),
                    addWorkoutInputData.getDate());
            final int workoutId = workoutDataAccessObject.saveWorkout(workout);

            final List<ExercisePerformedData> savedExercises = new ArrayList<>();
            for (ExercisePerformedInputData exerciseData : addWorkoutInputData.getExercises()) {
                final ExercisePerformed exercise = exercisePerformedFactory.create(exerciseData.getExerciseName(),
                        new StrengthDetails(parseInt(exerciseData.getSets()), parseInt(exerciseData.getReps()),
                                parseDouble(exerciseData.getWeight())), exerciseData.getDurationMins(),
                        exerciseData.getDistanceKm(), exerciseData.getIsCardio());
                exercise.setWorkoutId(workoutId);
                exercise.setId(workoutDataAccessObject.saveExercisePerformed(exercise));
                final StrengthDetailsData savedDetails = new StrengthDetailsData(exercise.getSets(),
                        exercise.getReps(), exercise.getWeight());
                savedExercises.add(new ExercisePerformedData(exercise.getId(), exercise.getExerciseName(),
                        savedDetails, exercise.getDurationMins(), exercise.getDistanceKm(),
                        exercise.getIsCardio()));
            }

            addWorkoutPresenter.prepareSuccessView(new AddWorkoutOutputData(workoutId, workout.getDate(),
                    savedExercises));
        }
        catch (DataAccessException exc) {
            addWorkoutPresenter.prepareFailView("Unable to save workout. Please try again.");
        }
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
