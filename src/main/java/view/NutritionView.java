package view;

import interface_adapter.nutrition.NutritionState;
import interface_adapter.nutrition.NutritionViewModel;
import interface_adapter.recommendation.RecommendationController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NutritionView extends JPanel implements PropertyChangeListener {

    private final String viewName = "nutrition";
    private final NutritionViewModel nutritionViewModel;

    private final JLabel calorieLabel = new JLabel();
    private final JLabel proteinLabel = new JLabel();
    private final JLabel bmiLabel = new JLabel();
    private final JLabel messageLabel = new JLabel();
    private final JButton refreshButton = new JButton("Refresh Recommendations");

    private RecommendationController recommendationController;

    public NutritionView(NutritionViewModel nutritionViewModel) {

        this.nutritionViewModel = nutritionViewModel;
        nutritionViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final JLabel title = new JLabel("Nutrition Recommendations");

        refreshButton.addActionListener(evt -> {
            if (recommendationController != null) {
                recommendationController.execute();
            }
        });

        this.add(title);
        this.add(calorieLabel);
        this.add(proteinLabel);
        this.add(bmiLabel);
        this.add(messageLabel);
        this.add(refreshButton);

        displayState(nutritionViewModel.getState());
    }

    private void displayState(NutritionState state) {
        calorieLabel.setText("Daily calorie target: " + state.getDailyCalorieTarget() + " kcal");
        proteinLabel.setText("Daily protein target: " + state.getDailyProteinGrams() + " g");
        bmiLabel.setText(String.format("BMI: %.1f", state.getBmi()));
        messageLabel.setText(state.getMessage());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        displayState((NutritionState) evt.getNewValue());
    }

    public String getViewName() {
        return viewName;
    }

    public void setRecommendationController(RecommendationController recommendationController) {
        this.recommendationController = recommendationController;
    }
}
