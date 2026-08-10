package interface_adapter.recommendation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import use_case.recommendation.RecommendWorkoutPlanInputBoundary;

/**
 * Unit tests for the Recommend Workout Plan Controller.
 */
public class RecommendationControllerTest {

    @Test
    public void executeDelegatesToInteractorExecute() {
        final FakeRecommendWorkoutPlanInteractor interactor = new FakeRecommendWorkoutPlanInteractor();
        final RecommendWorkoutPlanController controller = new RecommendWorkoutPlanController(interactor);

        controller.execute();

        assertTrue(interactor.executeCalled);
    }

    private static final class FakeRecommendWorkoutPlanInteractor implements RecommendWorkoutPlanInputBoundary {
        private boolean executeCalled;

        @Override
        public void execute() {
            this.executeCalled = true;
        }
    }
}
