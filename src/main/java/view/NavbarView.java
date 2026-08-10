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
import interface_adapter.calendar.CalendarController;
import interface_adapter.log_workout.workout.GetWorkoutsController;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.nutrition.meal.GetMealsController;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import use_case.dashboard.DashboardInputBoundary;

/**
 * The View for the navigation bar, allowing switching between
 * main views and logging out.
 */
public class NavbarView extends JPanel
        implements PropertyChangeListener {

    private final String viewName = "navbar";

    private final JButton toDashboard =
            new JButton("Dashboard");

    private final JButton toWorkouts =
            new JButton("Workouts");

    private final JButton toLogWorkouts =
            new JButton("Workout History");

    private final JButton toNutrition =
            new JButton("Nutrition");

    private final JButton toProfile =
            new JButton("Profile");

    private final JButton logOut =
            new JButton("Log Out");

    private final DashboardInputBoundary dashboardInteractor;
    private final LoginViewModel loginViewModel;
    private final CalendarController calendarController;
    private final GetMealsController getMealsController;
    private final GetWorkoutsController getWorkoutsController;

    private LogoutController logoutController;

    /**
     * Constructs a NavbarView instance.
     *
     * @param mainViewManagerModel manager model for active main tab
     * @param viewManagerModel manager model for top-level navigation
     * @param profileViewModel logged-in user profile view model
     * @param loginViewModel logged-in user login view model
     * @param dashboardInteractor dashboard refresh interactor
     * @param calendarController controller for loading calendar events on dashboard nav
     * @param getMealsController controller that fetches saved meals when the Nutrition tab opens
     * @param getWorkoutsController controller that fetches workout history when the Workout
     *        History tab opens
     */
    public NavbarView(
            final MainViewManagerModel mainViewManagerModel,
            final ViewManagerModel viewManagerModel,
            final ProfileViewModel profileViewModel,
            final LoginViewModel loginViewModel,
            final DashboardInputBoundary dashboardInteractor,
            final CalendarController calendarController,
            final GetMealsController getMealsController,
            final GetWorkoutsController getWorkoutsController
    ) {
        this.loginViewModel = loginViewModel;
        this.dashboardInteractor = dashboardInteractor;
        this.calendarController = calendarController;
        this.getMealsController = getMealsController;
        this.getWorkoutsController = getWorkoutsController;

        mainViewManagerModel.addPropertyChangeListener(this);

        this.toDashboard.addActionListener(event -> {
            final String username = this.loginViewModel.getState().getUsername();
            if (this.dashboardInteractor != null && username != null && !username.isBlank()) {
                this.dashboardInteractor.execute(username);
            }
            if (this.calendarController != null) {
                this.calendarController.execute(username);
            }
            mainViewManagerModel.setState("dashboard");
            mainViewManagerModel.firePropertyChanged();
        });

        this.toWorkouts.addActionListener(event -> {
            mainViewManagerModel.setState("workouts");
            mainViewManagerModel.firePropertyChanged();
        });

        this.toLogWorkouts.addActionListener(evt -> {
            if (this.getWorkoutsController != null) {
                this.getWorkoutsController.execute(this.loginViewModel.getState().getUsername());
            }
            mainViewManagerModel.setState("view workouts");
            mainViewManagerModel.firePropertyChanged();
        });

        this.toNutrition.addActionListener(event -> {
            if (this.getMealsController != null) {
                this.getMealsController.execute(this.loginViewModel.getState().getUsername());
            }
            mainViewManagerModel.setState("nutrition");
            mainViewManagerModel.firePropertyChanged();
        });

        this.toProfile.addActionListener(event -> {
            mainViewManagerModel.setState("profile");
            mainViewManagerModel.firePropertyChanged();
        });

        this.logOut.addActionListener(event -> {
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

        this.setLayout(
                new BoxLayout(
                        this,
                        BoxLayout.Y_AXIS
                )
        );
    }

    private void updateSelectedButton(
            final String page
    ) {
        final Color defaultColor =
                UIManager.getColor("Button.background");

        final Color selectedColour =
                Color.LIGHT_GRAY;

        this.toDashboard.setBackground(defaultColor);
        this.toWorkouts.setBackground(defaultColor);
        this.toLogWorkouts.setBackground(defaultColor);
        this.toNutrition.setBackground(defaultColor);
        this.toProfile.setBackground(defaultColor);

        switch (page) {
            case "dashboard":
                this.toDashboard.setBackground(
                        selectedColour
                );
                break;

            case "workouts":
                this.toWorkouts.setBackground(selectedColour);
                break;

            case "nutrition":
                this.toNutrition.setBackground(
                        selectedColour
                );
                break;

            case "profile":
                this.toProfile.setBackground(
                        selectedColour
                );
                break;

            default:
                break;
        }
    }

    @Override
    public void propertyChange(
            final PropertyChangeEvent event
    ) {
        if ("state".equals(
                event.getPropertyName()
        )) {
            final String currentPage =
                    (String) event.getNewValue();

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
    public void setLogoutController(
            final LogoutController logoutController
    ) {
        this.logoutController = logoutController;
    }
}
