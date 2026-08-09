package use_case.share.report;

import java.util.List;

import entity.ExercisePerformed;
import entity.LoggedWorkout;

/**
 * Leaf {@link ReportSection} presenting the user's completed workouts.
 *
 * <p>Included in the report only when the user has enabled
 * {@code PrivacySetting.SHARE_WORKOUT_LOGS}; see {@code use_case.share.ShareProgressInteractor}.
 */
public class WorkoutLogReportSection implements ReportSection {

    private static final String BULLET = " \u2022 Date: ";

    private final List<LoggedWorkout> workouts;

    /**
     * Constructs a workout-log section.
     *
     * @param workouts the user's logged workouts, possibly empty or {@code null}
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
        final int totalWorkouts = this.workouts != null ? this.workouts.size() : 0;

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


