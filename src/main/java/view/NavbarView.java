package view;

import interface_adapter.MainViewManagerModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NavbarView extends JPanel implements PropertyChangeListener {

    private final String viewName = "navbar";
    private final JButton toDashboard = new JButton("Dashboard");
    private final JButton toWorkouts = new JButton("Workouts");
    private final JButton toNutrition = new JButton("Nutrition");
    private final JButton toProfile = new JButton("Profile");
    private final JButton logOut = new JButton("Log Out");
    private LogoutController logoutController;

    public NavbarView(MainViewManagerModel mainViewManagerModel, ViewManagerModel viewManagerModel,
                      ProfileViewModel profileViewModel) {
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

        toProfile.addActionListener(evt -> {
            mainViewManagerModel.setState("profile");
            mainViewManagerModel.firePropertyChanged();
        });

        logOut.addActionListener(evt -> {
            mainViewManagerModel.setState("dashboard");
            mainViewManagerModel.firePropertyChanged();
            final ProfileState profileState = profileViewModel.getState();
            this.logoutController.execute(profileState.getUsername());
            viewManagerModel.setState("log in");
            viewManagerModel.firePropertyChanged();
        });

        this.add(toDashboard);
        this.add(toWorkouts);
        this.add(toNutrition);
        this.add(toProfile);
        this.add(logOut);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void updateSelectedButton(String page) {

        Color defaultColor = UIManager.getColor("Button.background");
        Color selectedColour = Color.LIGHT_GRAY;

        toDashboard.setBackground(defaultColor);
        toWorkouts.setBackground(defaultColor);
        toNutrition.setBackground(defaultColor);
        toProfile.setBackground(defaultColor);

        switch (page) {
            case "dashboard": toDashboard.setBackground(selectedColour); break;
            case "workouts": toWorkouts.setBackground(selectedColour); break;
            case "nutrition": toNutrition.setBackground(selectedColour); break;
            case "profile": toProfile.setBackground(selectedColour); break;
            default: break;
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

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

}
