package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entity.Meal;
import interface_adapter.nutrition.meal.ViewMealsState;
import interface_adapter.nutrition.meal.ViewMealsViewModel;

public class ViewMealsView extends JPanel implements PropertyChangeListener {

    private final ViewMealsViewModel viewMealsViewModel;

    private final JPanel mealPanelContainer;
    private final JLabel errorLabel;

    public ViewMealsView(ViewMealsViewModel viewMealsViewModel) {

        this.viewMealsViewModel = viewMealsViewModel;
        this.viewMealsViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        errorLabel = new JLabel("");
        add(errorLabel, BorderLayout.NORTH);

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
            mealPanelContainer.add(new MealPanel(meal));
        }

        mealPanelContainer.revalidate();
        mealPanelContainer.repaint();
    }
}
