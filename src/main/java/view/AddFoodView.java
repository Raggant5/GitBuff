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
import interface_adapter.nutrition.food.AddFoodController;
import interface_adapter.nutrition.food.FoodState;
import interface_adapter.nutrition.food.FoodViewModel;

public class AddFoodView extends JPanel implements PropertyChangeListener {

    private AddFoodController addFoodController;
    private final FoodViewModel foodViewModel;

    private final JTextField foodNameField;
    private final JTextField caloriesField;
    private final JTextField proteinField;
    private final JTextField carbsField;
    private final JTextField fatField;
    private final JTextField quantityField;
    private final JTextField gramsField;
    private final JComboBox<FoodUnit> unitBox;
    private final JLabel errorLabel;

    public AddFoodView(FoodViewModel foodViewModel) {
        setVisible(false);
        this.foodViewModel = foodViewModel;
        this.foodViewModel.addPropertyChangeListener(this);
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
            final FoodState state = foodViewModel.getState();
            state.setFoodName(foodNameField.getText());
            foodViewModel.setState(state);
        });

        addTextListener(caloriesField, () -> {
            final FoodState state = foodViewModel.getState();
            state.setCalories(caloriesField.getText());
            foodViewModel.setState(state);
        });

        addTextListener(proteinField, () -> {
            final FoodState state = foodViewModel.getState();
            state.setProtein(proteinField.getText());
            foodViewModel.setState(state);
        });

        addTextListener(carbsField, () -> {
            final FoodState state = foodViewModel.getState();
            state.setCarbs(carbsField.getText());
            foodViewModel.setState(state);
        });

        addTextListener(fatField, () -> {
            final FoodState state = foodViewModel.getState();
            state.setFat(fatField.getText());
            foodViewModel.setState(state);
        });

        addTextListener(quantityField, () -> {
            final FoodState state = foodViewModel.getState();
            state.setQuantity(quantityField.getText());
            foodViewModel.setState(state);
        });

        addTextListener(gramsField, () -> {
            final FoodState state = foodViewModel.getState();
            state.setGrams(gramsField.getText());
            foodViewModel.setState(state);
        });

        unitBox.addActionListener(evt -> {
            final FoodState state = foodViewModel.getState();
            state.setUnit((FoodUnit) unitBox.getSelectedItem());
            foodViewModel.setState(state);
        });

        final JButton saveButton = new JButton("Save Food");
        saveButton.addActionListener(evt -> {
            final FoodState state = foodViewModel.getState();
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
                setVisible(false);

            }
            catch (NumberFormatException exc) {
                errorLabel.setText("Please enter valid numbers.");
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
        final FoodState state = (FoodState) evt.getNewValue();
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
}
