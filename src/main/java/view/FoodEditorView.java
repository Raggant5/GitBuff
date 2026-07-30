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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import entity.FoodNutrition;
import entity.FoodUnit;
import interface_adapter.nutrition.food_editor.AddFoodController;
import interface_adapter.nutrition.food_editor.EditFoodController;
import interface_adapter.nutrition.food_editor.FoodEditorState;
import interface_adapter.nutrition.food_editor.FoodEditorViewModel;

public class FoodEditorView extends JPanel implements PropertyChangeListener {

    private AddFoodController addFoodController;
    private EditFoodController editFoodController;
    private final FoodEditorViewModel foodEditorViewModel;

    private final JTextField foodNameField;
    private final JTextField caloriesField;
    private final JTextField proteinField;
    private final JTextField carbsField;
    private final JTextField fatField;
    private final JTextField quantityField;
    private final JTextField gramsField;
    private final JComboBox<FoodUnit> unitBox;
    private final JLabel errorLabel;

    public FoodEditorView(FoodEditorViewModel foodEditorViewModel) {
        this.foodEditorViewModel = foodEditorViewModel;
        this.foodEditorViewModel.addPropertyChangeListener(this);
        errorLabel = new JLabel("");

        foodNameField = new JTextField();
        caloriesField = new JTextField();
        proteinField = new JTextField();
        carbsField = new JTextField();
        fatField = new JTextField();
        quantityField = new JTextField();
        gramsField = new JTextField();
        unitBox = new JComboBox<>(FoodUnit.values());

        final JPanel formPanel = new JPanel(new GridLayout(8, 2));
        formPanel.add(new JLabel("Food Name"));
        formPanel.add(foodNameField);

        formPanel.add(new JLabel("Calories"));
        formPanel.add(caloriesField);

        formPanel.add(new JLabel("Protein"));
        formPanel.add(proteinField);

        formPanel.add(new JLabel("Carbs"));
        formPanel.add(carbsField);

        formPanel.add(new JLabel("Fat"));
        formPanel.add(fatField);

        formPanel.add(new JLabel("Quantity"));
        formPanel.add(quantityField);

        formPanel.add(new JLabel("Unit"));
        formPanel.add(unitBox);

        formPanel.add(new JLabel("Grams"));
        formPanel.add(gramsField);

        addTextListener(foodNameField, () -> {
            final FoodEditorState state = foodEditorViewModel.getState();
            state.setFoodName(foodNameField.getText());
            foodEditorViewModel.setState(state);
        });

        addTextListener(caloriesField, () -> {
            final FoodEditorState state = foodEditorViewModel.getState();
            state.setCalories(caloriesField.getText());
            foodEditorViewModel.setState(state);
        });

        addTextListener(proteinField, () -> {
            final FoodEditorState state = foodEditorViewModel.getState();
            state.setProtein(proteinField.getText());
            foodEditorViewModel.setState(state);
        });

        addTextListener(carbsField, () -> {
            final FoodEditorState state = foodEditorViewModel.getState();
            state.setCarbs(carbsField.getText());
            foodEditorViewModel.setState(state);
        });

        addTextListener(fatField, () -> {
            final FoodEditorState state = foodEditorViewModel.getState();
            state.setFat(fatField.getText());
            foodEditorViewModel.setState(state);
        });

        addTextListener(quantityField, () -> {
            final FoodEditorState state = foodEditorViewModel.getState();
            state.setQuantity(quantityField.getText());
            foodEditorViewModel.setState(state);
        });

        addTextListener(gramsField, () -> {
            final FoodEditorState state = foodEditorViewModel.getState();
            state.setGrams(gramsField.getText());
            foodEditorViewModel.setState(state);
        });

        unitBox.addActionListener(evt -> {
            final FoodEditorState state = foodEditorViewModel.getState();
            state.setUnit((FoodUnit) unitBox.getSelectedItem());
            foodEditorViewModel.setState(state);
        });

        final JButton saveButton = new JButton("Save Food");
        saveButton.addActionListener(evt -> {
            final FoodEditorState state = foodEditorViewModel.getState();
            if (state.getEditingFood() == null) {
                try {
                    addFoodController.execute(
                            state.getFoodName(),
                            new FoodNutrition(
                                    Double.parseDouble(state.getCalories()),
                                    Double.parseDouble(state.getProtein()),
                                    Double.parseDouble(state.getCarbs()),
                                    Double.parseDouble(state.getFat())
                            ),
                            Double.parseDouble(state.getQuantity()),
                            state.getUnit(),
                            Double.parseDouble(state.getGrams())
                    );

                    errorLabel.setText("");

                }
                catch (NumberFormatException exc) {
                    errorLabel.setText("Please enter valid numbers.");
                }
            }
            else {
                editFoodController.execute(state.getEditingFood(), state.getFoodName(),
                        new FoodNutrition(
                                Double.parseDouble(state.getCalories()),
                                Double.parseDouble(state.getProtein()),
                                Double.parseDouble(state.getCarbs()),
                                Double.parseDouble(state.getFat())
                        ),
                        Double.parseDouble(state.getQuantity()),
                        state.getUnit(),
                        Double.parseDouble(state.getGrams()));
            }
        });

        final JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(saveButton);
        bottomPanel.add(errorLabel);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addTextListener(JTextField field, Runnable updater) {

        field.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {
                updater.run();
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

    private void updateField(JTextField field, String value) {

        if (!field.getText().equals(value)) {
            field.setText(value);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final FoodEditorState state = (FoodEditorState) evt.getNewValue();
        updateField(foodNameField, state.getFoodName());
        updateField(caloriesField, state.getCalories());
        updateField(proteinField, state.getProtein());
        updateField(carbsField, state.getCarbs());
        updateField(fatField, state.getFat());
        updateField(quantityField, state.getQuantity());
        updateField(gramsField, state.getGrams());
        if (unitBox.getSelectedItem() != state.getUnit()) {
            unitBox.setSelectedItem(state.getUnit());
        }
        errorLabel.setText(state.getError());
    }

    public void setAddFoodController(AddFoodController addFoodController) {
        this.addFoodController = addFoodController;
    }

    public void setEditFoodController(EditFoodController editFoodController) {
        this.editFoodController = editFoodController;
    }
}
