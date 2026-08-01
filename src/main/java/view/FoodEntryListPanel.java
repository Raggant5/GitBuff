package view;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import entity.FoodEntry;
import interface_adapter.nutrition.food.DeleteFoodController;
import interface_adapter.nutrition.food.PrepareEditFoodController;

public class FoodEntryListPanel extends JPanel {

    public FoodEntryListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * Refreshes the list of food entries displayed on the screen given a new list.
     *
     * @param foodsList the list of new food entries to display
     * @param prepareEditFoodController executes the Prepare Switch to Edit Food View Use Case
     * @param deleteFoodController executes the Delete Food Use Case
     */
    public void setFoodEntries(List<FoodEntry> foodsList, PrepareEditFoodController prepareEditFoodController,
                               DeleteFoodController deleteFoodController) {
        removeAll();
        if (foodsList != null) {
            for (FoodEntry food : foodsList) {
                this.add(new FoodEntryPanel(food, prepareEditFoodController, deleteFoodController));
            }
        }
        revalidate();
        repaint();
    }
}
