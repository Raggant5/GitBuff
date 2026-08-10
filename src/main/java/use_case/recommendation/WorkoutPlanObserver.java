package use_case.recommendation;

import java.util.function.Consumer;

/**
 * Observer notified when a fresh workout plan has been generated for a user.
 */
public interface WorkoutPlanObserver extends Consumer<WorkoutPlanGeneratedEvent> {

    /**
     * Called once, after a workout plan has been generated.
     *
     * @param event the event, carrying the generated plans
     */
    void onWorkoutPlanGenerated(WorkoutPlanGeneratedEvent event);

    @Override
    default void accept(WorkoutPlanGeneratedEvent event) {
        onWorkoutPlanGenerated(event);
    }
}
