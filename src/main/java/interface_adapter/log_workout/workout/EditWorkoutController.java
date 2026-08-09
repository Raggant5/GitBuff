package interface_adapter.log_workout.workout;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import use_case.log_workout.StrengthDetailsInput;
import use_case.log_workout.exercise_performed.ExercisePerformedInputData;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutInputBoundary;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutInputData;

public class EditWorkoutController {

    private final EditWorkoutInputBoundary editWorkoutInteractor;

    public EditWorkoutController(EditWorkoutInputBoundary editWorkoutInteractor) {
        this.editWorkoutInteractor = editWorkoutInteractor;
    }

    /**
     * Executes the Edit Workout Use Case.
     * @param workoutId the id of the workout being edited
     * @param exercisesForWorkout the updated list of exercises performed
     * @param exerciseIdsToDelete ids of exercises to delete
     */
    public void execute(int workoutId, List<ExercisePerformedDisplayData> exercisesForWorkout,
                        List<Integer> exerciseIdsToDelete) {
        final List<ExercisePerformedInputData> exercises = new ArrayList<>();
        for (ExercisePerformedDisplayData exercise : exercisesForWorkout) {
            final StrengthDetailsInput strengthDetailsInput = new StrengthDetailsInput(exercise.getSets(),
                    exercise.getReps(), exercise.getWeight());
            exercises.add(new ExercisePerformedInputData(exercise.getId(), exercise.getExerciseName(),
                    strengthDetailsInput, exercise.getDurationMins(), exercise.getDistanceKm(),
                    exercise.getIsCardio()));
        }
        editWorkoutInteractor.execute(new EditWorkoutInputData(workoutId, exercises,
                exerciseIdsToDelete));
    }

}
