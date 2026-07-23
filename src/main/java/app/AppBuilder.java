package app;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.InMemoryUserDataAccessObject;
import entity.CommonUserFactory;
import entity.UserFactory;
import interface_adapter.MainViewManagerModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.nutrition.NutritionViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;

/**
 * The AppBuilder class is responsible for putting together the pieces of
 * our CA architecture; piece by piece.
 * <p/>
 * This is done by adding each View and then adding related Use Cases.
 */
// Checkstyle note: you can ignore the "Class Data Abstraction Coupling"
//                  and the "Class Fan-Out Complexity" issues for this homework; you can
//                  think about ways to refactor the code to resolve these if you decide
//                  to work with this as starter code for your final project this term.
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private final JPanel mainPanel = new JPanel();
    private final CardLayout mainCardLayout = new CardLayout();
    private final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
    private final MainViewManager mainViewManager = new MainViewManager(mainPanel, mainCardLayout, mainViewManagerModel);

    private final UserFactory userFactory = new CommonUserFactory();
    private final InMemoryUserDataAccessObject userDataAccessObject = new InMemoryUserDataAccessObject();

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginView loginView;
    private LoginViewModel loginViewModel;
    private DashboardViewModel dashboardViewModel;
    private DashboardView dashboardView;
    private WorkoutsViewModel workoutViewModel;
    private WorkoutsView workoutsView;
    private NutritionViewModel nutritionViewModel;
    private NutritionView nutritionView;
    private NavbarView navbarView;
    private AppShellView appShellView;



    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        mainPanel.setLayout(mainCardLayout);
    }

    /**
     * Adds the Signup View to the application.
     * @return this builder
     */
    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    /**
     * Adds the Login View to the application.
     * @return this builder
     */
    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    /**
     * Adds the Main View to the application.
     * @return this builder
     */
    public AppBuilder addMainViews() {
        dashboardViewModel = new DashboardViewModel();
        dashboardView = new DashboardView(dashboardViewModel);
        workoutViewModel = new WorkoutsViewModel();
        workoutsView = new WorkoutsView(workoutViewModel);
        nutritionViewModel = new NutritionViewModel();
        nutritionView = new NutritionView(nutritionViewModel);
        mainPanel.add(dashboardView, dashboardView.getViewName());
        mainPanel.add(workoutsView, workoutsView.getViewName());
        mainPanel.add(nutritionView, nutritionView.getViewName());
        return this;
    }

    /**
     * Adds the Navbar View to the application.
     * @return this builder
     */
    public AppBuilder addNavbarView() {
        navbarView = new NavbarView(mainViewManagerModel);
        return this;
    }

    /**
     * Adds the Navbar + Main View to the application.
     * @return this builder
     */
    public AppBuilder addShellView() {
        appShellView = new AppShellView(mainPanel, navbarView);
        cardPanel.add(appShellView, "app shell");
        return this;
    }

    /**
     * Adds the Signup Use Case to the application.
     * @return this builder
     */
    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        final SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    /**
     * Adds the Login Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel, loginViewModel, signupViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        final LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }


    /**
     * Creates the JFrame for the application and initially sets the SignupView to be displayed.
     * @return the application
     */
    public JFrame build() {
        final JFrame application = new JFrame("GitBuff");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        cardPanel.setPreferredSize(new Dimension(1000, 700));
        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
