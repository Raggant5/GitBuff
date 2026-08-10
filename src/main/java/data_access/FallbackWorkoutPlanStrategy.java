package data_access;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import entity.Exercise;
import entity.FitnessGoal;
import entity.User;
import entity.WorkoutPlan;
import use_case.recommendation.WorkoutPlanGenerationStrategy;

/**
 * Deterministic, no-network {@link WorkoutPlanGenerationStrategy}: builds a week of workout
 * plans purely from {@link ExerciseKnowledgeBase}, driven by the user's fitness goal and
 * preferred workout days.
 *
 * <p>Extracted from {@code AiWorkoutDataAccessObject.getFallbackPlans}, which
 * {@code AiWorkoutDataAccessObject} now delegates to when the Gemini API is unavailable,
 * unconfigured, or fails - see {@link AiWorkoutDataAccessObject}.
 */
public class FallbackWorkoutPlanStrategy implements WorkoutPlanGenerationStrategy {

    private static final Logger LOGGER = Logger.getLogger(FallbackWorkoutPlanStrategy.class.getName());

    private static final int DEFAULT_WORKOUT_DURATION = 45;

    private static final String[] DEFAULT_WORKOUT_TYPES =
            {"strength", "running", "strength", "biking", "hiit", "running", "strength"};
    private static final String[] DEFAULT_WORKOUT_TITLES = {
            "Full Body Functional Strength", "Cardio Health Run", "Upper Body Conditioning",
            "Steady Cycling Session", "Full Body HIIT Refresh", "Lower Body Stability & Tone",
            "Total Fitness Compound Split"
    };
    private static final String[] DEFAULT_WORKOUT_DESCRIPTIONS = {
            "Balanced full body functional resistance workout for overall fitness.",
            "Steady state cardio run for heart health.",
            "Upper body strength and conditioning.",
            "Biking workout for lower body endurance.",
            "HIIT workout for fitness and conditioning.",
            "Lower body strength and stability workout.",
            "Full body strength workout to maintain muscle and overall fitness."
    };

    private static final int[][] NUTRITION_VALUES = {
            {350, 15, 40}, {320, 12, 45}, {350, 15, 40},
            {320, 12, 45}, {380, 14, 38}, {320, 12, 45}, {330, 18, 35},
    };

    @Override
    public List<WorkoutPlan> generatePlans(final User user, final int numberOfDays) {
        LOGGER.info("Generating fallback plans");
        final List<WorkoutPlan> plans = new ArrayList<>();
        final LocalDate today = LocalDate.now();
        final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEEE, MMM d");

        Set<DayOfWeek> preferredDays = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        if (user != null && user.getPreferredWorkoutDays() != null && !user.getPreferredWorkoutDays().isEmpty()) {
            preferredDays = user.getPreferredWorkoutDays();
        }

        int targetDuration = DEFAULT_WORKOUT_DURATION;
        if (user != null && user.getPreferredWorkoutDurationMinutes() > 0) {
            targetDuration = user.getPreferredWorkoutDurationMinutes();
        }

        final FitnessGoal goal = user != null ? user.getGoal() : FitnessGoal.MAINTAIN_GENERAL_FITNESS;
        final String[] workoutTypes = ExerciseKnowledgeBase.getWorkoutTypesForGoal(goal, DEFAULT_WORKOUT_TYPES);
        final String[] workoutTitles = ExerciseKnowledgeBase.getWorkoutTitlesForGoal(goal, DEFAULT_WORKOUT_TITLES);
        final String[] workoutDescs =
                ExerciseKnowledgeBase.getWorkoutDescriptionsForGoal(goal, DEFAULT_WORKOUT_DESCRIPTIONS);

        int workoutCounter = 0;
        for (int dayOffset = 0; dayOffset < numberOfDays; dayOffset++) {
            final LocalDate date = today.plusDays(dayOffset);
            final String dateLabel = date.format(dateFormat);
            final DayOfWeek dayOfWeek = date.getDayOfWeek();

            if (preferredDays.contains(dayOfWeek)) {
                final int typeIndex = workoutCounter % workoutTypes.length;
                workoutCounter++;
                final String type = workoutTypes[typeIndex];

                final List<Exercise> exercises = ExerciseKnowledgeBase.exercisesForType(type, user);

                final String title = workoutTitles[typeIndex];
                final String description = workoutDescs[typeIndex];
                final int[] nutrition = NUTRITION_VALUES[typeIndex % NUTRITION_VALUES.length];

                plans.add(new WorkoutPlan(dateLabel, title, description,
                        "GENERAL", "GENERAL", "MEDIUM", "Various", "BODYWEIGHT",
                        targetDuration, nutrition[0], nutrition[1], nutrition[2], exercises));
            }
            else {
                plans.add(new WorkoutPlan(dateLabel, "Rest & Recovery",
                        "Rest day. Focus on recovery, hydration, and light stretching.",
                        "REST", "REST", "LOW", "NONE", "BODYWEIGHT",
                        0, 0, 0, 0, new ArrayList<>()));
            }
        }

        LOGGER.log(Level.INFO, "Generated {0} fallback plans", plans.size());
        return plans;
    }
}


