package interface_adapter.nutrition;

/**
 * Meal recommendation DTO, for use in State/View. Never holds an Entity.
 */
public class MealRecommendationDisplayData {

    private final String title;
    private final int readyInMinutes;
    private final String sourceUrl;

    public MealRecommendationDisplayData(final String title, final int readyInMinutes, final String sourceUrl) {
        this.title = title;
        this.readyInMinutes = readyInMinutes;
        this.sourceUrl = sourceUrl;
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
