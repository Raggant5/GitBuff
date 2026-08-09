package use_case.log_workout.logged_workout.edit_workout;

import java.util.ArrayList;
import java.util.List;

import entity.ExercisePerformed;
import entity.ExercisePerformedFactory;
import entity.LoggedWorkout;
import entity.StrengthDetails;
import use_case.DataAccessException;
import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.ExercisePerformedData;
import use_case.log_workout.exercise_performed.ExercisePerformedInputData;
import use_case.log_workout.logged_workout.get_workouts.ViewWorkoutDataAccessInterface;

public class EditWorkoutInteractor implements EditWorkoutInputBoundary {

    private final EditWorkoutOutputBoundary presenter;
    private final EditWorkoutDataAccessInterface dataAccess;
    private final ViewWorkoutDataAccessInterface viewDataAccess;
    private final ExercisePerformedFactory exercisePerformedFactory;

    public EditWorkoutInteractor(EditWorkoutOutputBoundary presenter, EditWorkoutDataAccessInterface dataAccess,
                                 ViewWorkoutDataAccessInterface viewDataAccess,
                                 ExercisePerformedFactory exercisePerformedFactory) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
        this.viewDataAccess = viewDataAccess;
        this.exercisePerformedFactory = exercisePerformedFactory;
    }

    @Override
    public void execute(EditWorkoutInputData inputData) {
        try {
            final LoggedWorkout workout = viewDataAccess.getWorkoutById(inputData.getWorkoutId());

            final List<ExercisePerformed> exercises = new ArrayList<>();
            for (ExercisePerformedInputData exerciseData : inputData.getExercises()) {
                final ExercisePerformed exercise = exercisePerformedFactory.create(exerciseData.getExerciseName(),
                        new StrengthDetails(parseInt(exerciseData.getSets()), parseInt(exerciseData.getReps()),
                                parseDouble(exerciseData.getWeight())), exerciseData.getDurationMins(),
                        exerciseData.getDistanceKm(), exerciseData.getIsCardio());
                final Integer id = exerciseData.getId();
                if (id != null && id > 0) {
                    exercise.setId(id);
                }
                exercises.add(exercise);
            }
            workout.setExercises(exercises);

            final LoggedWorkout savedWorkout = dataAccess.editWorkout(workout, inputData.getExerciseIdsToDelete());

            final List<ExercisePerformedData> savedExercises = new ArrayList<>();
            for (ExercisePerformed exercise : savedWorkout.getExercises()) {
                final StrengthDetailsData savedDetails = new StrengthDetailsData(exercise.getSets(),
                        exercise.getReps(), exercise.getWeight());
                savedExercises.add(new ExercisePerformedData(exercise.getId(), exercise.getExerciseName(),
                        savedDetails, exercise.getDurationMins(), exercise.getDistanceKm(),
                        exercise.getIsCardio()));
            }

            presenter.prepareSuccessView(new EditWorkoutOutputData(savedWorkout.getId(), savedWorkout.getDate(),
                    savedExercises));
        }
        catch (DataAccessException exc) {
            presenter.prepareFailView("Unable to save workout. Please try again.");
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
