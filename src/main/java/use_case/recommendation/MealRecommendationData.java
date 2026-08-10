package use_case.recommendation;

import entity.MealRecommendation;

/**
 * Use case boundary DTO mirroring MealRecommendation entity.
 */
public class MealRecommendationData {

    private final String title;
    private final int readyInMinutes;
    private final String sourceUrl;

    public MealRecommendationData(final String title, final int readyInMinutes, final String sourceUrl) {
        this.title = title;
        this.readyInMinutes = readyInMinutes;
        this.sourceUrl = sourceUrl;
    }

    /**
     * Converts a MealRecommendation entity into its boundary DTO form.
     *
     * @param meal the entity to convert
     * @return the equivalent DTO
     */
    public static MealRecommendationData from(final MealRecommendation meal) {
        return new MealRecommendationData(meal.getTitle(), meal.getReadyInMinutes(), meal.getSourceUrl());
    }

    public String getTitle() {
        return this.title;
    }

    public int getReadyInMinutes() {
        return this.readyInMinutes;
    }

    public String getSourceUrl() {
        return this.sourceUrl;
    }
}
