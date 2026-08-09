package use_case.log_workout.logged_workout.prepare_edit_workout;

import java.util.ArrayList;
import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;
import use_case.DataAccessException;
import use_case.log_workout.StrengthDetailsData;
import use_case.log_workout.exercise_performed.ExercisePerformedData;
import use_case.log_workout.logged_workout.get_workouts.ViewWorkoutDataAccessInterface;

public class PrepareEditWorkoutInteractor implements PrepareEditWorkoutInputBoundary {

    private final PrepareEditWorkoutOutputBoundary presenter;
    private final ViewWorkoutDataAccessInterface workoutDataAccessObject;

    public PrepareEditWorkoutInteractor(PrepareEditWorkoutOutputBoundary presenter,
                                        ViewWorkoutDataAccessInterface workoutDataAccessObject) {
        this.presenter = presenter;
        this.workoutDataAccessObject = workoutDataAccessObject;
    }

    @Override
    public void execute(PrepareEditWorkoutInputData inputData) {
        try {
            final LoggedWorkout workout = workoutDataAccessObject.getWorkoutById(inputData.getWorkoutId());

            final List<ExercisePerformedData> exercises = new ArrayList<>();
            for (ExercisePerformed exercise : workout.getExercises()) {
                final StrengthDetailsData details = new StrengthDetailsData(exercise.getSets(), exercise.getReps(),
                        exercise.getWeight());
                exercises.add(new ExercisePerformedData(exercise.getId(), exercise.getExerciseName(),
                        details, exercise.getDurationMins(), exercise.getDistanceKm(), exercise.getIsCardio()));
            }

            presenter.prepareSuccessView(new PrepareEditWorkoutOutputData(workout.getId(), workout.getDate(),
                    exercises));
        }
        catch (DataAccessException exc) {
            presenter.prepareFailView("Unable to load workout. Please try again.");
        }
    }
}
