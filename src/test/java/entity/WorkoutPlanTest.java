package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

class WorkoutPlanTest {

    @Test
    void gettersReturnConstructorValues() {
        final Exercise exercise = new Exercise(
                "Push Up", 3, 10, 5, "Chest", "None", "Do it", "http://video",
                "Strength", "Upper Body", "Medium", "Bodyweight");
        final WorkoutPlan plan = new WorkoutPlan(
                "2026-08-10", "Push Day", "Upper body push workout",
                "Strength", "Upper Body", "Medium", "Chest", "Bodyweight",
                45, 300, 20, 30, List.of(exercise));

        assertNull(plan.getId());
        assertEquals("2026-08-10", plan.getDate());
        assertEquals("Push Day", plan.getTitle());
        assertEquals("Upper body push workout", plan.getDescription());
        assertEquals("Strength", plan.getCategory());
        assertEquals("Upper Body", plan.getSubCategory());
        assertEquals("Medium", plan.getIntensityLevel());
        assertEquals("Chest", plan.getTargetMuscleGroup());
        assertEquals("Bodyweight", plan.getEquipmentType());
        assertEquals(45, plan.getEstimatedDurationMinutes());
        assertEquals(300, plan.getEstimatedCaloriesBurned());
        assertEquals(20, plan.getEstimatedFatBurnedGrams());
        assertEquals(30, plan.getEstimatedCarbsBurnedGrams());
        assertEquals(List.of(exercise), plan.getExercises());
    }
}
