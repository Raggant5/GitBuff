package use_case.recommendation;

import entity.User;

/**
 * The Recommendation Interactor. Computes personalized daily calorie and protein targets,
 * and calls the AI API for an AI workout plan.
 */
public class RecommendationInteractor implements RecommendationInputBoundary {

    private static final double RESTING_KCAL_PER_KG = 22.0;

    private final RecommendationUserDataAccessInterface userDataAccessObject;
    private final RecommendationOutputBoundary recommendationPresenter;
    private final AiWorkoutDataAccessInterface aiWorkoutDataAccessObject;

    /**
     * Constructs a RecommendationInteractor instance.
     *
     * @param userDataAccessObject the user DAO
     * @param recommendationOutputBoundary the recommendation presenter
     * @param aiWorkoutDataAccessObject the AI service adapter
     */
    public RecommendationInteractor(final RecommendationUserDataAccessInterface userDataAccessObject,
                                    final RecommendationOutputBoundary recommendationOutputBoundary,
                                    final AiWorkoutDataAccessInterface aiWorkoutDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
        this.recommendationPresenter = recommendationOutputBoundary;
        this.aiWorkoutDataAccessObject = aiWorkoutDataAccessObject;
    }

    @Override
    public void execute() {
        final String username = this.userDataAccessObject.getCurrentUsername();
        if (username == null) {
            this.recommendationPresenter.prepareFailView("No user is currently logged in.");
            return;
        }

        final User user = this.userDataAccessObject.get(username);
        if (user == null || user.getWeight() <= 0.0f || user.getHeight() <= 0.0f) {
            this.recommendationPresenter.prepareFailView(
                    "Please set your height and weight in your profile before viewing recommendations.");
            return;
        }

        final double restingCalories = RESTING_KCAL_PER_KG * user.getWeight();
        final double maintenanceCalories = restingCalories * user.getActivityLevel().getCalorieMultiplier();
        final int dailyCalorieTarget =
                (int) Math.round(maintenanceCalories + user.getGoal().getDailyCalorieAdjustment());
        final int dailyProteinGrams = (int) Math.round(user.getWeight() * user.getGoal().getProteinGramsPerKg());

        String aiWorkoutPlan = "No AI plan generated.";
        if (this.aiWorkoutDataAccessObject != null) {
            aiWorkoutPlan = this.aiWorkoutDataAccessObject.generateWorkoutPlan(user);
        }

        final RecommendationOutputData outputData = new RecommendationOutputData(
                user.getBMI(),
                dailyCalorieTarget,
                dailyProteinGrams,
                user.getGoal().getWorkoutFocus(),
                user.getActivityLevel().getDescription(),
                aiWorkoutPlan);

        this.recommendationPresenter.prepareSuccessView(outputData);
    }
}
