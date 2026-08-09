package use_case.log_workout;

/**
 * Use case boundary DTO for the validated sets/reps/weight of a weightlifting exercise.
 * Used for InputData/OutputData everywhere except the Add/Edit Exercise validation
 * boundary - by the time data reaches this type it's already known-valid.
 */
public class StrengthDetailsData {

    private final Integer sets;
    private final Integer reps;
    private final Double weight;

    public StrengthDetailsData(Integer sets, Integer reps, Double weight) {
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    public Integer getSets() {
        return sets;
    }

    public Integer getReps() {
        return reps;
    }

    public Double getWeight() {
        return weight;
    }
}
