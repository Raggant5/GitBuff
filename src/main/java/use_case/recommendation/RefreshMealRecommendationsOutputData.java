package use_case.recommendation;

import java.util.List;

import entity.MealRecommendation;

/**
 * Output Data for the Refresh Meal Recommendations Use Case.
 */
public class RefreshMealRecommendationsOutputData {

    private final double bmi;
    private final int dailyCalorieTarget;
    private final int dailyProteinGrams;
    private final List<MealRecommendation> mealRecommendations;

    /**
     * Constructs a RefreshMealRecommendationsOutputData instance.
     *
     * @param bmi the user's current BMI
     * @param dailyCalorieTarget the user's daily calorie target
     * @param dailyProteinGrams the user's daily protein target in grams
     * @param mealRecommendations the refreshed list of suggested meals
     */
    public RefreshMealRecommendationsOutputData(final double bmi, final int dailyCalorieTarget,
                                                final int dailyProteinGrams,
                                                final List<MealRecommendation> mealRecommendations) {
        this.bmi = bmi;
        this.dailyCalorieTarget = dailyCalorieTarget;
        this.dailyProteinGrams = dailyProteinGrams;
        this.mealRecommendations = mealRecommendations;
    }

    public double getBmi() {
        return this.bmi;
    }

    public int getDailyCalorieTarget() {
        return this.dailyCalorieTarget;
    }

    public int getDailyProteinGrams() {
        return this.dailyProteinGrams;
    }

    public List<MealRecommendation> getMealRecommendations() {
        return this.mealRecommendations;
    }
}
