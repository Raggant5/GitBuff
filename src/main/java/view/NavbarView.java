package view;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

import interface_adapter.MainViewManagerModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;

/**
 * The View for the navigation bar, allowing switching between main views and logging out.
 */
public class NavbarView extends JPanel implements PropertyChangeListener {

    private final String viewName = "navbar";
    private final JButton toDashboard = new JButton("Dashboard");
    private final JButton toWorkouts = new JButton("Workouts");
    private final JButton toLogWorkouts = new JButton("Workout History");
    private final JButton toNutrition = new JButton("Nutrition");
    private final JButton toProfile = new JButton("Profile");
    private final JButton logOut = new JButton("Log Out");
    private LogoutController logoutController;

    /**
     * Constructs a NavbarView instance.
     *
     * @param mainViewManagerModel manager model for active main tab view
     * @param viewManagerModel manager model for top-level view navigation
     * @param profileViewModel view model for logged-in user profile state
     */
    public NavbarView(final MainViewManagerModel mainViewManagerModel,
                      final ViewManagerModel viewManagerModel,
                      final ProfileViewModel profileViewModel) {
        this.toDashboard.addActionListener(evt -> {
            mainViewManagerModel.setState("dashboard");
            mainViewManagerModel.firePropertyChanged();
        });

        this.toWorkouts.addActionListener(evt -> {
            mainViewManagerModel.setState("workouts");
            mainViewManagerModel.firePropertyChanged();
        });

        this.toLogWorkouts.addActionListener(evt -> {
            mainViewManagerModel.setState("view workouts");
            mainViewManagerModel.firePropertyChanged();
        });

        this.toNutrition.addActionListener(evt -> {
            mainViewManagerModel.setState("nutrition");
            mainViewManagerModel.firePropertyChanged();
        });

        this.toProfile.addActionListener(evt -> {
            mainViewManagerModel.setState("profile");
            mainViewManagerModel.firePropertyChanged();
        });

        this.logOut.addActionListener(evt -> {
            mainViewManagerModel.setState("dashboard");
            mainViewManagerModel.firePropertyChanged();
            final ProfileState profileState = profileViewModel.getState();
            if (this.logoutController != null) {
                this.logoutController.execute(profileState.getUsername());
            }
            viewManagerModel.setState("log in");
            viewManagerModel.firePropertyChanged();
        });

        this.add(this.toDashboard);
        this.add(this.toWorkouts);
        this.add(this.toLogWorkouts);
        this.add(this.toNutrition);
        this.add(this.toProfile);
        this.add(this.logOut);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void updateSelectedButton(final String page) {
        final Color defaultColor = UIManager.getColor("Button.background");
        final Color selectedColour = Color.LIGHT_GRAY;

        this.toDashboard.setBackground(defaultColor);
        this.toWorkouts.setBackground(defaultColor);
        this.toLogWorkouts.setBackground(defaultColor);
        this.toNutrition.setBackground(defaultColor);
        this.toProfile.setBackground(defaultColor);

        switch (page) {
            case "dashboard":
                this.toDashboard.setBackground(selectedColour);
                break;
            case "workouts":
                this.toWorkouts.setBackground(selectedColour);
                break;
            case "view workouts":
                this.toLogWorkouts.setBackground(selectedColour);
                break;
            case "nutrition":
                this.toNutrition.setBackground(selectedColour);
                break;
            case "profile":
                this.toProfile.setBackground(selectedColour);
                break;
            default:
                break;
        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final String currentPage = (String) evt.getNewValue();
            updateSelectedButton(currentPage);
        }
    }

    /**
     * Gets the view name.
     *
     * @return view name string
     */
    public String getViewName() {
        return this.viewName;
    }

    /**
     * Sets the logout controller.
     *
     * @param logoutController controller instance
     */
    public void setLogoutController(final LogoutController logoutController) {
        this.logoutController = logoutController;
    }
}
