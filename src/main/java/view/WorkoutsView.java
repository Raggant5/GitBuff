package view;

import interface_adapter.recommendation.RecommendationController;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WorkoutsView extends JPanel implements PropertyChangeListener {

    private final String viewName = "workouts";
    private final WorkoutsViewModel workoutsViewModel;

    private final JLabel focusLabel = new JLabel();
    private final JLabel activityLevelLabel = new JLabel();
    private final JLabel messageLabel = new JLabel();
    private final JButton refreshButton = new JButton("Refresh Recommendations");

    private RecommendationController recommendationController;

    public WorkoutsView(WorkoutsViewModel workoutsViewModel) {

        this.workoutsViewModel = workoutsViewModel;
        workoutsViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final JLabel title = new JLabel("Workout Recommendation");

        refreshButton.addActionListener(evt -> {
            if (recommendationController != null) {
                recommendationController.execute();
            }
        });

        this.add(title);
        this.add(focusLabel);
        this.add(activityLevelLabel);
        this.add(messageLabel);
        this.add(refreshButton);

        displayState(workoutsViewModel.getState());
    }

    private void displayState(WorkoutsState state) {
        focusLabel.setText("Recommended focus: " + state.getWorkoutFocus());
        activityLevelLabel.setText("Based on activity level: " + state.getActivityLevelDescription());
        messageLabel.setText(state.getMessage());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        displayState((WorkoutsState) evt.getNewValue());
    }

    public String getViewName() {
        return viewName;
    }

    public void setRecommendationController(RecommendationController recommendationController) {
        this.recommendationController = recommendationController;
    }
}
