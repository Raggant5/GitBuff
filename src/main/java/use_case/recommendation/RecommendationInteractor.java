package use_case.recommendation;

import java.util.ArrayList;
import java.util.List;

import entity.MealRecommendation;
import entity.User;
import entity.WorkoutPlan;

/**
 * Interactor that computes target macros and generates structured AI workout routines and meal suggestions.
 */
public class RecommendationInteractor implements RecommendationInputBoundary {

    private static final double RESTING_KCAL_PER_KG = 22.0;

    private final RecommendationUserDataAccessInterface userDataAccessObject;
    private final RecommendationOutputBoundary recommendationPresenter;
    private final AiWorkoutDataAccessInterface aiWorkoutDataAccessObject;
    private final FoodRecommendationDataAccessInterface foodRecommendationDataAccessObject;

    /**
     * Constructs a RecommendationInteractor instance.
     *
     * @param userDataAccessObject user data access interface
     * @param recommendationOutputBoundary presenter output boundary
     * @param aiWorkoutDataAccessObject AI workout generation data access interface
     * @param foodRecommendationDataAccessObject meal suggestion data access interface
     */
    public RecommendationInteractor(final RecommendationUserDataAccessInterface userDataAccessObject,
                                    final RecommendationOutputBoundary recommendationOutputBoundary,
                                    final AiWorkoutDataAccessInterface aiWorkoutDataAccessObject,
                                    final FoodRecommendationDataAccessInterface foodRecommendationDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
        this.recommendationPresenter = recommendationOutputBoundary;
        this.aiWorkoutDataAccessObject = aiWorkoutDataAccessObject;
        this.foodRecommendationDataAccessObject = foodRecommendationDataAccessObject;
    }

    @Override
    public void execute() {
        final String username = this.userDataAccessObject.getCurrentUsername();
        if (username == null) {
            this.recommendationPresenter.prepareFailView("No user is currently logged in.");
        }
        else {
            final User user = this.userDataAccessObject.get(username);
            if (user == null || user.getWeight() <= 0.0f || user.getHeight() <= 0.0f) {
                final RecommendationOutputData defaultOutput = new RecommendationOutputData(
                        0.0, 0, 0, "General Fitness", "Please update your profile details.",
                        new ArrayList<>(), new ArrayList<>()
                );
                this.recommendationPresenter.prepareSuccessView(defaultOutput);
            }
            else {
                presentRecommendationFor(user);
            }
        }
    }

    private void presentRecommendationFor(final User user) {
        final double restingCalories = RESTING_KCAL_PER_KG * user.getWeight();
        final double maintenanceCalories = restingCalories * user.getActivityLevel().getCalorieMultiplier();
        final int dailyCalorieTarget =
                (int) Math.round(maintenanceCalories + user.getGoal().getDailyCalorieAdjustment());
        final int dailyProteinGrams = (int) Math.round(user.getWeight() * user.getGoal().getProteinGramsPerKg());

        List<WorkoutPlan> plans = new ArrayList<>();
        if (this.aiWorkoutDataAccessObject != null) {
            plans = this.aiWorkoutDataAccessObject.generateWorkoutPlans(user);
        }

        List<MealRecommendation> meals = new ArrayList<>();
        if (this.foodRecommendationDataAccessObject != null) {
            meals = this.foodRecommendationDataAccessObject.generateMealRecommendations(user, dailyCalorieTarget);
        }

        final RecommendationOutputData outputData = new RecommendationOutputData(
                user.getBmi(),
                dailyCalorieTarget,
                dailyProteinGrams,
                user.getGoal().getWorkoutFocus(),
                user.getActivityLevel().getDescription(),
                plans,
                meals);

        this.recommendationPresenter.prepareSuccessView(outputData);
    }
}
