package entity;

/**
 * Groups the sets, reps, and weight for a weightlifting exercise.
 */
public class StrengthDetails {

    private final Integer sets;
    private final Integer reps;
    private final Double weight;

    public StrengthDetails(Integer sets, Integer reps, Double weight) {
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
