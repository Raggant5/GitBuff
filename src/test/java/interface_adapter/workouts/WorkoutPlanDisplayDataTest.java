package interface_adapter.workouts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class WorkoutPlanDisplayDataTest {

    @Test
    void gettersReturnConstructorValues() {
        final RecommendedExerciseDisplayData exercise = new RecommendedExerciseDisplayData(
                "Squat", 3, 10, 5, "Legs", "Barbell", "Lower with control", "");
        final WorkoutPlanDisplayData plan = new WorkoutPlanDisplayData(
                "Monday, Aug 3", "Leg Day", "Lower body strength", 45, 300, 20, 30, List.of(exercise));

        assertEquals("Monday, Aug 3", plan.getDate());
        assertEquals("Leg Day", plan.getTitle());
        assertEquals("Lower body strength", plan.getDescription());
        assertEquals(45, plan.getEstimatedDurationMinutes());
        assertEquals(300, plan.getEstimatedCaloriesBurned());
        assertEquals(20, plan.getEstimatedFatBurnedGrams());
        assertEquals(30, plan.getEstimatedCarbsBurnedGrams());
        assertEquals(List.of(exercise), plan.getExercises());
    }
}
