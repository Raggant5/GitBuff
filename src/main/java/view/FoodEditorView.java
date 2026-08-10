package view;

import java.awt.BorderLayout;
import java.awt.Color;
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

import entity.FoodUnit;
import interface_adapter.nutrition.food.AddFoodController;
import interface_adapter.nutrition.food.ChangeServingSizeController;
import interface_adapter.nutrition.food.EditFoodController;
import interface_adapter.nutrition.food.FoodEditorState;
import interface_adapter.nutrition.food.FoodEditorViewModel;
import interface_adapter.nutrition.food.FoodNutritionDisplayData;
import interface_adapter.nutrition.food.FoodSearchResultDisplayData;
import interface_adapter.nutrition.food.FoodServingDetails;
import interface_adapter.nutrition.food.SearchFoodController;

public class FoodEditorView extends JPanel implements PropertyChangeListener {

    private static final int SEARCH_DEBOUNCE_MILLIS = 300;

    private static final String DEFAULT_VALUE_INPUT = "0.0";

    private AddFoodController addFoodController;
    private EditFoodController editFoodController;
    private SearchFoodController searchFoodController;
    private ChangeServingSizeController changeServingSizeController;
    private final FoodEditorViewModel foodEditorViewModel;

    private JTextField foodNameField;
    private JTextField caloriesField;
    private JTextField proteinField;
    private JTextField carbsField;
    private JTextField fatField;
    private JTextField quantityField;
    private JTextField gramsField;
    private JComboBox<FoodUnit> unitBox;
    private JLabel quantityLabel;
    private JLabel unitLabel;
    private JLabel servingLabelValue;
    private JPanel formPanel;

    private JLabel caloriesErrorLabel;
    private JLabel proteinErrorLabel;
    private JLabel carbsErrorLabel;
    private JLabel fatErrorLabel;
    private JLabel quantityErrorLabel;
    private JLabel gramsErrorLabel;
    private JLabel submitErrorLabel;

    private final JTextField searchField;
    private final JPanel searchResultsPanel;
    private final Timer searchDebounceTimer;

    private boolean isUpdatingFromState;

    public FoodEditorView(FoodEditorViewModel foodEditorViewModel) {
        this.foodEditorViewModel = foodEditorViewModel;
        this.foodEditorViewModel.addPropertyChangeListener(this);
        submitErrorLabel = new JLabel("");
        submitErrorLabel.setForeground(Color.RED);
        servingLabelValue = new JLabel("");
        final int formRowCount = 9;
        final int formColCount = 3;
        formPanel = new JPanel(new GridLayout(formRowCount, formColCount));
        createFormPanel();

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
        bottomPanel.add(submitErrorLabel);

        setLayout(new BorderLayout());
        final JPanel centerPanel = new JPanel(new BorderLayout());

        centerPanel.add(createSearchPanel(), BorderLayout.NORTH);
        centerPanel.add(formPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JLabel createFieldErrorLabel() {
        final JLabel label = new JLabel("");
        label.setForeground(Color.RED);
        return label;
    }

    private void createFieldErrorLabels() {
        caloriesErrorLabel = createFieldErrorLabel();
        proteinErrorLabel = createFieldErrorLabel();
        carbsErrorLabel = createFieldErrorLabel();
        fatErrorLabel = createFieldErrorLabel();
        quantityErrorLabel = createFieldErrorLabel();
        gramsErrorLabel = createFieldErrorLabel();
    }

    private void createFormPanel() {

        createFormFields();
        createFieldErrorLabels();

        formPanel.add(new JLabel("Food Name"));
        formPanel.add(foodNameField);
        formPanel.add(new JLabel(""));

        formPanel.add(new JLabel("Serving"));
        formPanel.add(servingLabelValue);
        formPanel.add(new JLabel(""));

        formPanel.add(new JLabel("Calories"));
        formPanel.add(caloriesField);
        formPanel.add(caloriesErrorLabel);

        formPanel.add(new JLabel("Protein"));
        formPanel.add(proteinField);
        formPanel.add(proteinErrorLabel);

        formPanel.add(new JLabel("Carbs"));
        formPanel.add(carbsField);
        formPanel.add(carbsErrorLabel);

        formPanel.add(new JLabel("Fat"));
        formPanel.add(fatField);
        formPanel.add(fatErrorLabel);

        formPanel.add(quantityLabel);
        formPanel.add(quantityField);
        formPanel.add(quantityErrorLabel);

        formPanel.add(unitLabel);
        formPanel.add(unitBox);
        formPanel.add(new JLabel(""));

        formPanel.add(new JLabel("Grams"));
        formPanel.add(gramsField);
        formPanel.add(gramsErrorLabel);
    }

    private void saveFood() {
        final FoodEditorState state = foodEditorViewModel.getState();
        final FoodServingDetails servingDetails = state.getServingDetails();
        final FoodNutritionDisplayData nutritionDisplayData = new FoodNutritionDisplayData(
                servingDetails.getTotalCaloriesDisplay(), servingDetails.getTotalProteinDisplay(),
                servingDetails.getTotalCarbsDisplay(), servingDetails.getTotalFatDisplay());
        if (state.getEditingFoodEntryId() == null) {
            addFoodController.execute(state.getFoodName(), nutritionDisplayData,
                    servingDetails.getQuantity(), servingDetails.getUnit(), servingDetails.getTotalGramsDisplay());
        }
        else {
            editFoodController.execute(state.getEditingFoodEntryId(), state.getFoodName(), nutritionDisplayData,
                    servingDetails.getQuantity(), servingDetails.getUnit(), servingDetails.getTotalGramsDisplay());
        }
    }

    private JPanel createSearchPanel() {

        final JPanel searchPanel = new JPanel(new BorderLayout());

        searchPanel.add(new JLabel("Search Food"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchResultsPanel, BorderLayout.SOUTH);

        return searchPanel;
    }

    private void createFormFields() {
        foodNameField = new JTextField("");
        caloriesField = new JTextField(DEFAULT_VALUE_INPUT);
        proteinField = new JTextField(DEFAULT_VALUE_INPUT);
        carbsField = new JTextField(DEFAULT_VALUE_INPUT);
        fatField = new JTextField(DEFAULT_VALUE_INPUT);
        quantityField = new JTextField("1");
        gramsField = new JTextField(DEFAULT_VALUE_INPUT);
        unitBox = new JComboBox<>(FoodUnit.values());
        quantityLabel = new JLabel("Quantity");
        unitLabel = new JLabel("Unit");
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
        state.setSubmitError("");
        updater.accept(state);
        foodEditorViewModel.setState(state);
        foodEditorViewModel.firePropertyChanged();
    }

    private void addListeners() {
        addTextListener(searchField, () -> {
            updateState(state -> {
                state.setSearchQuery(searchField.getText());
            });
            searchDebounceTimer.restart();
        });

        addTextListener(foodNameField, () -> {
            updateState(state -> {
                state.setFoodName(foodNameField.getText()); }); });

        addTextListener(caloriesField, () -> {
            final String value = caloriesField.getText();
            updateState(state -> state.getServingDetails().setTotalCaloriesDisplay(value));
        });

        addTextListener(proteinField, () -> {
            final String value = proteinField.getText();
            updateState(state -> state.getServingDetails().setTotalProteinDisplay(value));
        });

        addTextListener(carbsField, () -> {
            final String value = carbsField.getText();
            updateState(state -> state.getServingDetails().setTotalCarbsDisplay(value));
        });

        addTextListener(fatField, () -> {
            final String value = fatField.getText();
            updateState(state -> state.getServingDetails().setTotalFatDisplay(value));
        });

        addTextListener(quantityField, () -> {
            final String value = quantityField.getText();
            updateState(state -> state.getServingDetails().setQuantity(value));
        });

        addTextListener(gramsField, () -> {
            final String value = gramsField.getText();
            updateState(state -> state.getServingDetails().setTotalGramsDisplay(value));
        });

        unitBox.addActionListener(evt -> {
            if (!isUpdatingFromState) {
                final FoodServingDetails servingDetails = foodEditorViewModel.getState().getServingDetails();
                final FoodUnit selectedUnit = (FoodUnit) unitBox.getSelectedItem();
                changeServingSizeController.execute(selectedUnit, servingDetails.getOriginalServingGrams(),
                        servingDetails.getServingGrams(), servingDetails.getServingCalories(),
                        servingDetails.getServingProtein(), servingDetails.getServingCarbs(),
                        servingDetails.getServingFat());
            }
        });

    }

    private void updateField(JTextField field, String value) {

        if (!field.hasFocus()) {
            if (!field.getText().equals(value)) {
                field.setText(value);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final FoodEditorState state = (FoodEditorState) evt.getNewValue();

        isUpdatingFromState = true;
        final FoodServingDetails servingDetails = state.getServingDetails();
        try {
            updateField(searchField, state.getSearchQuery());
            updateField(foodNameField, state.getFoodName());
            servingLabelValue.setText(servingDetails.getServingLabel());
            updateField(caloriesField, servingDetails.getTotalCaloriesDisplay());
            updateField(proteinField, servingDetails.getTotalProteinDisplay());
            updateField(carbsField, servingDetails.getTotalCarbsDisplay());
            updateField(fatField, servingDetails.getTotalFatDisplay());
            updateField(quantityField, servingDetails.getQuantity());
            updateField(gramsField, servingDetails.getTotalGramsDisplay());
            if (unitBox.getSelectedItem() != servingDetails.getUnit()) {
                unitBox.setSelectedItem(servingDetails.getUnit());
            }
        }
        finally {
            isUpdatingFromState = false;
        }
        updateSearchResults(state);
        caloriesErrorLabel.setText(state.getCaloriesError());
        proteinErrorLabel.setText(state.getProteinError());
        carbsErrorLabel.setText(state.getCarbsError());
        fatErrorLabel.setText(state.getFatError());
        quantityErrorLabel.setText(state.getQuantityError());
        gramsErrorLabel.setText(state.getGramsError());
        submitErrorLabel.setText(state.getSubmitError());
    }

    private void updateSearchResults(FoodEditorState state) {

        searchResultsPanel.removeAll();
        for (FoodSearchResultDisplayData food : state.getSearchResults()) {
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

    private void selectFood(FoodSearchResultDisplayData food) {
        updateState(state -> {
            state.selectSearchResult(food.getFoodName(), food.getServingLabel(), food.getServingGrams(),
                    food.getNutrition(), food.getUnit(), food.getQuantity());
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

    public void setChangeServingSizeController(ChangeServingSizeController controller) {
        this.changeServingSizeController = controller;
    }

    /**
     * Clears any in-progress food form state, even when navigating away without saving.
     */
    public void resetState() {
        foodEditorViewModel.getState().reset();
        foodEditorViewModel.firePropertyChanged();
    }
}
