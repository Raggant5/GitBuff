package use_case.log_workout.exercise_performed.create_exercise;

public class AddExercisePerformedInputData {

    private final String name;
    private final String sets;
    private final String weight;
    private final String reps;
    private final boolean isCardio;
    private final String distance;
    private final String duration;

    public AddExercisePerformedInputData(String name, String sets, String weight, String reps,
                                         boolean isCardio, String distance, String duration) {
        this.name = name;
        this.sets = sets;
        this.weight = weight;
        this.reps = reps;
        this.isCardio = isCardio;
        this.distance = distance;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getSets() {
        return sets;
    }

    public String getWeight() {
        return weight;
    }

    public String getReps() {
        return reps;
    }

    public boolean isCardio() {
        return isCardio;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }
}
