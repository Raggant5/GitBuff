package use_case.share.report;

import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;

/**
 * Leaf ReportSection presenting the user's completed workouts.
 */
public class WorkoutLogReportSection implements ReportSection {

    private static final String BULLET = " • Date: ";

    private final List<LoggedWorkout> workouts;

    /**
     * Constructs a workout-log section.
     *
     * @param workouts the user's logged workouts, possibly empty
     */
    public WorkoutLogReportSection(final List<LoggedWorkout> workouts) {
        this.workouts = workouts;
    }

    @Override
    public boolean hasContent() {
        return true;
    }

    @Override
    public String render() {
        final int totalWorkouts;
        if (this.workouts != null) {
            totalWorkouts = this.workouts.size();
        }
        else {
            totalWorkouts = 0;
        }

        final StringBuilder builder = new StringBuilder();
        builder.append("--- COMPLETED WORKOUTS ---\n");
        builder.append("Total Workouts Completed: ").append(totalWorkouts).append("\n");

        if (this.workouts != null && !this.workouts.isEmpty()) {
            builder.append("Recent Activities:\n");
            for (final LoggedWorkout workout : this.workouts) {
                builder.append(BULLET).append(workout.getDate()).append("\n");
                appendExercises(builder, workout);
            }
        }
        builder.append("\n");
        return builder.toString();
    }

    private void appendExercises(final StringBuilder builder, final LoggedWorkout workout) {
        if (workout.getExercises() != null) {
            for (final ExercisePerformed exercise : workout.getExercises()) {
                builder.append("   - ").append(exercise.getExerciseName())
                        .append(" (").append((int) exercise.getDurationMins()).append(" mins)\n");
            }
        }
    }
}
