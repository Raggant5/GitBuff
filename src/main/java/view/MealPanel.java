package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.nutrition.food.FoodEntryDisplayData;
import interface_adapter.nutrition.meal.DeleteMealController;
import interface_adapter.nutrition.meal.MealDisplayData;
import interface_adapter.nutrition.meal.PrepareEditMealController;

public class MealPanel extends JPanel {

    public MealPanel(MealDisplayData meal, PrepareEditMealController prepareEditMealController,
                     DeleteMealController deleteMealController) {

        setLayout(new BorderLayout());

        final JPanel mealInfoPanel = addMealLabels(meal);
        add(mealInfoPanel, BorderLayout.NORTH);

        final JPanel buttonPanel = new JPanel();
        final JButton editButton = new JButton("Edit Meal");
        final JButton deleteButton = new JButton("Delete Meal");

        editButton.addActionListener(evt -> {
            prepareEditMealController.execute(meal.getId());
        });

        deleteButton.addActionListener(evt -> {
            deleteMealController.execute(meal.getId());
        });

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private JPanel addMealLabels(MealDisplayData meal) {
        final JLabel nameLabel = new JLabel("Meal: " + meal.getName());
        final JLabel dateLabel = new JLabel("Date: " + meal.getDate());

        double totalCalories = 0;
        double totalProtein = 0;
        double totalFat = 0;
        double totalCarbs = 0;
        for (FoodEntryDisplayData food : meal.getFoodEntries()) {
            totalCalories += Double.parseDouble(food.getNutrition().getCalories());
            totalProtein += Double.parseDouble(food.getNutrition().getProtein());
            totalFat += Double.parseDouble(food.getNutrition().getFat());
            totalCarbs += Double.parseDouble(food.getNutrition().getCarbs());
        }
        final JLabel caloriesLabel = new JLabel("Calories: " + totalCalories);
        final JLabel proteinLabel = new JLabel("Protein: " + totalProtein);
        final JLabel fatLabel = new JLabel("Fat: " + totalFat);
        final JLabel carbsLabel = new JLabel("Carbs: " + totalCarbs);
        final JPanel mealInfoPanel = new JPanel();
        mealInfoPanel.setLayout(new FlowLayout());
        mealInfoPanel.add(nameLabel);
        mealInfoPanel.add(dateLabel);
        mealInfoPanel.add(caloriesLabel);
        mealInfoPanel.add(proteinLabel);
        mealInfoPanel.add(fatLabel);
        mealInfoPanel.add(carbsLabel);

        return mealInfoPanel;
    }
}
