package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import entity.MealRecommendation;
import interface_adapter.MainViewManagerModel;
import interface_adapter.nutrition.NutritionState;
import interface_adapter.nutrition.NutritionViewModel;
import interface_adapter.nutrition.meal.MealEditorViewModel;
import interface_adapter.recommendation.RefreshMealRecommendationsController;

/**
 * View for displaying nutrition recommendations and meal options.
 */
public class NutritionView extends JPanel implements PropertyChangeListener {

    private static final int MEAL_AREA_ROWS = 4;
    private static final int MEAL_AREA_COLUMNS = 30;

    private final String viewName = "nutrition";
    private final NutritionViewModel nutritionViewModel;
    private final MainViewManagerModel mainViewManagerModel;
    private final MealEditorViewModel mealEditorViewModel;
    private final ViewMealsView viewMealsView;

    private final JLabel calorieLabel = new JLabel();
    private final JLabel proteinLabel = new JLabel();
    private final JLabel bmiLabel = new JLabel();
    private final JLabel messageLabel = new JLabel();
    private final JLabel mealRecommendationsTitle = new JLabel("Suggested Meals");
    private final JTextArea mealRecommendationsArea = new JTextArea(MEAL_AREA_ROWS, MEAL_AREA_COLUMNS);
    private final JButton refreshButton = new JButton("Refresh Recommendations");

    private RefreshMealRecommendationsController refreshMealRecommendationsController;

    /**
     * Constructs a NutritionView instance.
     *
     * @param nutritionViewModel view model managing nutrition state
     * @param mainViewManagerModel manager model for top-level view navigation
     * @param mealEditorViewModel view model for editing meals
     * @param viewMealsView sub-view for viewing meals
     */
    public NutritionView(final NutritionViewModel nutritionViewModel, final MainViewManagerModel mainViewManagerModel,
                         final MealEditorViewModel mealEditorViewModel, final ViewMealsView viewMealsView) {
        this.viewMealsView = viewMealsView;
        this.nutritionViewModel = nutritionViewModel;
        this.mainViewManagerModel = mainViewManagerModel;
        this.mealEditorViewModel = mealEditorViewModel;
        nutritionViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final JLabel title = new JLabel("Nutrition Recommendations");
        final JButton addMealButton = new JButton("Add Meal");

        addMealButton.addActionListener(evt -> {
            mealEditorViewModel.getState().reset();
            mealEditorViewModel.firePropertyChanged();
            mainViewManagerModel.setState("meal editor");
            mainViewManagerModel.firePropertyChanged();
        });

        refreshButton.addActionListener(evt -> {
            if (refreshMealRecommendationsController != null) {
                refreshMealRecommendationsController.execute();
            }
        });
        mealRecommendationsArea.setEditable(false);
        mealRecommendationsArea.setLineWrap(true);

        this.add(title);
        this.add(calorieLabel);
        this.add(proteinLabel);
        this.add(bmiLabel);
        this.add(messageLabel);
        this.add(refreshButton);
        this.add(mealRecommendationsTitle);
        this.add(mealRecommendationsArea);
        this.add(addMealButton);
        this.add(viewMealsView);

        displayState(nutritionViewModel.getState());
    }

    private void displayState(final NutritionState state) {
        calorieLabel.setText("Daily calorie target: " + state.getDailyCalorieTarget() + " kcal");
        proteinLabel.setText("Daily protein target: " + state.getDailyProteinGrams() + " g");
        bmiLabel.setText(String.format("BMI: %.1f", state.getBmi()));
        messageLabel.setText(state.getMessage());

        final StringBuilder mealsText = new StringBuilder();
        for (final MealRecommendation meal : state.getMealRecommendations()) {
            mealsText.append(meal.getTitle())
                    .append(" (").append(meal.getReadyInMinutes()).append(" min)\n");
        }
        mealRecommendationsArea.setText(mealsText.toString());
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        displayState((NutritionState) evt.getNewValue());
    }

    public String getViewName() {
        return viewName;
    }

    public void setRefreshMealRecommendationsController(
            final RefreshMealRecommendationsController refreshMealRecommendationsController) {
        this.refreshMealRecommendationsController = refreshMealRecommendationsController;
    }
}
