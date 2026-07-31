package entity;

/**
 * Represents an exercise with instructions and visual references.
 */
public class Exercise {

    private final String name;
    private final String setsAndReps;
    private final String instructions;
    private final String videoUrl;

    /**
     * Constructs an Exercise instance.
     *
     * @param name exercise title
     * @param setsAndReps sets and reps summary
     * @param instructions step-by-step guidance
     * @param videoUrl link to search or view video/GIF demo
     */
    public Exercise(final String name, final String setsAndReps,
                    final String instructions, final String videoUrl) {
        this.name = name;
        this.setsAndReps = setsAndReps;
        this.instructions = instructions;
        this.videoUrl = videoUrl;
    }

    public String getName() {
        return this.name;
    }

    public String getSetsAndReps() {
        return this.setsAndReps;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }
}
