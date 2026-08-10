package use_case.nutrition.meal.get_meals;

/**
 * Input boundary for fetching a user's saved meals on demand (e.g. when the Nutrition tab is
 * opened), rather than Login eagerly fetching them for every user at every login.
 */
public interface GetMealsInputBoundary {

    /**
     * Executes the get-meals use case.
     *
     * @param inputData the user to fetch meals for
     */
    void execute(GetMealsInputData inputData);
}
