package use_case.nutrition.food.search_food;

public class SearchFoodInputData {
    private final String searchQuery;

    public SearchFoodInputData(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
