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

    public UpdateMealCalendarEventInputData(final String userId, final int mealId, final String name,
                                            final LocalDate date, final MealChangeType changeType) {
        this.userId = userId;
        this.mealId = mealId;
        this.name = name;
        this.date = date;
        this.changeType = changeType;
    }

    public String getUserId() {
        return this.userId;
    }

    public int getMealId() {
        return this.mealId;
    }

    public String getName() {
        return this.name;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public MealChangeType getChangeType() {
        return this.changeType;
    }
}
