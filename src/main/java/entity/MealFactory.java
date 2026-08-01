package entity;

import java.time.LocalDate;

/**
 * Factory for creating meals.
 */
public class MealFactory {
    /**
     * Creates a new Meal.
     * @param name the name of the meal
     * @param date the date the meal was eaten
     * @param userId the id of the user who created it
     * @return the new meal
     */
    public Meal create(String userId, LocalDate date, String name) {
        return new Meal(userId, date, name);
    }
}
