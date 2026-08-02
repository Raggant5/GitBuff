package use_case.calendar.remove_event;

public class RemoveCalendarEventInputData {
    private final String userId;
    private final String eventID;

    public RemoveCalendarEventInputData(String userId, String eventID) {
        this.userId = userId;
        this.eventID = eventID;
    }

    public String getUserId() {return this.userId;}

    public String getEventID() {return this.eventID;}
}
