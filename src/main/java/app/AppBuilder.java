package app;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.AiWorkoutDataAccessObject;
import data_access.SQLiteUserDataAccessObject;
import entity.CommonUserFactory;
import entity.UserFactory;
import interface_adapter.MainViewManagerModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.nutrition.NutritionViewModel;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfilePresenter;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.recommendation.RecommendationController;
import interface_adapter.recommendation.RecommendationPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.profile.EditProfileInputBoundary;
import use_case.profile.EditProfileInteractor;
import use_case.profile.EditProfileOutputBoundary;
import use_case.recommendation.AiWorkoutDataAccessInterface;
import use_case.recommendation.RecommendationInputBoundary;
import use_case.recommendation.RecommendationInteractor;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.AppShellView;
import view.DashboardView;
import view.LoginView;
import view.MainViewManager;
import view.NavbarView;
import view.NutritionView;
import view.ProfileView;
import view.SignupView;
import view.ViewManager;
import view.WorkoutsView;

/**
 * The AppBuilder class is responsible for assembling Clean Architecture components step by step.
 */
public class AppBuilder {

    private static final int APP_WIDTH = 1000;
    private static final int APP_HEIGHT = 700;
    private static final String DEFAULT_AI_KEY = "API KEY GOES HERE";

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();

    private final JPanel mainPanel = new JPanel();
    private final CardLayout mainCardLayout = new CardLayout();
    private final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();

    private final UserFactory userFactory = new CommonUserFactory();
    private final SQLiteUserDataAccessObject userDataAccessObject = new SQLiteUserDataAccessObject();

    private final AiWorkoutDataAccessInterface aiWorkoutDao =
            new AiWorkoutDataAccessObject();

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
    private ProfileViewModel profileViewModel;
    private ProfileView profileView;
    private NavbarView navbarView;
    private AppShellView appShellView;
    private RecommendationController recommendationController;
    private RecommendationInputBoundary recommendationInteractor;

    /**
     * Constructs the AppBuilder instance, sets panel layouts, and wires view managers.
     */
    public AppBuilder() {
        this.cardPanel.setLayout(this.cardLayout);
        this.mainPanel.setLayout(this.mainCardLayout);

        new ViewManager(this.cardPanel, this.cardLayout, this.viewManagerModel);
        new MainViewManager(this.mainPanel, this.mainCardLayout, this.mainViewManagerModel);
    }

    /**
     * Adds the Signup View to the application.
     *
     * @return this builder
     */
    public AppBuilder addSignupView() {
        this.signupViewModel = new SignupViewModel();
        this.signupView = new SignupView(this.signupViewModel);
        this.cardPanel.add(this.signupView, this.signupView.getViewName());
        return this;
    }

    /**
     * Adds the Login View to the application.
     *
     * @return this builder
     */
    public AppBuilder addLoginView() {
        this.loginViewModel = new LoginViewModel();
        this.loginView = new LoginView(this.loginViewModel);
        this.cardPanel.add(this.loginView, this.loginView.getViewName());
        return this;
    }

    /**
     * Adds the Main Views to the application.
     *
     * @return this builder
     */
    public AppBuilder addMainViews() {
        this.dashboardViewModel = new DashboardViewModel();
        this.dashboardView = new DashboardView(this.dashboardViewModel);
        this.workoutViewModel = new WorkoutsViewModel();
        this.workoutsView = new WorkoutsView(this.workoutViewModel);
        this.nutritionViewModel = new NutritionViewModel();
        this.nutritionView = new NutritionView(this.nutritionViewModel);
        this.profileViewModel = new ProfileViewModel();
        this.profileView = new ProfileView(this.profileViewModel);

        this.mainPanel.add(this.dashboardView, this.dashboardView.getViewName());
        this.mainPanel.add(this.workoutsView, this.workoutsView.getViewName());
        this.mainPanel.add(this.nutritionView, this.nutritionView.getViewName());
        this.mainPanel.add(this.profileView, this.profileView.getViewName());
        return this;
    }

    /**
     * Adds the Navbar View to the application.
     *
     * @return this builder
     */
    public AppBuilder addNavbarView() {
        this.navbarView = new NavbarView(this.mainViewManagerModel, this.viewManagerModel, this.profileViewModel);
        return this;
    }

    /**
     * Adds the App Shell View to the application.
     *
     * @return this builder
     */
    public AppBuilder addShellView() {
        this.appShellView = new AppShellView(this.mainPanel, this.navbarView);
        this.cardPanel.add(this.appShellView, "app shell");
        return this;
    }

    /**
     * Adds the Signup Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(this.viewManagerModel,
                this.signupViewModel, this.loginViewModel, this.profileViewModel, this.mainViewManagerModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                this.userDataAccessObject, signupOutputBoundary, this.userFactory);

        final SignupController controller = new SignupController(userSignupInteractor);
        this.signupView.setSignupController(controller);
        return this;
    }

    /**
     * Adds the Login Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(
                this.viewManagerModel, this.loginViewModel, this.signupViewModel,
                this.profileViewModel, this.recommendationController);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                this.userDataAccessObject, loginOutputBoundary);

        final LoginController loginController = new LoginController(loginInteractor);
        this.loginView.setLoginController(loginController);
        return this;
    }

    /**
     * Adds the Recommendation Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addRecommendationUseCase() {
        final RecommendationOutputBoundary recommendationOutputBoundary = new RecommendationPresenter(
                this.nutritionViewModel, this.workoutViewModel);
        this.recommendationInteractor = new RecommendationInteractor(
                this.userDataAccessObject, recommendationOutputBoundary, this.aiWorkoutDao);
        this.recommendationController = new RecommendationController(this.recommendationInteractor);
        this.nutritionView.setRecommendationController(this.recommendationController);
        this.workoutsView.setRecommendationController(this.recommendationController);
        return this;
    }

    /**
     * Adds the Edit Profile Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addProfileUseCase() {
        final EditProfileOutputBoundary profileOutputBoundary = new ProfilePresenter(this.profileViewModel);
        final EditProfileInputBoundary editProfileInteractor = new EditProfileInteractor(
                this.userDataAccessObject, profileOutputBoundary, this.recommendationInteractor);

        final ProfileController profileController = new ProfileController(editProfileInteractor);
        this.profileView.setProfileController(profileController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(this.viewManagerModel,
                this.profileViewModel, this.loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(this.userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        this.navbarView.setLogoutController(logoutController);
        return this;
    }

    /**
     * Creates the JFrame for the application and sets initial view.
     *
     * @return the application frame
     */
    public JFrame build() {
        final JFrame application = new JFrame("GitBuff");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.cardPanel.setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
        application.add(this.cardPanel);

        this.viewManagerModel.setState(this.signupView.getViewName());
        this.viewManagerModel.firePropertyChanged();

        return application;
    }
}
