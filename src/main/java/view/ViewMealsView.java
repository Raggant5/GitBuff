package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entity.Meal;
import interface_adapter.nutrition.food_editor.PrepareEditFoodController;
import interface_adapter.nutrition.meals.ViewMealsState;
import interface_adapter.nutrition.meals.ViewMealsViewModel;

public class ViewMealsView extends JPanel implements PropertyChangeListener {
    private final String viewName = "view meals";
    private final ViewMealsViewModel viewMealsViewModel;
    private PrepareEditFoodController prepareEditFoodController;

    private final JPanel mealPanelContainer;
    private final JLabel errorLabel;

    public ViewMealsView(ViewMealsViewModel viewMealsViewModel, PrepareEditFoodController prepareEditFoodController) {

        this.viewMealsViewModel = viewMealsViewModel;
        this.prepareEditFoodController = prepareEditFoodController;
        this.viewMealsViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        final JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        final JLabel titleLabel = new JLabel("View Meals");
        errorLabel = new JLabel("");

        headerPanel.add(titleLabel);
        headerPanel.add(errorLabel);

        add(headerPanel, BorderLayout.NORTH);

        mealPanelContainer = new JPanel();
        mealPanelContainer.setLayout(new BoxLayout(mealPanelContainer, BoxLayout.Y_AXIS));

        add(mealPanelContainer, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ViewMealsState state = (ViewMealsState) evt.getNewValue();
        displayMeals(state);
    }

    private void displayMeals(ViewMealsState state) {

        mealPanelContainer.removeAll();
        errorLabel.setText(state.getError());
        for (Meal meal : state.getMeals()) {
            mealPanelContainer.add(new MealPanel(meal, prepareEditFoodController));
        }

        mealPanelContainer.revalidate();
        mealPanelContainer.repaint();
    }

    public String getViewName() {
        return viewName;
    }
}
