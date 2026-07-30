package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.nutrition.meal_editor.AddMealController;
import interface_adapter.nutrition.meal_editor.MealEditorViewModel;
import interface_adapter.nutrition.meal_editor.MealEditorState;

public class MealEditorView extends JPanel implements PropertyChangeListener {
    private final String viewName = "add meal";
    private final JTextField mealNameField;
    private final MealEditorViewModel mealEditorViewModel;
    private final FoodEntryListPanel foodEntryListPanel;
    private AddMealController addMealController;

    private final FoodEditorView foodEditorView;

    public MealEditorView(MealEditorViewModel mealEditorViewModel, FoodEditorView foodEditorView) {
        setVisible(false);
        this.mealEditorViewModel = mealEditorViewModel;
        this.foodEditorView = foodEditorView;
        mealEditorViewModel.addPropertyChangeListener(this);

        mealNameField = new JTextField(20);
        final JLabel mealNameLabel = new JLabel("Meal Name");
        setLayout(new BorderLayout());

        final LabelTextPanel topPanel = new LabelTextPanel(mealNameLabel, mealNameField);
        topPanel.add(mealNameLabel);
        topPanel.add(mealNameField);
        add(topPanel, BorderLayout.NORTH);

        foodEntryListPanel = new FoodEntryListPanel();
        add(foodEntryListPanel, BorderLayout.CENTER);

        final JPanel buttonPanel = new JPanel(new FlowLayout());
        final JButton addFoodButton = new JButton("Add Food");
        addFoodButton.addActionListener(evt -> {
            foodEditorView.setVisible(true);
        });

        final JButton saveButton = new JButton("Save Meal");
        saveButton.addActionListener(evt -> {
            final MealEditorState mealEditorState = mealEditorViewModel.getState();
            this.addMealController.execute(mealEditorState.getName(),
                mealEditorState.getFoodEntriesForMeal());
        });

        buttonPanel.add(addFoodButton);
        buttonPanel.add(saveButton);
        final JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(foodEditorView, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        mealNameField.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {
                final MealEditorState state = mealEditorViewModel.getState();
                state.setName(mealNameField.getText());
                mealEditorViewModel.setState(state);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final MealEditorState currentState = (MealEditorState) evt.getNewValue();
        foodEntryListPanel.setFoodEntries(currentState.getFoodEntriesForMeal());
        if (!mealNameField.getText().equals(currentState.getName())) {
            mealNameField.setText(currentState.getName());
        }
    }

    public void setAddMealController(AddMealController addMealController) {
        this.addMealController = addMealController;
    }

    public String getViewName() {
        return viewName;
    }
}
