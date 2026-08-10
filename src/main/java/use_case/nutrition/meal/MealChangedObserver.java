package use_case.nutrition.meal;

import java.util.function.Consumer;

/**
 * Observer notified whenever a meal is added, edited, or deleted.
 */
public interface MealChangedObserver extends Consumer<MealChangedEvent> {

    /**
     * Called once, after a meal has been added, edited, or deleted.
     *
     * @param event the event, carrying what changed
     */
    void onMealChanged(MealChangedEvent event);

    @Override
    default void accept(MealChangedEvent event) {
        onMealChanged(event);
    }
}
