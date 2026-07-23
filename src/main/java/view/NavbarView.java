package view;

import interface_adapter.MainViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NavbarView extends JPanel implements PropertyChangeListener {

    private final String viewName = "navbar";
    private final JButton toDashboard = new JButton("Dashboard");
    private final JButton toWorkouts = new JButton("Workouts");
    private final JButton toNutrition = new JButton("Nutrition");
    private final JButton logOut = new JButton("Log Out");

    public NavbarView(MainViewManagerModel mainViewManagerModel) {
        toDashboard.addActionListener(evt -> {
            mainViewManagerModel.setState("dashboard");
            mainViewManagerModel.firePropertyChanged();
        });

        toWorkouts.addActionListener(evt -> {
            mainViewManagerModel.setState("workouts");
            mainViewManagerModel.firePropertyChanged();
        });

        toNutrition.addActionListener(evt -> {
            mainViewManagerModel.setState("nutrition");
            mainViewManagerModel.firePropertyChanged();
        });

        logOut.addActionListener(evt -> {
            mainViewManagerModel.setState("dashboard");
            mainViewManagerModel.firePropertyChanged();
        });

        this.add(toDashboard);
        this.add(toWorkouts);
        this.add(toNutrition);
        this.add(logOut);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void updateSelectedButton(String page) {

        Color defaultColor = UIManager.getColor("Button.background");
        Color selectedColor = Color.LIGHT_GRAY;

        toDashboard.setBackground(defaultColor);
        toWorkouts.setBackground(defaultColor);
        toNutrition.setBackground(defaultColor);

        switch (page) {
            case "dashboard": toDashboard.setBackground(selectedColor);
            case "workouts": toWorkouts.setBackground(selectedColor);
            case "nutrition": toNutrition.setBackground(selectedColor);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            String currentPage = (String) evt.getNewValue();
            updateSelectedButton(currentPage);
        }

    }
    public String getViewName() {
        return viewName;
    }

}
