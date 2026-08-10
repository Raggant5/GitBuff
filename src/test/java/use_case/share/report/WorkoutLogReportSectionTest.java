package use_case.share.report;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.ExercisePerformed;
import entity.LoggedWorkout;

class WorkoutLogReportSectionTest {

    private static final double DURATION_MINS = 25.0;

    @Test
    void renderWithNullWorkoutsShowsZeroTotal() {
        final WorkoutLogReportSection section = new WorkoutLogReportSection(null);

        final String rendered = section.render();

        assertTrue(rendered.contains("Total Workouts Completed: 0"));
    }

    @Test
    void renderWithWorkoutsAndExercisesListsThem() {
        final LoggedWorkout workout = new LoggedWorkout("aahir", LocalDate.of(2026, 1, 1));
        workout.getExercises().add(new ExercisePerformed("Push-Ups", null, DURATION_MINS, null, false));

        final WorkoutLogReportSection section = new WorkoutLogReportSection(List.of(workout));

        final String rendered = section.render();

        assertTrue(rendered.contains("Total Workouts Completed: 1"));
        assertTrue(rendered.contains("Push-Ups"));
        assertTrue(rendered.contains("25 mins"));
    }

    @Test
    void hasContentIsAlwaysTrue() {
        final WorkoutLogReportSection section = new WorkoutLogReportSection(null);

        assertTrue(section.hasContent());
    }
}
