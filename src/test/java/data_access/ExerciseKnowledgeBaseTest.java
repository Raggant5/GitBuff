package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import entity.CommonUser;
import entity.Equipment;
import entity.Exercise;
import entity.FitnessGoal;
import entity.User;

class ExerciseKnowledgeBaseTest {

    private static final String[] DEFAULT_TYPES = {"strength"};
    private static final String[] DEFAULT_TITLES = {"Full Body Strength"};
    private static final String[] DEFAULT_DESCS = {"A general strength workout."};

    @Test
    void getWorkoutTypesForGoalReturnsMappedValuesForKnownGoal() {
        final String[] types = ExerciseKnowledgeBase.getWorkoutTypesForGoal(
                FitnessGoal.LOSE_WEIGHT, DEFAULT_TYPES);

        assertEquals("hiit", types[0]);
    }

    @Test
    void getWorkoutTypesForGoalReturnsDefaultForUnmappedGoal() {
        final String[] types = ExerciseKnowledgeBase.getWorkoutTypesForGoal(null, DEFAULT_TYPES);

        assertEquals(DEFAULT_TYPES, types);
    }

    @Test
    void getWorkoutTitlesForGoalReturnsMappedValuesForKnownGoal() {
        final String[] titles = ExerciseKnowledgeBase.getWorkoutTitlesForGoal(
                FitnessGoal.MUSCLE_AND_STRENGTH_GAIN, DEFAULT_TITLES);

        assertEquals("Upper Body Hypertrophy", titles[0]);
    }

    @Test
    void getWorkoutTitlesForGoalReturnsDefaultForUnmappedGoal() {
        final String[] titles = ExerciseKnowledgeBase.getWorkoutTitlesForGoal(null, DEFAULT_TITLES);

        assertEquals(DEFAULT_TITLES, titles);
    }

    @Test
    void getWorkoutDescriptionsForGoalReturnsMappedValuesForKnownGoal() {
        final String[] descs = ExerciseKnowledgeBase.getWorkoutDescriptionsForGoal(
                FitnessGoal.INCREASE_ENDURANCE, DEFAULT_DESCS);

        assertTrue(descs[0].contains("aerobic capacity"));
    }

    @Test
    void getWorkoutDescriptionsForGoalReturnsDefaultForUnmappedGoal() {
        final String[] descs = ExerciseKnowledgeBase.getWorkoutDescriptionsForGoal(null, DEFAULT_DESCS);

        assertEquals(DEFAULT_DESCS, descs);
    }

    @Test
    void exercisesForTypeReturnsMappedExercisesForKnownType() {
        final User user = new CommonUser("aahir", "password");

        final List<Exercise> exercises = ExerciseKnowledgeBase.exercisesForType("running", user);

        assertEquals(3, exercises.size());
        assertEquals("Running", exercises.get(0).getName());
    }

    @Test
    void exercisesForTypeFallsBackToDefaultTrioForUnknownType() {
        final User user = new CommonUser("aahir", "password");

        final List<Exercise> exercises = ExerciseKnowledgeBase.exercisesForType("unknown-type", user);

        assertEquals(3, exercises.size());
        assertEquals("Push-Ups", exercises.get(0).getName());
        assertEquals("Squats", exercises.get(1).getName());
        assertEquals("Planks", exercises.get(2).getName());
    }

    @Test
    void exercisesForTypeSubstitutesBodyweightWhenUserLacksEquipment() {
        final User user = new CommonUser("aahir", "password");
        user.setEquipment(Set.of());

        final List<Exercise> exercises = ExerciseKnowledgeBase.exercisesForType("upper", user);

        final Exercise dumbbellExercise = exercises.get(1);
        assertEquals("BODYWEIGHT", dumbbellExercise.getEquipmentType());
    }

    @Test
    void exercisesForTypeKeepsEquipmentWhenUserHasIt() {
        final User user = new CommonUser("aahir", "password");
        user.setEquipment(Set.of(Equipment.DUMBBELLS));

        final List<Exercise> exercises = ExerciseKnowledgeBase.exercisesForType("upper", user);

        final Exercise dumbbellExercise = exercises.get(1);
        assertEquals("DUMBBELLS", dumbbellExercise.getEquipmentType());
    }

    @Test
    void createExerciseBuildsGenericExerciseFromName() {
        final Exercise exercise = ExerciseKnowledgeBase.createExercise("Random Movement");

        assertEquals("Random Movement", exercise.getName());
        assertEquals("GENERAL", exercise.getCategory());
        assertTrue(exercise.getVideoUrl().contains("Random+Movement"));
    }

    @Test
    void userHasEquipmentTrueForBodyweightRequirement() {
        assertTrue(ExerciseKnowledgeBase.userHasEquipment(null, "BODYWEIGHT"));
        assertTrue(ExerciseKnowledgeBase.userHasEquipment(null, "BODYWEIGHT_ONLY"));
        assertTrue(ExerciseKnowledgeBase.userHasEquipment(null, null));
    }

    @Test
    void userHasEquipmentFalseWhenUserIsNullOrHasNoEquipment() {
        assertFalse(ExerciseKnowledgeBase.userHasEquipment(null, "DUMBBELLS"));

        final User user = new CommonUser("aahir", "password");
        user.setEquipment(Set.of());
        assertFalse(ExerciseKnowledgeBase.userHasEquipment(user, "DUMBBELLS"));
    }

    @Test
    void userHasEquipmentTrueWhenUserOwnsMatchingEquipment() {
        final User user = new CommonUser("aahir", "password");
        user.setEquipment(Set.of(Equipment.DUMBBELLS));

        assertTrue(ExerciseKnowledgeBase.userHasEquipment(user, "DUMBBELLS"));
    }

    @Test
    void userHasEquipmentFalseWhenUserOwnsNonMatchingEquipment() {
        final User user = new CommonUser("aahir", "password");
        user.setEquipment(Set.of(Equipment.KETTLEBELL));

        assertFalse(ExerciseKnowledgeBase.userHasEquipment(user, "DUMBBELLS"));
    }

    @Test
    void determineWorkoutTypeMapsAllKnownKeywords() {
        assertEquals("yoga", ExerciseKnowledgeBase.determineWorkoutType("morning yoga flow"));
        assertEquals("yoga", ExerciseKnowledgeBase.determineWorkoutType("deep stretch"));
        assertEquals("hiit", ExerciseKnowledgeBase.determineWorkoutType("hiit blast"));
        assertEquals("hiit", ExerciseKnowledgeBase.determineWorkoutType("interval training"));
        assertEquals("running", ExerciseKnowledgeBase.determineWorkoutType("cardio run"));
        assertEquals("biking", ExerciseKnowledgeBase.determineWorkoutType("cycle session"));
        assertEquals("core", ExerciseKnowledgeBase.determineWorkoutType("core & abs"));
        assertEquals("upper", ExerciseKnowledgeBase.determineWorkoutType("upper body push"));
        assertEquals("lower", ExerciseKnowledgeBase.determineWorkoutType("leg day squats"));
        assertEquals("strength", ExerciseKnowledgeBase.determineWorkoutType("full body compound"));
    }

    @Test
    void getInstructionForExerciseCoversAllKnownKeywords() {
        assertTrue(ExerciseKnowledgeBase.getInstructionForExercise("Push-Ups").contains("chest"));
        assertTrue(ExerciseKnowledgeBase.getInstructionForExercise("Squats").contains("hips"));
        assertTrue(ExerciseKnowledgeBase.getInstructionForExercise("Plank").contains("forearms"));
        assertTrue(ExerciseKnowledgeBase.getInstructionForExercise("Lunges").contains("90 degrees"));
        assertTrue(ExerciseKnowledgeBase.getInstructionForExercise("Running").contains("Jog"));
        assertTrue(ExerciseKnowledgeBase.getInstructionForExercise("Bike Ride").contains("Pedal"));
        assertTrue(ExerciseKnowledgeBase.getInstructionForExercise("Burpees").contains("squat"));
        assertTrue(ExerciseKnowledgeBase.getInstructionForExercise("Yoga Flow").contains("yoga pose"));
        assertTrue(ExerciseKnowledgeBase.getInstructionForExercise("Farmer Carry").contains("Farmer Carry"));
    }

    @Test
    void createExerciseDerivesCategoryFromNameKeywords() {
        assertEquals("CARDIO", ExerciseKnowledgeBase.createExercise("Sprint").getCategory());
        assertEquals("STRENGTH", ExerciseKnowledgeBase.createExercise("Deadlift").getCategory());
        assertEquals("HIIT", ExerciseKnowledgeBase.createExercise("Burpees").getCategory());
        assertEquals("CORE", ExerciseKnowledgeBase.createExercise("Plank Hold").getCategory());
        assertEquals("FLEXIBILITY", ExerciseKnowledgeBase.createExercise("Warrior Pose").getCategory());
        assertEquals("GENERAL", ExerciseKnowledgeBase.createExercise("Farmer Carry").getCategory());
    }

    @Test
    void createExerciseDerivesSubCategoryFromNameKeywords() {
        assertEquals("RUNNING", ExerciseKnowledgeBase.createExercise("Jog").getSubCategory());
        assertEquals("BIKING", ExerciseKnowledgeBase.createExercise("Pedal Session").getSubCategory());
        assertEquals("UPPER_BODY", ExerciseKnowledgeBase.createExercise("Press").getSubCategory());
        assertEquals("LOWER_BODY", ExerciseKnowledgeBase.createExercise("Calf Raise").getSubCategory());
        assertEquals("FULL_BODY", ExerciseKnowledgeBase.createExercise("Mountain Climber").getSubCategory());
        assertEquals("CORE", ExerciseKnowledgeBase.createExercise("Leg Raise").getSubCategory());
        assertEquals("YOGA", ExerciseKnowledgeBase.createExercise("Tree Pose").getSubCategory());
        assertEquals("GENERAL", ExerciseKnowledgeBase.createExercise("Farmer Carry").getSubCategory());
    }

    @Test
    void createExerciseDerivesIntensityFromNameKeywords() {
        assertEquals("HIGH", ExerciseKnowledgeBase.createExercise("Sprint").getIntensityLevel());
        assertEquals("LOW", ExerciseKnowledgeBase.createExercise("Yoga Flow").getIntensityLevel());
        assertEquals("MEDIUM", ExerciseKnowledgeBase.createExercise("Farmer Carry").getIntensityLevel());
    }

    @Test
    void createExerciseDerivesEquipmentFromNameKeywords() {
        assertEquals("STATIONARY_BIKE", ExerciseKnowledgeBase.createExercise("Bike Ride").getEquipmentType());
        assertEquals("PULL_UP_BAR", ExerciseKnowledgeBase.createExercise("Chin Up").getEquipmentType());
        assertEquals("DUMBBELLS", ExerciseKnowledgeBase.createExercise("Dumbbell Press").getEquipmentType());
        assertEquals("BARBELL", ExerciseKnowledgeBase.createExercise("Deadlift").getEquipmentType());
        assertEquals("BODYWEIGHT", ExerciseKnowledgeBase.createExercise("Farmer Carry").getEquipmentType());
    }
}
