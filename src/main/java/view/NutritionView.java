package view;

import interface_adapter.nutrition.NutritionViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NutritionView extends JPanel implements PropertyChangeListener {

    private final String viewName = "nutrition";
    private final JLabel nutritionLabel;

    public NutritionView(NutritionViewModel nutritionViewModel) {

        nutritionViewModel.addPropertyChangeListener(this);
        setLayout(new BorderLayout());
        nutritionLabel = new JLabel();
        this.add(nutritionLabel, BorderLayout.CENTER);
        nutritionLabel.setText(nutritionViewModel.getState());
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        nutritionLabel.setText((String) evt.getNewValue());
    }
    public String getViewName() {
        return viewName;
    }
}