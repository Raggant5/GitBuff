package use_case.calendar.sync_meal_event;

/**
 * Input boundary for keeping a single meal's calendar event in sync right after that one meal
 * is added, edited, or deleted.
 */
public interface UpdateMealCalendarEventInputBoundary {

    /**
     * Executes the update-meal-calendar-event use case.
     *
     * @param inputData what changed about the meal
     */
    void execute(UpdateMealCalendarEventInputData inputData);
}
