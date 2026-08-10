package use_case.nutrition.food.search_food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import data_access.MockSearchFoodDataAccessObject;
import entity.FoodSearchResult;
import org.junit.jupiter.api.Test;

public class SearchFoodInteractorTest {

    @Test
    public void executeWithMockApiDataAccessReturnsCannedResults() {
        final SearchFoodDataAccessInterface dataAccess = new MockSearchFoodDataAccessObject();

        final SearchFoodOutputBoundary presenter = new SearchFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(SearchFoodOutputData outputData) {
                assertEquals(5, outputData.getFoodResults().size());
                assertEquals("Apple", outputData.getFoodResults().get(0).getFoodName());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new SearchFoodInteractor(dataAccess, presenter).execute(new SearchFoodInputData("apple"));
    }

    @Test
    public void executeWithResultsReturnsThemToPresenter() {
        final SearchFoodDataAccessInterface dataAccess = query -> {
            assertEquals("chicken", query);
            return List.of(new FoodSearchResult("Chicken Breast", "1 serving", 150.0, 200.0, 20.0, 0.0, 5.0));
        };

        final SearchFoodOutputBoundary presenter = new SearchFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(SearchFoodOutputData outputData) {
                assertEquals(1, outputData.getFoodResults().size());
                assertEquals("Chicken Breast", outputData.getFoodResults().get(0).getFoodName());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new SearchFoodInteractor(dataAccess, presenter).execute(new SearchFoodInputData("chicken"));
    }

    @Test
    public void executeWithNoMatchesReturnsEmptyList() {
        final SearchFoodDataAccessInterface dataAccess = query -> List.of();
        final boolean[] succeeded = {false};

        final SearchFoodOutputBoundary presenter = new SearchFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(SearchFoodOutputData outputData) {
                succeeded[0] = true;
                assertTrue(outputData.getFoodResults().isEmpty());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };

        new SearchFoodInteractor(dataAccess, presenter).execute(new SearchFoodInputData("xyzzy"));
        assertTrue(succeeded[0]);
    }

    @Test
    public void executeWhenDataAccessThrowsIOExceptionPreparesFailView() {
        final SearchFoodDataAccessInterface dataAccess = query -> {
            throw new IOException("API unavailable");
        };
        final boolean[] failed = {false};

        final SearchFoodOutputBoundary presenter = new SearchFoodOutputBoundary() {
            @Override
            public void prepareSuccessView(SearchFoodOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertEquals("API unavailable", errorMessage);
            }
        };

        new SearchFoodInteractor(dataAccess, presenter).execute(new SearchFoodInputData("chicken"));
        assertTrue(failed[0]);
    }
}
