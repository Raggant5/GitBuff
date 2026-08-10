package use_case.profile;

/**
 * Event published whenever a user's profile is saved - weight/goal/activity-level changes
 * affect both the workout plan and meal recommendations.
 */
public class ProfileUpdatedEvent {

    private final String username;

    public ProfileUpdatedEvent(final String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
