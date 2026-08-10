package use_case.nutrition.meal;

import use_case.calendar.sync_meal_event.UpdateMealCalendarEventInputBoundary;
import use_case.calendar.sync_meal_event.UpdateMealCalendarEventInputData;

/**
 * Keeps a single meal's calendar event in sync right after that meal is added, edited, or
 * deleted.
 */
public class MealCalendarSyncObserver implements MealChangedObserver {

    private final UpdateMealCalendarEventInputBoundary updateMealCalendarEventInteractor;

    public MealCalendarSyncObserver(final UpdateMealCalendarEventInputBoundary updateMealCalendarEventInteractor) {
        this.updateMealCalendarEventInteractor = updateMealCalendarEventInteractor;
    }

    @Override
    public void onMealChanged(final MealChangedEvent event) {
        this.updateMealCalendarEventInteractor.execute(new UpdateMealCalendarEventInputData(
                event.getUserId(), event.getMealId(), event.getName(), event.getDate(), event.getChangeType()));
    }
}
