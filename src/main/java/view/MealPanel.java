package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entity.Meal;

public class MealPanel extends JPanel {
    private final FoodEntryListPanel foodEntryListPanel;

    public MealPanel(Meal meal) {

        setLayout(new BorderLayout());
        final JLabel nameLabel = new JLabel("Meal: " + meal.getName());
        final JLabel dateLabel = new JLabel("Date: " + meal.getDate());

        final JPanel mealInfoPanel = new JPanel();
        mealInfoPanel.setLayout(new FlowLayout());
        mealInfoPanel.add(nameLabel);
        mealInfoPanel.add(dateLabel);
        add(mealInfoPanel, BorderLayout.NORTH);

        foodEntryListPanel = new FoodEntryListPanel();
        foodEntryListPanel.setFoodEntries(meal.getFoodEntries());
        add(foodEntryListPanel, BorderLayout.CENTER);

        final JPanel buttonPanel = new JPanel();
        final JButton editButton = new JButton("Edit");
        final JButton deleteButton = new JButton("Delete");

        editButton.addActionListener(evt -> {
        });

        deleteButton.addActionListener(evt -> {
        });

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}
