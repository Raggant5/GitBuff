package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.nutrition.NutritionState;
import interface_adapter.nutrition.NutritionViewModel;
import interface_adapter.recommendation.RecommendationController;

public class NutritionView extends JPanel implements PropertyChangeListener {

    private final String viewName = "nutrition";
    private final NutritionViewModel nutritionViewModel;
    private final AddMealView addMealView;
    private final ViewMealsView viewMealsView;

    private final JLabel calorieLabel = new JLabel();
    private final JLabel proteinLabel = new JLabel();
    private final JLabel bmiLabel = new JLabel();
    private final JLabel messageLabel = new JLabel();
    private final JButton refreshButton = new JButton("Refresh Recommendations");

    private RecommendationController recommendationController;

    public NutritionView(NutritionViewModel nutritionViewModel, AddMealView addMealView, ViewMealsView viewMealsView) {
        this.viewMealsView = viewMealsView;
        this.nutritionViewModel = nutritionViewModel;
        this.addMealView = addMealView;
        nutritionViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final JLabel title = new JLabel("Nutrition Recommendations");
        final JButton addMealButton = new JButton("Add Meal");

        addMealButton.addActionListener(evt -> {
            this.addMealView.setVisible(true);
        });

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
        this.add(addMealButton);
        this.add(addMealView);
        this.add(viewMealsView);

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
