package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entity.Meal;
import interface_adapter.nutrition.meal_editor.PrepareEditMealController;
import interface_adapter.nutrition.meals.ViewMealsState;
import interface_adapter.nutrition.meals.ViewMealsViewModel;

public class ViewMealsView extends JPanel implements PropertyChangeListener {
    private final String viewName = "view meals";
    private final ViewMealsViewModel viewMealsViewModel;
    private final PrepareEditMealController prepareEditMealController;

    private final JPanel mealListContainer;
    private final JLabel errorLabel;

    public ViewMealsView(ViewMealsViewModel viewMealsViewModel, PrepareEditMealController prepareEditMealController) {

        this.prepareEditMealController = prepareEditMealController;
        this.viewMealsViewModel = viewMealsViewModel;
        this.viewMealsViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        final JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        final JLabel titleLabel = new JLabel("View Meals");
        errorLabel = new JLabel("");

        headerPanel.add(titleLabel);
        headerPanel.add(errorLabel);

        add(headerPanel, BorderLayout.NORTH);

        mealListContainer = new JPanel();
        mealListContainer.setLayout(new BoxLayout(mealListContainer, BoxLayout.Y_AXIS));
        add(mealListContainer, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ViewMealsState state = (ViewMealsState) evt.getNewValue();
        displayMeals(state);
    }

    private void displayMeals(ViewMealsState state) {

        mealListContainer.removeAll();
        errorLabel.setText(state.getError());
        for (Meal meal : state.getMeals()) {
            mealListContainer.add(new MealPanel(meal, prepareEditMealController));
        }

        mealListContainer.revalidate();
        mealListContainer.repaint();
    }

    public String getViewName() {
        return viewName;
    }
}
