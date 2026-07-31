package view;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import entity.FoodEntry;
import interface_adapter.nutrition.food_editor.PrepareEditFoodController;

public class FoodEntryListPanel extends JPanel {

    public FoodEntryListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * Refreshes the list of food entries displayed on the screen given a new list.
     * @param foodsList the list of new food entries to display
     * @param prepareEditFoodController executes the Prepare Switch to Edit Food View Use Case
     */
    public void setFoodEntries(List<FoodEntry> foodsList, PrepareEditFoodController prepareEditFoodController) {
        removeAll();
        if (foodsList != null) {
            for (FoodEntry food : foodsList) {
                this.add(new FoodEntryPanel(food, prepareEditFoodController));
            }
        }
        revalidate();
        repaint();
    }
}
