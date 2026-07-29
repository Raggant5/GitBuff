package use_case.nutrition.meal;

import java.time.LocalDate;

import entity.Meal;

/**
 * Output Data for the Add Meal Use Case.
 */
public class AddMealOutputData {

    private final Meal meal;

    public AddMealOutputData(Meal meal) {
        this.meal = meal;
    }

    public Meal getMeal() {
        return meal;
    }

    public int getMealId() {
        return meal.getId();
    }

    public String getName() {
        return meal.getName();
    }

    public LocalDate getDate() {
        return meal.getDate();
    }

}
