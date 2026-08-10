package interface_adapter.recommendation;

import javax.swing.SwingWorker;

import interface_adapter.session.UserLoggedInEvent;
import interface_adapter.session.UserSessionObserver;

/**
 * Triggers a background refresh of AI workout/meal recommendations after login.
 *
 * <p>Extracted from {@code interface_adapter.login.LoginPresenter}, which previously
 * constructed this same {@link SwingWorker} inline.
 */
public class RecommendationRefreshObserver implements UserSessionObserver {

    private final RecommendationController recommendationController;

    /**
     * Constructs a RecommendationRefreshObserver instance.
     *
     * @param recommendationController controller to trigger recommendations
     */
    public RecommendationRefreshObserver(final RecommendationController recommendationController) {
        this.recommendationController = recommendationController;
    }

    @Override
    public void onUserLoggedIn(final UserLoggedInEvent event) {
        final SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                recommendationController.execute();
                return null;
            }
        };
        worker.execute();
    }
}

