package interface_adapter.recommendation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.recommendation.RecommendationInputBoundary;

/**
 * Unit tests for the Recommendation Controller.
 */
public class RecommendationControllerTest {

    @Test
    public void executeDelegatesToInteractorExecute() {
        final FakeRecommendationInteractor interactor = new FakeRecommendationInteractor();
        final RecommendationController controller = new RecommendationController(interactor);

        controller.execute();

        assertTrue(interactor.executeCalled);
    }

    private static final class FakeRecommendationInteractor implements RecommendationInputBoundary {
        private boolean executeCalled;

        @Override
        public void execute() {
            this.executeCalled = true;
        }
    }
}
