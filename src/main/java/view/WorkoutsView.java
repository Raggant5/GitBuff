package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import interface_adapter.recommendation.RecommendationController;
import interface_adapter.workouts.WorkoutsState;
import interface_adapter.workouts.WorkoutsViewModel;

/**
 * The View for displaying personalized workout recommendations and AI plans.
 */
public class WorkoutsView extends JPanel implements PropertyChangeListener {

    private static final int TITLE_FONT_SIZE = 16;
    private static final int TEXT_AREA_ROWS = 10;
    private static final int TEXT_AREA_COLS = 30;

    private final String viewName = "workouts";
    private final WorkoutsViewModel workoutsViewModel;

    private final JLabel focusLabel = new JLabel();
    private final JLabel activityLevelLabel = new JLabel();
    private final JTextArea aiWorkoutArea = new JTextArea(TEXT_AREA_ROWS, TEXT_AREA_COLS);
    private final JLabel messageLabel = new JLabel();
    private final JButton refreshButton = new JButton("Refresh Recommendations");

    private RecommendationController recommendationController;

    /**
     * Constructs a WorkoutsView instance.
     *
     * @param workoutsViewModel the workouts view model
     */
    public WorkoutsView(final WorkoutsViewModel workoutsViewModel) {
        this.workoutsViewModel = workoutsViewModel;
        this.workoutsViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        final JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("Personalized Workout Recommendations");
        title.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE));

        this.refreshButton.addActionListener(evt -> {
            if (this.recommendationController != null) {
                this.recommendationController.execute();
            }
        });

        this.aiWorkoutArea.setEditable(false);
        this.aiWorkoutArea.setLineWrap(true);
        this.aiWorkoutArea.setWrapStyleWord(true);
        final JScrollPane scrollPane = new JScrollPane(this.aiWorkoutArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("AI Suggested Workout Plan"));

        contentPanel.add(title);
        contentPanel.add(this.focusLabel);
        contentPanel.add(this.activityLevelLabel);
        contentPanel.add(scrollPane);
        contentPanel.add(this.messageLabel);
        contentPanel.add(this.refreshButton);

        add(contentPanel, BorderLayout.CENTER);

        displayState(this.workoutsViewModel.getState());
    }

    private void displayState(final WorkoutsState state) {
        this.focusLabel.setText("Recommended Focus: " + state.getWorkoutFocus());
        this.activityLevelLabel.setText("Based on Activity Level: " + state.getActivityLevelDescription());
        this.aiWorkoutArea.setText(state.getAiWorkoutPlan());
        this.messageLabel.setText(state.getMessage());
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        displayState((WorkoutsState) evt.getNewValue());
    }

    public String getViewName() {
        return this.viewName;
    }

    public void setRecommendationController(final RecommendationController recommendationController) {
        this.recommendationController = recommendationController;
    }
}
