package use_case.recommendation;

import entity.User;

/**
 * The Recommendation Interactor. Computes a personalized daily calorie target,
 * protein target, and workout focus for the current user, based on their
 * height, weight, activity level, and fitness goal.
 */
public class RecommendationInteractor implements RecommendationInputBoundary {

    private static final double RESTING_KCAL_PER_KG = 22.0;

    private final RecommendationUserDataAccessInterface userDataAccessObject;
    private final RecommendationOutputBoundary recommendationPresenter;

    public RecommendationInteractor(RecommendationUserDataAccessInterface userDataAccessObject,
                                     RecommendationOutputBoundary recommendationOutputBoundary) {
        this.userDataAccessObject = userDataAccessObject;
        this.recommendationPresenter = recommendationOutputBoundary;
    }

    @Override
    public void execute() {
        final String username = userDataAccessObject.getCurrentUsername();
        if (username == null) {
            recommendationPresenter.prepareFailView("No user is currently logged in.");
            return;
        }

        final User user = userDataAccessObject.get(username);
        if (user == null || user.getWeight() <= 0.0f || user.getHeight() <= 0.0f) {
            recommendationPresenter.prepareFailView(
                    "Please set your height and weight in your profile before viewing recommendations.");
            return;
        }

        final double restingCalories = RESTING_KCAL_PER_KG * user.getWeight();
        final double maintenanceCalories = restingCalories * user.getActivityLevel().getCalorieMultiplier();
        final int dailyCalorieTarget =
                (int) Math.round(maintenanceCalories + user.getGoal().getDailyCalorieAdjustment());
        final int dailyProteinGrams = (int) Math.round(user.getWeight() * user.getGoal().getProteinGramsPerKg());

        final RecommendationOutputData outputData = new RecommendationOutputData(
                user.getBMI(),
                dailyCalorieTarget,
                dailyProteinGrams,
                user.getGoal().getWorkoutFocus(),
                user.getActivityLevel().getDescription());

        recommendationPresenter.prepareSuccessView(outputData);
    }
}
