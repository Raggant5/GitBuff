package use_case.log_workout.logged_workout.edit_workout;

import entity.ExercisePerformed;
import entity.LoggedWorkout;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseDataAccessInterface;

public class EditWorkoutInteractor implements EditWorkoutInputBoundary {

    private final EditWorkoutOutputBoundary presenter;
    private final EditWorkoutDataAccessInterface dataAccess;
    private final DeleteExerciseDataAccessInterface deleteExerciseDataAccess;

    public EditWorkoutInteractor(EditWorkoutOutputBoundary presenter, EditWorkoutDataAccessInterface dataAccess,
                                 DeleteExerciseDataAccessInterface deleteExerciseDataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
        this.deleteExerciseDataAccess = deleteExerciseDataAccess;
    }

    @Override
    public void execute(EditWorkoutInputData inputData) {
        try {
            final LoggedWorkout workout = inputData.getWorkout();
            workout.setExercises(inputData.getExercises());

            for (ExercisePerformed exercise : inputData.getExercisesToDelete()) {
                if (exercise.getId() != null) {
                    deleteExerciseDataAccess.deleteExercisePerformed(exercise.getId());
                }
            }

            dataAccess.editWorkout(workout);

            presenter.prepareSuccessView(new EditWorkoutOutputData(workout));
        }
        catch (RuntimeException exc) {
            presenter.prepareFailView("Unable to save workout. Please try again.");
        }
    }
}
