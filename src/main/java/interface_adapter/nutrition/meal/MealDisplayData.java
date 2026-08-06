package interface_adapter.nutrition.meal;

import java.time.LocalDate;

import entity.Meal;

/**
 * Display-only view of a Meal, for rendering in panels/lists without the panel depending on the entity
 * directly.
 */
public class MealDisplayData {

    private final Meal meal;

    public MealDisplayData(Meal meal) {
        this.meal = meal;
    }

    public String getName() {
        return meal.getName();
    }

    public LocalDate getDate() {
        return meal.getDate();
    }

    public int getId() {
        return meal.getId();
    }

    /**
     * Gets the underlying entity for the Edit controller.
     * @return the wrapped meal
     */
    public Meal getEntity() {
        return meal;
    }
}
