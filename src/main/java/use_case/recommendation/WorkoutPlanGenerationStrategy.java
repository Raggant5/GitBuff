package use_case.recommendation;

import java.util.List;

import entity.User;
import entity.WorkoutPlan;

/**
 * Strategy for generating a run of daily workout plans for a user.
 *
 * <p>{@code data_access.AiWorkoutDataAccessObject} previously chose between "call the Gemini
 * API" and "use a deterministic fallback" with an inline try/catch around a private method
 * call. Formalizing the fallback as a {@code WorkoutPlanGenerationStrategy} implementation
 * ({@code data_access.FallbackWorkoutPlanStrategy}) makes that choice an explicit,
 * independently testable strategy swap (Strategy design pattern) rather than logic buried
 * inside the AI-calling class, and keeps that class open to new fallback strategies without
 * modification (Open/Closed Principle).
 */
public interface WorkoutPlanGenerationStrategy {

    /**
     * Generates a run of daily workout plans for a user, one entry per day, with rest days
     * included as workout plans with no exercises.
     *
     * @param user the user to generate plans for, possibly {@code null}
     * @param numberOfDays how many days to generate
     * @return the generated workout plans, one per day
     */
    List<WorkoutPlan> generatePlans(User user, int numberOfDays);
}


