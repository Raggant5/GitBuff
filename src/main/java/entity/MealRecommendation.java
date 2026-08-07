package entity;

/**
 * Represents a single suggested meal, sized to fit part of a user's daily calorie target.
 */
public class MealRecommendation {

    private final String title;
    private final int readyInMinutes;
    private final String sourceUrl;

    /**
     * Constructs a MealRecommendation instance.
     *
     * @param title the meal's name
     * @param readyInMinutes estimated preparation time, in minutes
     * @param sourceUrl a link to the full recipe, or empty if unavailable
     */
    public MealRecommendation(final String title, final int readyInMinutes, final String sourceUrl) {
        this.title = title;
        this.readyInMinutes = readyInMinutes;
        this.sourceUrl = sourceUrl;
    }

    /**
     * Gets the meal's name.
     *
     * @return title string
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the estimated preparation time.
     *
     * @return minutes to prepare
     */
    public int getReadyInMinutes() {
        return this.readyInMinutes;
    }

    /**
     * Gets a link to the full recipe.
     *
     * @return source URL, or empty string if unavailable
     */
    public String getSourceUrl() {
        return this.sourceUrl;
    }
}
