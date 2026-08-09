package interface_adapter.log_workout.workout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;

/**
 * The state for the Add ExercisePerformed View Model / Add Workout Use Case.
 */
public class WorkoutEditorState {

    private Integer editingWorkoutId;
    private LocalDate date;
    private List<ExercisePerformedDisplayData> exercisesForWorkout = new ArrayList<>();
    private List<Integer> exercisesDeleteStage = new ArrayList<>();
    private String errorMessage = "";
    private Boolean showExerciseEditor = false;
    private int nextTempId = -1;

    public List<ExercisePerformedDisplayData> getExercisesForWorkout() {
        return exercisesForWorkout;
    }

    /**
     * Adding another exercise performed into the list of exercises associated with the workout.
     * @param exercise the exercise to be added
     */
    public void addExercise(ExercisePerformedDisplayData exercise) {
        exercisesForWorkout.add(exercise);
    }

    public int getNextTempId() {
        return nextTempId;
    }

    public void setNextTempId(int nextTempId) {
        this.nextTempId = nextTempId;
    }

    public void setExercisesForWorkout(List<ExercisePerformedDisplayData> exercisesForWorkout) {
        this.exercisesForWorkout = exercisesForWorkout;
    }

    /**
     * Removes the exercise with the given id from the workout being edited.
     * @param id the id of the exercise performed to be removed
     */
    public void removeExerciseById(Integer id) {
        exercisesForWorkout.removeIf(exercise -> Objects.equals(exercise.getId(), id));
    }

    /**
     * Replaces the exercise matching the given exercise's id with the given exercise.
     * @param exercise the updated exercise
     */
    public void replaceExercise(ExercisePerformedDisplayData exercise) {
        for (int i = 0; i < exercisesForWorkout.size(); i++) {
            if (Objects.equals(exercisesForWorkout.get(i).getId(), exercise.getId())) {
                exercisesForWorkout.set(i, exercise);
                break;
            }
        }
    }

    public List<Integer> getExercisesDeleteStage() {
        return exercisesDeleteStage;
    }

    /**
     * Marks the exercise with the given id to be deleted upon saving a workout.
     * @param id id of the exercise performed to be deleted upon saving a workout
     */
    public void addExerciseToBeDeleted(Integer id) {
        exercisesDeleteStage.add(id);
    }

    /**
     * Setting values back to their defaults as to reuse the object for more adding/editing workouts.
     */
    public void reset() {
        editingWorkoutId = null;
        date = null;
        exercisesForWorkout = new ArrayList<>();
        errorMessage = "";
        showExerciseEditor = false;
        exercisesDeleteStage = new ArrayList<>();
        nextTempId = -1;
    }

    public boolean getShowExerciseEditor() {
        return showExerciseEditor;
    }

    public void setShowExerciseEditor(boolean showExerciseEditor) {
        this.showExerciseEditor = showExerciseEditor;
    }

    public Integer getEditingWorkoutId() {
        return editingWorkoutId;
    }

    public void setEditingWorkoutId(Integer editingWorkoutId) {
        this.editingWorkoutId = editingWorkoutId;
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
