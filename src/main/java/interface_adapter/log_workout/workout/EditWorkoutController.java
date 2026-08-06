package interface_adapter.log_workout.workout;

import java.util.ArrayList;
import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutInputBoundary;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutInputData;

public class EditWorkoutController {

    private final EditWorkoutInputBoundary editWorkoutInteractor;

    public EditWorkoutController(EditWorkoutInputBoundary editWorkoutInteractor) {
        this.editWorkoutInteractor = editWorkoutInteractor;
    }

    /**
     * Executes the Edit Workout Use Case.
     * @param workout the workout entity being edited
     * @param exercises the updated list of exercises performed
     * @param exercisesToDelete list of exercises to delete before adding
     */
    public void execute(LoggedWorkout workout, List<ExercisePerformed> exercises,
                        List<ExercisePerformed> exercisesToDelete) {
        if (exercisesToDelete != null) {
            exercises.removeAll(exercisesToDelete);
            editWorkoutInteractor.execute(new EditWorkoutInputData(workout, exercises, exercisesToDelete));
        }
        else {
            editWorkoutInteractor.execute(new EditWorkoutInputData(workout, exercises, new ArrayList<>()));
        }
    }

}
