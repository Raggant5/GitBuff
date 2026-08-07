package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.nutrition.food.DeleteFoodController;
import interface_adapter.nutrition.food.FoodEntryDisplayData;
import interface_adapter.nutrition.food.PrepareEditFoodController;

public class FoodEntryPanel extends JPanel {
    private final String gramsUnit = " g";

    public FoodEntryPanel(FoodEntryDisplayData food, PrepareEditFoodController prepareEditFoodController,
                          DeleteFoodController deleteFoodController) {
        setLayout(new BorderLayout());
        final JLabel foodNameLabel = new JLabel("Name: " + food.getFoodName());
        final JLabel caloriesLabel = new JLabel("Calories: " + food.getCalories());
        final JLabel proteinLabel = new JLabel("Protein: " + food.getProtein() + gramsUnit);
        final JLabel fatLabel = new JLabel("Fat: " + food.getFat() + gramsUnit);
        final JLabel carbsLabel = new JLabel("Carbs: " + food.getCarbs() + gramsUnit);
        final JLabel quantityLabel = new JLabel("Quantity: " + food.getQuantity() + " " + food.getUnit());
        final JLabel gramsLabel = new JLabel("Grams: " + food.getGrams() + gramsUnit);

        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        infoPanel.add(foodNameLabel);
        infoPanel.add(caloriesLabel);
        infoPanel.add(proteinLabel);
        infoPanel.add(fatLabel);
        infoPanel.add(carbsLabel);
        infoPanel.add(quantityLabel);
        infoPanel.add(gramsLabel);

        final JButton editButton = new JButton("Edit");
        final JButton deleteButton = new JButton("Delete");

        editButton.addActionListener(evt -> {
            prepareEditFoodController.execute(food.getEntity());
        });

        deleteButton.addActionListener(evt -> {
            deleteFoodController.execute(food.getEntity());
        });

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
