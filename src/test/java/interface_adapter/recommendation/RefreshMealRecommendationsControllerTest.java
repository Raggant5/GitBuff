package interface_adapter.recommendation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.recommendation.RefreshMealRecommendationsInputBoundary;

/**
 * Unit tests for the Refresh Meal Recommendations Controller.
 */
public class RefreshMealRecommendationsControllerTest {

    @Test
    public void executeDelegatesToInteractorExecute() {
        final FakeRefreshMealRecommendationsInteractor interactor = new FakeRefreshMealRecommendationsInteractor();
        final RefreshMealRecommendationsController controller =
                new RefreshMealRecommendationsController(interactor);

        controller.execute();

        assertTrue(interactor.executeCalled);
    }

    private static final class FakeRefreshMealRecommendationsInteractor
            implements RefreshMealRecommendationsInputBoundary {
        private boolean executeCalled;

        @Override
        public void execute() {
            this.executeCalled = true;
        }
    }
}
