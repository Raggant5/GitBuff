package use_case.recommendation;

import entity.User;

/**
 * Interface for generating personalized workout plans via external AI API.
 */
public interface AiWorkoutDataAccessInterface {

    /**
     * Generates a workout recommendation for the given user using AI.
     *
     * @param user the user requesting recommendations
     * @return a structured workout recommendation string
     */
    String generateWorkoutPlan(final User user);
}
