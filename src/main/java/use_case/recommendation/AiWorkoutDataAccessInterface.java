package use_case.recommendation;

import java.util.List;

import entity.User;
import entity.WorkoutPlan;

/**
 * Interface for generating structured AI workout plans via external API.
 */
public interface AiWorkoutDataAccessInterface {

    /**
     * Generates a list of structured workout plans for the given user.
     * Default implementation generates 14 days (2 weeks).
     *
     * @param user the user requesting recommendations
     * @return a list of structured workout plans
     */
    List<WorkoutPlan> generateWorkoutPlans(final User user);

    /**
     * Generates a list of structured workout plans for the given user
     * for a specified number of days.
     *
     * @param user the user requesting recommendations
     * @param numberOfDays number of days to generate (7 or 14)
     * @return a list of structured workout plans
     */
    List<WorkoutPlan> generateWorkoutPlans(final User user, final int numberOfDays);
}