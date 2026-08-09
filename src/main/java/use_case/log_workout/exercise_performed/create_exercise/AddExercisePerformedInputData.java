package use_case.log_workout.exercise_performed.create_exercise;

import use_case.log_workout.StrengthDetailsInput;

public class AddExercisePerformedInputData {

    private final String name;
    private final StrengthDetailsInput strengthDetailsInput;
    private final boolean isCardio;
    private final String distance;
    private final String duration;

    public AddExercisePerformedInputData(String name, StrengthDetailsInput strengthDetailsInput,
                                         boolean isCardio, String distance, String duration) {
        this.name = name;
        this.strengthDetailsInput = strengthDetailsInput;
        this.isCardio = isCardio;
        this.distance = distance;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public StrengthDetailsInput getStrengthDetailsInput() {
        return strengthDetailsInput;
    }

    public String getSets() {
        return strengthDetailsInput.getSets();
    }

    public String getWeight() {
        return strengthDetailsInput.getWeight();
    }

    public String getReps() {
        return strengthDetailsInput.getReps();
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
