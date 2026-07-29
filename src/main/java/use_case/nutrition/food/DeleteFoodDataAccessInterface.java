package use_case.nutrition.food;

/**
 * DAO for deleting food entries.
 */
public interface DeleteFoodDataAccessInterface {

    /**
     * Deletes a food entry.
     * @param foodEntryId the food entry id
     */
    void deleteFoodEntry(int foodEntryId);
}
