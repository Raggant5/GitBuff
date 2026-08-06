package interface_adapter.log_workout.workout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;

/**
 * The state for the Add ExercisePerformed View Model / Add Workout Use Case.
 */
public class WorkoutEditorState {

    private LoggedWorkout editingWorkout;
    private LocalDate date;
    private List<ExercisePerformed> exercisesForWorkout = new ArrayList<>();
    private List<ExercisePerformed> exercisesDeleteStage = new ArrayList<>();
    private String errorMessage = "";
    private Boolean showExerciseEditor = false;

    public List<ExercisePerformed> getExercisesForWorkout() {
        return exercisesForWorkout;
    }

    /**
     * Adding another exercise performed into the list of exercises associated with the workout.
     * @param exercisePerformed the exercise to be added
     */
    public void addExercise(ExercisePerformed exercisePerformed) {
        exercisesForWorkout.add(exercisePerformed);
    }

    public void setExercisesForWorkout(List<ExercisePerformed> exercisesForWorkout) {
        this.exercisesForWorkout = exercisesForWorkout;
    }

    /**
     * Remove the given exercise performed from the workout being edited.
     * @param exercisePerformed the exercise performed to be removed
     */
    public void removeExercise(ExercisePerformed exercisePerformed) {
        exercisesForWorkout.remove(exercisePerformed);
    }

    public List<ExercisePerformed> getExercisesDeleteStage() {
        return exercisesDeleteStage;
    }

    /**
     * Adds an exercise performed to be deleted upon saving a workout.
     * @param exercisePerformed exercise performed to be deleted upon saving a workout
     */
    public void addExerciseToBeDeleted(ExercisePerformed exercisePerformed) {
        exercisesDeleteStage.add(exercisePerformed);
    }

    /**
     * Setting values back to their defaults as to reuse the object for more adding/editing workouts.
     */
    public void reset() {
        editingWorkout = null;
        date = null;
        exercisesForWorkout = new ArrayList<>();
        errorMessage = "";
        showExerciseEditor = false;
        exercisesDeleteStage = new ArrayList<>();
    }

    public boolean getShowExerciseEditor() {
        return showExerciseEditor;
    }

    public void setShowExerciseEditor(boolean showExerciseEditor) {
        this.showExerciseEditor = showExerciseEditor;
    }

    public LoggedWorkout getEditingWorkout() {
        return editingWorkout;
    }

    public void setEditingWorkout(LoggedWorkout editingWorkout) {
        this.editingWorkout = editingWorkout;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
