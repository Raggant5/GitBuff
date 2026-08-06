package use_case.log_workout.logged_workout.add_workout;

import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;
import entity.LoggedWorkoutFactory;

public class AddWorkoutInteractor implements AddWorkoutInputBoundary {

    private final AddWorkoutOutputBoundary addWorkoutPresenter;
    private final AddWorkoutDataAccessInterface workoutDataAccessObject;
    private final LoggedWorkoutFactory loggedWorkoutFactory;

    public AddWorkoutInteractor(AddWorkoutOutputBoundary addWorkoutPresenter,
                                AddWorkoutDataAccessInterface workoutDataAccessObject,
                                LoggedWorkoutFactory loggedWorkoutFactory) {
        this.addWorkoutPresenter = addWorkoutPresenter;
        this.workoutDataAccessObject = workoutDataAccessObject;
        this.loggedWorkoutFactory = loggedWorkoutFactory;
    }

    @Override
    public void execute(AddWorkoutInputData addWorkoutInputData) {
        try {
            final LoggedWorkout workout = loggedWorkoutFactory.create(addWorkoutInputData.getUserId(),
                    addWorkoutInputData.getDate());
            final int workoutId = workoutDataAccessObject.saveWorkout(workout);
            final List<ExercisePerformed> exercises = addWorkoutInputData.getExercises();
            workout.getExercises().addAll(exercises);
            for (ExercisePerformed exercise : exercises) {
                exercise.setWorkoutId(workoutId);
                exercise.setId(workoutDataAccessObject.saveExercisePerformed(exercise));
            }
            addWorkoutPresenter.prepareSuccessView(new AddWorkoutOutputData(workout));
        }
        catch (RuntimeException exc) {
            addWorkoutPresenter.prepareFailView("Unable to save workout. Please try again.");
        }
    }
}
