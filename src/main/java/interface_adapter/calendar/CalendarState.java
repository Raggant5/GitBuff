package interface_adapter.calendar;

import java.util.List;

/**
 * The state for the Calendar View Model.
 *
 * <p>Holds {@link CalendarEventDisplayData} rather than the {@code entity.CalendarEvent}
 * entity, so this interface_adapter-layer class does not depend on the entity layer -
 * consistent with the Dependency Rule and with how the nutrition and workout-logging features
 * already expose display data (for example {@code FoodEntryDisplayData},
 * {@code ExercisePerformedDisplayData}).
 */
public class CalendarState {

    private List<CalendarEventDisplayData> calendarEvents = List.of();
    private String errorMessage;

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setCalendarEvents(final List<CalendarEventDisplayData> calendarEvents) {
        this.calendarEvents = List.copyOf(calendarEvents);
    }

    public List<CalendarEventDisplayData> getCalendarEvents() {
        return this.calendarEvents;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}

