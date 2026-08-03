package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import entity.FoodSearchResult;
import entity.FoodUnit;
import interface_adapter.nutrition.food.AddFoodController;
import interface_adapter.nutrition.food.EditFoodController;
import interface_adapter.nutrition.food.FoodEditorState;
import interface_adapter.nutrition.food.FoodEditorViewModel;
import interface_adapter.nutrition.food.SearchFoodController;
import use_case.nutrition.food.FoodNutritionInputData;

public class FoodEditorView extends JPanel implements PropertyChangeListener {

    private static final int SEARCH_DEBOUNCE_MILLIS = 300;

    private AddFoodController addFoodController;
    private EditFoodController editFoodController;
    private SearchFoodController searchFoodController;
    private final FoodEditorViewModel foodEditorViewModel;

    private final JTextField foodNameField;
    private final JTextField caloriesField;
    private final JTextField proteinField;
    private final JTextField carbsField;
    private final JTextField fatField;
    private final JTextField quantityField;
    private final JTextField gramsField;
    private final JComboBox<FoodUnit> unitBox;
    private final JLabel quantityLabel;
    private final JLabel unitLabel;
    private final JLabel errorLabel;
    private final JLabel servingLabelValue;
    private final JPanel formPanel;

    private final JTextField searchField;
    private final JPanel searchResultsPanel;
    private final Timer searchDebounceTimer;

    private boolean isUpdatingFromState;

    public FoodEditorView(FoodEditorViewModel foodEditorViewModel) {
        this.foodEditorViewModel = foodEditorViewModel;
        this.foodEditorViewModel.addPropertyChangeListener(this);
        errorLabel = new JLabel("");
        servingLabelValue = new JLabel("");

        foodNameField = new JTextField("");
        caloriesField = new JTextField("0.0");
        proteinField = new JTextField("0.0");
        carbsField = new JTextField("0.0");
        fatField = new JTextField("0.0");
        quantityField = new JTextField("1");
        gramsField = new JTextField("0.0");
        unitBox = new JComboBox<>(FoodUnit.values());
        quantityLabel = new JLabel("Quantity");
        unitLabel = new JLabel("Unit");

        formPanel = createFormPanel();

        searchField = new JTextField();
        searchResultsPanel = new JPanel();
        searchResultsPanel.setLayout(new GridLayout(0, 1));
        searchDebounceTimer = new Timer(SEARCH_DEBOUNCE_MILLIS, evt -> {
            final String query = foodEditorViewModel.getState().getSearchQuery();
            if (!query.isBlank() && searchFoodController != null) {
                searchFoodController.execute(query);
            }
        });
        searchDebounceTimer.setRepeats(false);

        addListeners();

        final JButton saveButton = new JButton("Save Food");
        saveButton.addActionListener(evt -> {
            saveFood();
        });

        final JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(saveButton);
        bottomPanel.add(errorLabel);

        setLayout(new BorderLayout());
        final JPanel centerPanel = new JPanel(new BorderLayout());

        centerPanel.add(createSearchPanel(), BorderLayout.NORTH);
        centerPanel.add(formPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {

        final JPanel formPanel = new JPanel(new GridLayout(9, 2));
        formPanel.add(new JLabel("Food Name"));
        formPanel.add(foodNameField);

        formPanel.add(new JLabel("Serving"));
        formPanel.add(servingLabelValue);

        formPanel.add(new JLabel("Calories"));
        formPanel.add(caloriesField);

        formPanel.add(new JLabel("Protein"));
        formPanel.add(proteinField);

        formPanel.add(new JLabel("Carbs"));
        formPanel.add(carbsField);

        formPanel.add(new JLabel("Fat"));
        formPanel.add(fatField);

        formPanel.add(quantityLabel);
        formPanel.add(quantityField);

        formPanel.add(unitLabel);
        formPanel.add(unitBox);

        formPanel.add(new JLabel("Grams"));
        formPanel.add(gramsField);
        return formPanel;
    }

    private void saveFood() {
        final FoodEditorState state = foodEditorViewModel.getState();
        if (state.getEditingFood() == null) {
            addFoodController.execute(state.getFoodName(), new FoodNutritionInputData(state.getTotalCaloriesDisplay(),
                            state.getTotalProteinDisplay(), state.getTotalCarbsDisplay(), state.getTotalFatDisplay()),
                    state.getQuantity(), state.getUnit(), state.getTotalGramsDisplay());
        }
        else {
            editFoodController.execute(state.getEditingFood(), state.getFoodName(), new FoodNutritionInputData(
                            state.getTotalCaloriesDisplay(), state.getTotalProteinDisplay(), state.getTotalCarbsDisplay(),
                            state.getTotalFatDisplay()),
                    state.getQuantity(), state.getUnit(), state.getTotalGramsDisplay());
        }
    }

    private JPanel createSearchPanel() {

        final JPanel searchPanel = new JPanel(new BorderLayout());

        searchPanel.add(new JLabel("Search Food"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchResultsPanel, BorderLayout.SOUTH);

        return searchPanel;
    }

    private void addTextListener(JTextField field, Runnable updater) {

        field.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {
                if (!isUpdatingFromState) {
                    updater.run();
                }
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

    private void updateState(java.util.function.Consumer<FoodEditorState> updater) {
        final FoodEditorState state = foodEditorViewModel.getState();
        updater.accept(state);
        foodEditorViewModel.setState(state);
        foodEditorViewModel.firePropertyChanged();
    }

    private void addListeners() {
        foodEditorViewModel.getState().setSearchSelected(false);
        addTextListener(searchField, () -> {
            updateState(state -> {
                state.setSearchQuery(searchField.getText());
                state.setSearchSelected(true);
            });
            searchDebounceTimer.restart();
        });

        addTextListener(foodNameField, () -> {
            updateState(state -> {
                state.setFoodName(foodNameField.getText()); }); });

        addTextListener(caloriesField, () -> {
            updateState(state -> {
                state.setTotalCaloriesDisplay(caloriesField.getText()); }); });

        addTextListener(proteinField, () -> {
            updateState(state -> {
                state.setTotalProteinDisplay(proteinField.getText()); }); });

        addTextListener(carbsField, () -> {
            updateState(state -> {
                state.setTotalCarbsDisplay(carbsField.getText()); }); });

        addTextListener(fatField, () -> {
            updateState(state -> {
                state.setTotalFatDisplay(fatField.getText()); }); });

        addTextListener(quantityField, () -> {
            updateState(state -> {
                state.setQuantity(quantityField.getText()); }); });

        addTextListener(gramsField, () -> {
            updateState(state -> {
                state.setTotalGramsDisplay(gramsField.getText()); }); });

        unitBox.addActionListener(evt -> {
            if (!isUpdatingFromState) {
                updateState(state -> {
                    state.setUnit((FoodUnit) unitBox.getSelectedItem()); });
            }
        });

    }

    private void updateField(JTextField field, String value) {

        if (!field.getText().equals(value)) {
            field.setText(value);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final FoodEditorState state = (FoodEditorState) evt.getNewValue();

        isUpdatingFromState = true;
        try {
            updateField(searchField, state.getSearchQuery());
            updateField(foodNameField, state.getFoodName());
            servingLabelValue.setText(state.getServingLabel());
            updateField(caloriesField, state.getTotalCaloriesDisplay());
            updateField(proteinField, state.getTotalProteinDisplay());
            updateField(carbsField, state.getTotalCarbsDisplay());
            updateField(fatField, state.getTotalFatDisplay());
            updateField(quantityField, state.getQuantity());
            updateField(gramsField, state.getTotalGramsDisplay());
            if (unitBox.getSelectedItem() != state.getUnit()) {
                unitBox.setSelectedItem(state.getUnit());
            }
        }
        finally {
            isUpdatingFromState = false;
        }
        updateSearchResults(state);
        errorLabel.setText(state.getError());
    }

    private void updateSearchResults(FoodEditorState state) {

        searchResultsPanel.removeAll();
        for (FoodSearchResult food : state.getSearchResults()) {
            final JButton button = new JButton(
                    food.getFoodName() + " - " + food.getServingLabel() + " (" + food.getServingGrams() + "g)");
            button.addActionListener(evt -> {
                selectFood(food);
            });
            searchResultsPanel.add(button);
        }
        searchResultsPanel.revalidate();
        searchResultsPanel.repaint();
    }

    private void selectFood(FoodSearchResult food) {

        updateState(state -> {
            state.setFoodName(food.getFoodName());
            state.setServingLabel(food.getServingLabel());
            state.setOriginalServingGrams(food.getServingGrams());
            state.setServingGrams(food.getServingGrams());

            state.setServingCalories(food.getServingCalories());
            state.setServingProtein(food.getServingProtein());
            state.setServingCarbs(food.getServingCarbs());
            state.setServingFat(food.getServingFat());

            state.setQuantity("1");
            state.setUnit(FoodUnit.DEFAULT_SERVING);
            state.recalculateTotals();

            state.getSearchResults().clear();
        });

    }

    public void setAddFoodController(AddFoodController addFoodController) {
        this.addFoodController = addFoodController;
    }

    public void setEditFoodController(EditFoodController editFoodController) {
        this.editFoodController = editFoodController;
    }

    public void setSearchFoodController(SearchFoodController controller) {
        this.searchFoodController = controller;
    }

    /**
     * Clears any in-progress food form state, even when navigating away without saving.
     */
    public void resetState() {
        foodEditorViewModel.getState().reset();
        foodEditorViewModel.firePropertyChanged();
    }
}
