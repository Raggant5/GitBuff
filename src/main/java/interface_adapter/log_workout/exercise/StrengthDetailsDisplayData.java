package interface_adapter.log_workout.exercise;

/**
 * View-facing sets/reps/weight for a weightlifting exercise, so panels/state in this
 * layer don't need to depend on the use case's StrengthDetailsData directly.
 */
public class StrengthDetailsDisplayData {

    private final String sets;
    private final String reps;
    private final String weight;

    public StrengthDetailsDisplayData(String sets, String reps, String weight) {
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    /**
     * Formats validated numeric output data for display. Blank string for null (e.g. a
     * cardio exercise has no sets/reps/weight).
     * @param sets the number of sets if weightlifting exercise
     * @param reps the number of reps if weightlifting exercise
     * @param weight amount of weight per rep if weightlifting
     */
    public StrengthDetailsDisplayData(Integer sets, Integer reps, Double weight) {
        this(display(sets), display(reps), display(weight));
    }

    private static String display(Object value) {
        String result = "";
        if (value != null) {
            result = String.valueOf(value);
        }
        return result;
    }

    public String getSets() {
        return sets;
    }

    public String getReps() {
        return reps;
    }

    public String getWeight() {
        return weight;
    }
}
