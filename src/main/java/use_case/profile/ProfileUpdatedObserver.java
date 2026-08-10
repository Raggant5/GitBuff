package use_case.profile;

import java.util.function.Consumer;

/**
 * Observer notified when a user's profile is saved.
 */
public interface ProfileUpdatedObserver extends Consumer<ProfileUpdatedEvent> {

    /**
     * Called once, after a profile has been saved.
     *
     * @param event the event, carrying who changed
     */
    void onProfileUpdated(ProfileUpdatedEvent event);

    @Override
    default void accept(ProfileUpdatedEvent event) {
        onProfileUpdated(event);
    }
}
