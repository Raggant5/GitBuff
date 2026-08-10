package entity;

/**
 * Factory for creating exercises performed, a subset of logged workout.
 */
public class ExercisePerformedFactory {
    /**
     * Creates a new ExercisePerformed.
     * @param exerciseName name of the exercise to add
     * @param strengthDetails the sets, reps, and weight performed if weightlifting
     * @param durationMins minutes of exercise being performed
     * @param distanceKm distance in km if a cardio exercise
     * @param isCardio if the exercise was cardio or not
     * @return the new ExercisePerformed entity
     */
    public ExercisePerformed create(String exerciseName, StrengthDetails strengthDetails,
                            Double durationMins, Double distanceKm, boolean isCardio) {

        return new ExercisePerformed(exerciseName, strengthDetails, durationMins, distanceKm, isCardio);
    }
}
