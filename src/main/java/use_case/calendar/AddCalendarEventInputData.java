package use_case.calendar;

import java.time.LocalDate;

public class AddCalendarEventInputData {
    private final String userId;
    private final String title;
    private final String description;
    private final LocalDate activityDate;

    public AddCalendarEventInputData(
            String userId,
            String title,
            String description,
            LocalDate activityDate) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.activityDate = activityDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }
}
