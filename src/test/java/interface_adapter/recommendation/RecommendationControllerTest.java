package interface_adapter.recommendation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.recommendation.RecommendationInputBoundary;

/**
 * Unit tests for the Recommendation Controller.
 */
public class RecommendationControllerTest {

    @Test
    public void executeDelegatesOnlyToInteractorExecute() {
        final FakeRecommendationInteractor interactor = new FakeRecommendationInteractor();
        final RecommendationController controller = new RecommendationController(interactor);

        controller.execute();

        assertTrue(interactor.executeCalled);
        assertFalse(interactor.executeMealRecommendationsOnlyCalled);
    }

    @Test
    public void executeMealRecommendationsOnlyDelegatesOnlyToInteractorMatchingMethod() {
        final FakeRecommendationInteractor interactor = new FakeRecommendationInteractor();
        final RecommendationController controller = new RecommendationController(interactor);

        controller.executeMealRecommendationsOnly();

        assertTrue(interactor.executeMealRecommendationsOnlyCalled);
        assertFalse(interactor.executeCalled);
    }

    private static final class FakeRecommendationInteractor implements RecommendationInputBoundary {
        private boolean executeCalled;
        private boolean executeMealRecommendationsOnlyCalled;

        @Override
        public void execute() {
            this.executeCalled = true;
        }

        @Override
        public void executeMealRecommendationsOnly() {
            this.executeMealRecommendationsOnlyCalled = true;
        }
    }
}
