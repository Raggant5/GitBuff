package use_case.calendar.sync_meal_event;

/**
 * Output boundary for the Update Meal Calendar Event Use Case.
 */
public interface UpdateMealCalendarEventOutputBoundary {

    /**
     * Prepares the success view for the Update Meal Calendar Event Use Case.
     *
     * @param outputData the user's calendar events after the update
     */
    void prepareSuccessView(UpdateMealCalendarEventOutputData outputData);

    /**
     * Prepares the failure view for the Update Meal Calendar Event Use Case.
     *
     * @param errorMessage explanation of failure cause
     */
    void prepareFailureView(String errorMessage);
}
