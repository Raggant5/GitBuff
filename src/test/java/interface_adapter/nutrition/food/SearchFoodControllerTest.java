package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.nutrition.food.search_food.SearchFoodInputBoundary;
import use_case.nutrition.food.search_food.SearchFoodInputData;

/**
 * Unit tests for the Search Food Controller.
 */
class SearchFoodControllerTest {

    @Test
    void executeDelegatesToInteractorWithGivenQuery() {
        final FakeSearchFoodInteractor interactor = new FakeSearchFoodInteractor();
        final SearchFoodController controller = new SearchFoodController(interactor);

        controller.execute("banana");

        assertTrue(interactor.executeCalled);
        assertEquals("banana", interactor.receivedInputData.getSearchQuery());
    }

    private static final class FakeSearchFoodInteractor implements SearchFoodInputBoundary {
        private boolean executeCalled;
        private SearchFoodInputData receivedInputData;

        @Override
        public void execute(SearchFoodInputData searchFoodInputData) {
            this.executeCalled = true;
            this.receivedInputData = searchFoodInputData;
        }
    }
}
