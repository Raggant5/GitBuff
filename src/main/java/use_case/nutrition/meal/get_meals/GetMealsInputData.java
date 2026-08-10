package use_case.nutrition.meal.get_meals;

/**
 * Input Data for the Get Meals Use Case.
 */
public class GetMealsInputData {

    private final String userId;

    public GetMealsInputData(final String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }
}
