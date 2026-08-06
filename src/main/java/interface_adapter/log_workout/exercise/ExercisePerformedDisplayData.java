package interface_adapter.log_workout.exercise;

import entity.ExercisePerformed;

/**
 * Display-only view of an ExercisePerformed, for rendering in panels/lists without the panel depending on
 * the entity directly.
 */
public class ExercisePerformedDisplayData {

    private final ExercisePerformed exercisePerformed;

    public ExercisePerformedDisplayData(ExercisePerformed exercisePerformed) {
        this.exercisePerformed = exercisePerformed;
    }

    public String getExerciseName() {
        return exercisePerformed.getExerciseName();
    }

    public Integer getSets() {
        return exercisePerformed.getSets();
    }

    public Integer getReps() {
        return exercisePerformed.getReps();
    }

    public Double getWeight() {
        return exercisePerformed.getWeight();
    }

    public double getDurationMins() {
        return exercisePerformed.getDurationMins();
    }

    public Double getDistanceKm() {
        return exercisePerformed.getDistanceKm();
    }

    public boolean getIsCardio() {
        return exercisePerformed.getIsCardio();
    }

    /**
     * Gets the underlying entity needed for the Edit/Delete controllers.
     * @return the wrapped exercise performed
     */
    public ExercisePerformed getEntity() {
        return exercisePerformed;
    }
}
