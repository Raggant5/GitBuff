package entity;

/**
 * Factory for creating exercises performed, a subset of logged workout.
 */
public class ExercisePerformedFactory {
    /**
     * Creates a new ExercisePerformed.
     * @param exerciseName name of the exercise to add
     * @param sets the number of sets performed if weightlifting
     * @param reps number of reps performed if weightlifting
     * @param weight weight in kg if weightlifting
     * @param durationMins minutes of exercise being performed
     * @param distanceKm distance in km if a cardio exercise
     * @param isCardio if the exercise was cardio or not
     * @return the new ExercisePerformed entity
     */
    public ExercisePerformed create(String exerciseName, Integer sets, Integer reps, Double weight,
                            Double durationMins, Double distanceKm, boolean isCardio) {

        return new ExercisePerformed(exerciseName, sets, reps, weight, durationMins, distanceKm, isCardio);
    }
}
