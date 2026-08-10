package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ExerciseTest {

    @Test
    void gettersReturnConstructorValues() {
        final Exercise exercise = new Exercise(
                "Squat", 4, 8, 6, "Legs", "Barbell", "Bend knees, keep back straight",
                "http://video", "Strength", "Lower Body", "High", "Barbell");

        assertNull(exercise.getId());
        assertEquals("Squat", exercise.getName());
        assertEquals(4, exercise.getSets());
        assertEquals(8, exercise.getReps());
        assertEquals(6, exercise.getDurationMinutes());
        assertEquals("Legs", exercise.getTargetMuscleGroup());
        assertEquals("Barbell", exercise.getEquipmentRequired());
        assertEquals("Bend knees, keep back straight", exercise.getInstructions());
        assertEquals("http://video", exercise.getVideoUrl());
        assertEquals("Strength", exercise.getCategory());
        assertEquals("Lower Body", exercise.getSubCategory());
        assertEquals("High", exercise.getIntensityLevel());
        assertEquals("Barbell", exercise.getEquipmentType());
    }
}
