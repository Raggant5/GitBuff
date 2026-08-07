package use_case.calendar.load_events;

public class LoadCalendarEventsInputData {
    private final String userID;

    public LoadCalendarEventsInputData(String userID) {
        this.userID = userID;
    }

    public String getUserID() {return this.userID;}
}
