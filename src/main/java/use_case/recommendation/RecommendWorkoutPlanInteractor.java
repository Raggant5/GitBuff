package use_case.recommendation;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.ActivityLevel;
import entity.FitnessGoal;
import entity.User;
import entity.WorkoutPlan;
import use_case.EventPublisher;

/**
 * Interactor that generates a structured AI workout plan for the currently logged-in user.
 * Never touches meal recommendations - see RefreshMealRecommendationsInteractor for that
 * pipeline.
 */
public class RecommendWorkoutPlanInteractor implements RecommendWorkoutPlanInputBoundary {

    private static final int DEFAULT_DURATION_MINUTES = 45;
    private static final int WEEK_DAYS = 7;

    private final RecommendationUserDataAccessInterface userDataAccessObject;
    private final RecommendWorkoutPlanOutputBoundary presenter;
    private final AiWorkoutDataAccessInterface aiWorkoutDataAccessObject;
    private final EventPublisher<WorkoutPlanGeneratedEvent> workoutPlanEventPublisher;

    public RecommendWorkoutPlanInteractor(final RecommendationUserDataAccessInterface userDataAccessObject,
                                          final RecommendWorkoutPlanOutputBoundary presenter,
                                          final AiWorkoutDataAccessInterface aiWorkoutDataAccessObject,
                                          final EventPublisher<WorkoutPlanGeneratedEvent> workoutPlanEventPublisher) {
        this.userDataAccessObject = userDataAccessObject;
        this.presenter = presenter;
        this.aiWorkoutDataAccessObject = aiWorkoutDataAccessObject;
        this.workoutPlanEventPublisher = workoutPlanEventPublisher;
    }

    @Override
    public void execute() {
        final String username = this.userDataAccessObject.getCurrentUsername();
        if (username == null) {
            this.presenter.prepareFailView("No user is currently logged in.");
        }
        else {
            final User user = this.userDataAccessObject.get(username);
            if (user == null || user.getWeight() <= 0.0f || user.getHeight() <= 0.0f) {
                this.presenter.prepareSuccessView(new RecommendWorkoutPlanOutputData(
                        "General Fitness", "Please update your profile details.", new ArrayList<>()));
                this.workoutPlanEventPublisher.publish(
                        new WorkoutPlanGeneratedEvent(username, Collections.emptyList()));
            }
            else {
                ensureProfileDefaults(user);

                final List<WorkoutPlan> generatedPlans = this.aiWorkoutDataAccessObject.generateWorkoutPlans(
                        user, WEEK_DAYS);
                final List<WorkoutPlanData> plans = new ArrayList<>();
                for (final WorkoutPlan plan : generatedPlans) {
                    plans.add(WorkoutPlanData.from(plan));
                }

                final String focusSummary;
                if (user.getGoal() != null) {
                    focusSummary = user.getGoal().getWorkoutFocus();
                }
                else {
                    focusSummary = FitnessGoal.MAINTAIN_GENERAL_FITNESS.getWorkoutFocus();
                }
                final String activitySummary;
                if (user.getActivityLevel() != null) {
                    activitySummary = user.getActivityLevel().getDescription();
                }
                else {
                    activitySummary = ActivityLevel.MODERATELY_ACTIVE.getDescription();
                }

                this.presenter.prepareSuccessView(
                        new RecommendWorkoutPlanOutputData(focusSummary, activitySummary, plans));
                this.workoutPlanEventPublisher.publish(new WorkoutPlanGeneratedEvent(username, plans));
            }
        }
    }

    private void ensureProfileDefaults(final User user) {
        if (user.getGoal() == null) {
            user.setGoal(FitnessGoal.MAINTAIN_GENERAL_FITNESS);
        }
        if (user.getActivityLevel() == null) {
            user.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        }
        if (user.getPreferredWorkoutDays() == null || user.getPreferredWorkoutDays().isEmpty()) {
            final Set<DayOfWeek> defaultDays = new HashSet<>();
            defaultDays.add(DayOfWeek.MONDAY);
            defaultDays.add(DayOfWeek.WEDNESDAY);
            defaultDays.add(DayOfWeek.FRIDAY);
            user.setPreferredWorkoutDays(defaultDays);
        }
        if (user.getPreferredWorkoutDurationMinutes() <= 0) {
            user.setPreferredWorkoutDurationMinutes(DEFAULT_DURATION_MINUTES);
        }
    }
}
