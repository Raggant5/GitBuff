package use_case.recommendation;

import java.util.List;

import entity.User;
import entity.WorkoutPlan;

/**
 * Strategy for generating a run of daily workout plans for a user.
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


