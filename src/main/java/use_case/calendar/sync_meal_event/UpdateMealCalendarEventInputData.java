package use_case.calendar.sync_meal_event;

import java.time.LocalDate;

import use_case.nutrition.meal.MealChangeType;

/**
 * Input Data for the Update Meal Calendar Event Use Case.
 */
public class UpdateMealCalendarEventInputData {

    private final String userId;
    private final int mealId;
    private final String name;
    private final LocalDate date;
    private final MealChangeType changeType;

    /**
     * Creates input data describing a meal change.
     *
     * @param userId the GitBuff user identifier
     * @param mealId the stored meal identifier
     * @param name the meal name
     * @param date the scheduled meal date
     * @param changeType the kind of change made to the meal
     */
    public UpdateMealCalendarEventInputData(final String userId, final int mealId, final String name,
                                            final LocalDate date, final MealChangeType changeType) {
        this.userId = userId;
        this.mealId = mealId;
        this.name = name;
        this.date = date;
        this.changeType = changeType;
    }

    /**
     * Returns the GitBuff user identifier.
     *
     * @return the user identifier
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Returns the stored meal identifier.
     *
     * @return the meal identifier
     */
    public int getMealId() {
        return this.mealId;
    }

    /**
     * Returns the meal name.
     *
     * @return the meal name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the scheduled meal date.
     *
     * @return the meal date
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Returns the type of meal change.
     *
     * @return the meal change type
     */
    public MealChangeType getChangeType() {
        return this.changeType;
    }
}
