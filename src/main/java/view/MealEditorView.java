package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.MainViewManagerModel;
import interface_adapter.nutrition.food.DeleteFoodController;
import interface_adapter.nutrition.food.PrepareEditFoodController;
import interface_adapter.nutrition.meal.AddMealController;
import interface_adapter.nutrition.meal.EditMealController;
import interface_adapter.nutrition.meal.MealEditorState;
import interface_adapter.nutrition.meal.MealEditorViewModel;

public class MealEditorView extends JPanel implements PropertyChangeListener {
    private static final int TEXT_FIELD_COL = 20;
    private final String viewName = "meal editor";
    private final String foodsListName = "food list";
    private final String foodEditorName = "food editor";

    private final JTextField mealNameField;
    private final MealEditorViewModel mealEditorViewModel;
    private FoodEntryListPanel foodEntryListPanel;
    private AddMealController addMealController;
    private EditMealController editMealController;
    private PrepareEditFoodController prepareEditFoodController;
    private DeleteFoodController deleteFoodController;
    private JPanel foodPanelContainer;
    private CardLayout cardLayout;

    private final FoodEditorView foodEditorView;
    private final JLabel errorLabel;

    public MealEditorView(MealEditorViewModel mealEditorViewModel, FoodEditorView foodEditorView,
                          PrepareEditFoodController prepareEditFoodController,
                          MainViewManagerModel mainViewManagerModel) {
        this.prepareEditFoodController = prepareEditFoodController;
        this.mealEditorViewModel = mealEditorViewModel;
        this.foodEditorView = foodEditorView;
        mealEditorViewModel.addPropertyChangeListener(this);
        mainViewManagerModel.addPropertyChangeListener(evt -> {
            if (!"meal editor".equals(evt.getNewValue())) {
                final MealEditorState state = mealEditorViewModel.getState();
                if (state.getShowFoodEditor()) {
                    state.setShowFoodEditor(false);
                    mealEditorViewModel.firePropertyChanged();
                }
                foodEditorView.resetState();
            }
        });

        mealNameField = new JTextField(TEXT_FIELD_COL);
        final JLabel mealNameLabel = new JLabel("Meal Name");
        errorLabel = new JLabel("");
        setLayout(new BorderLayout());

        final LabelTextPanel topPanel = new LabelTextPanel(mealNameLabel, mealNameField);
        topPanel.add(mealNameLabel);
        topPanel.add(mealNameField);
        add(topPanel, BorderLayout.NORTH);

        createFoodPanel();

        final JPanel buttonPanel = new JPanel(new FlowLayout());
        final JButton addFoodButton = new JButton("Add Food");
        addFoodButton.addActionListener(evt -> {
            prepareEditFoodController.switchToAddFoodEditor();
        });

        final JButton saveButton = new JButton("Save Meal");
        saveButton.addActionListener(evt -> {
            final MealEditorState mealEditorState = mealEditorViewModel.getState();
            if (mealEditorState.getEditingMealId() == null) {
                this.addMealController.execute(mealEditorState.getName(), mealEditorState.getFoodEntriesForMeal());
            }
            else {
                this.editMealController.execute(mealEditorState.getEditingMealId(), mealEditorState.getName(),
                        mealEditorState.getFoodEntriesForMeal(), mealEditorState.getFoodEntriesDeleteStage());
            }
        });

        buttonPanel.add(addFoodButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(errorLabel);

        add(buttonPanel, BorderLayout.SOUTH);

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

    private void createFoodPanel() {
        foodEntryListPanel = new FoodEntryListPanel();
        cardLayout = new CardLayout();
        foodPanelContainer = new JPanel(cardLayout);
        foodPanelContainer.add(foodEntryListPanel, foodsListName);
        foodPanelContainer.add(foodEditorView, foodEditorName);

        add(foodPanelContainer, BorderLayout.CENTER);
        cardLayout.show(foodPanelContainer, foodsListName);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final MealEditorState currentState = (MealEditorState) evt.getNewValue();
        foodEntryListPanel.setFoodEntries(currentState.getFoodEntriesForMeal(), prepareEditFoodController,
                deleteFoodController);
        if (!mealNameField.getText().equals(currentState.getName())) {
            mealNameField.setText(currentState.getName());
        }
        if (currentState.getShowFoodEditor()) {
            cardLayout.show(foodPanelContainer, foodEditorName);
        }
        else {
            cardLayout.show(foodPanelContainer, foodsListName);
        }
        errorLabel.setText(currentState.getErrorMessage());
    }

    public void setAddMealController(AddMealController addMealController) {
        this.addMealController = addMealController;
    }

    public void setEditMealController(EditMealController editMealController) {
        this.editMealController = editMealController;
    }

    public void setDeleteFoodController(DeleteFoodController deleteFoodController) {
        this.deleteFoodController = deleteFoodController;
    }

    public String getViewName() {
        return viewName;
    }
}
