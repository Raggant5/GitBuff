package interface_adapter.workouts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RecommendedExerciseDisplayDataTest {

    @Test
    void gettersReturnConstructorValues() {
        final RecommendedExerciseDisplayData exercise = new RecommendedExerciseDisplayData(
                "Squat", 3, 10, 5, "Legs", "Barbell", "Lower with control", "http://video");

        assertEquals("Squat", exercise.getName());
        assertEquals(3, exercise.getSets());
        assertEquals(10, exercise.getReps());
        assertEquals(5, exercise.getDurationMinutes());
        assertEquals("Legs", exercise.getTargetMuscleGroup());
        assertEquals("Barbell", exercise.getEquipmentRequired());
        assertEquals("Lower with control", exercise.getInstructions());
        assertEquals("http://video", exercise.getVideoUrl());
    }
}
