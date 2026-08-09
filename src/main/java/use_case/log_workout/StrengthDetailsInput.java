package use_case.log_workout;

/**
 * Groups the raw, unvalidated sets/reps/weight form strings for a weightlifting exercise.
 * Only used at the Add/Edit Exercise boundary, where the interactor validates each field
 * and reports per-field errors - everywhere else already-valid data flows as StrengthDetailsData.
 */
public class StrengthDetailsInput {

    private final String sets;
    private final String reps;
    private final String weight;

    public StrengthDetailsInput(String sets, String reps, String weight) {
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
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
