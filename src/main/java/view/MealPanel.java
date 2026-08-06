package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.nutrition.meal.DeleteMealController;
import interface_adapter.nutrition.meal.MealDisplayData;
import interface_adapter.nutrition.meal.PrepareEditMealController;

public class MealPanel extends JPanel {

    public MealPanel(MealDisplayData meal, PrepareEditMealController prepareEditMealController,
                     DeleteMealController deleteMealController) {

        setLayout(new BorderLayout());
        final JLabel nameLabel = new JLabel("Meal: " + meal.getName());
        final JLabel dateLabel = new JLabel("Date: " + meal.getDate());

        final JPanel mealInfoPanel = new JPanel();
        mealInfoPanel.setLayout(new FlowLayout());
        mealInfoPanel.add(nameLabel);
        mealInfoPanel.add(dateLabel);
        add(mealInfoPanel, BorderLayout.NORTH);

        final JPanel buttonPanel = new JPanel();
        final JButton editButton = new JButton("Edit Meal");
        final JButton deleteButton = new JButton("Delete Meal");

        editButton.addActionListener(evt -> {
            prepareEditMealController.execute(meal.getEntity());
        });

        deleteButton.addActionListener(evt -> {
            deleteMealController.execute(meal.getId());
        });

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.CENTER);
    }
}
