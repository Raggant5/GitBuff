package app;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import interface_adapter.dashboard.DashboardPresenter;
import use_case.dashboard.DashboardInputBoundary;
import use_case.dashboard.DashboardInteractor;
import use_case.dashboard.DashboardOutputBoundary;
import data_access.SQLiteMealDataAccessObject;
import data_access.AiWorkoutDataAccessObject;
import data_access.MockSearchFoodDataAccessObject;
import data_access.SQLiteUserDataAccessObject;
import entity.CommonUserFactory;
import entity.FoodEntryFactory;
import entity.MealFactory;
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
import interface_adapter.nutrition.food.AddFoodController;
import interface_adapter.nutrition.food.AddFoodPresenter;
import interface_adapter.nutrition.food.DeleteFoodController;
import interface_adapter.nutrition.food.DeleteFoodPresenter;
import interface_adapter.nutrition.food.EditFoodController;
import interface_adapter.nutrition.food.EditFoodPresenter;
import interface_adapter.nutrition.food.FoodEditorViewModel;
import interface_adapter.nutrition.food.PrepareEditFoodController;
import interface_adapter.nutrition.food.PrepareEditFoodPresenter;
import interface_adapter.nutrition.food.SearchFoodController;
import interface_adapter.nutrition.food.SearchFoodPresenter;
import interface_adapter.nutrition.meal.AddMealController;
import interface_adapter.nutrition.meal.AddMealPresenter;
import interface_adapter.nutrition.meal.DeleteMealController;
import interface_adapter.nutrition.meal.DeleteMealPresenter;
import interface_adapter.nutrition.meal.EditMealController;
import interface_adapter.nutrition.meal.EditMealPresenter;
import interface_adapter.nutrition.meal.MealEditorViewModel;
import interface_adapter.nutrition.meal.PrepareEditMealController;
import interface_adapter.nutrition.meal.PrepareEditMealPresenter;
import interface_adapter.nutrition.meal.ViewMealsViewModel;
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
import use_case.nutrition.food.create_food.AddFoodEntryInputBoundary;
import use_case.nutrition.food.create_food.AddFoodEntryInteractor;
import use_case.nutrition.food.create_food.AddFoodEntryOutputBoundary;
import use_case.nutrition.food.delete_food.DeleteFoodDataAccessInterface;
import use_case.nutrition.food.delete_food.DeleteFoodInputBoundary;
import use_case.nutrition.food.delete_food.DeleteFoodInteractor;
import use_case.nutrition.food.delete_food.DeleteFoodOutputBoundary;
import use_case.nutrition.food.edit_food.EditFoodDataAccessInterface;
import use_case.nutrition.food.edit_food.EditFoodInputBoundary;
import use_case.nutrition.food.edit_food.EditFoodInteractor;
import use_case.nutrition.food.edit_food.EditFoodOutputBoundary;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodInputBoundary;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodInteractor;
import use_case.nutrition.food.search_food.SearchFoodDataAccessInterface;
import use_case.nutrition.food.search_food.SearchFoodInputBoundary;
import use_case.nutrition.food.search_food.SearchFoodInteractor;
import use_case.nutrition.meal.add_meal.AddMealDataAccessInterface;
import use_case.nutrition.meal.add_meal.AddMealInputBoundary;
import use_case.nutrition.meal.add_meal.AddMealInteractor;
import use_case.nutrition.meal.add_meal.AddMealOutputBoundary;
import use_case.nutrition.meal.delete_meal.DeleteMealDataAccessInterface;
import use_case.nutrition.meal.delete_meal.DeleteMealInputBoundary;
import use_case.nutrition.meal.delete_meal.DeleteMealInteractor;
import use_case.nutrition.meal.delete_meal.DeleteMealOutputBoundary;
import use_case.nutrition.meal.edit_meal.EditMealDataAccessInterface;
import use_case.nutrition.meal.edit_meal.EditMealInputBoundary;
import use_case.nutrition.meal.edit_meal.EditMealInteractor;
import use_case.nutrition.meal.edit_meal.EditMealOutputBoundary;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealInputBoundary;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealInteractor;
import use_case.profile.EditProfileInputBoundary;
import use_case.profile.EditProfileInteractor;
import use_case.profile.EditProfileOutputBoundary;
import use_case.recommendation.AiWorkoutDataAccessInterface;
import use_case.recommendation.FoodRecommendationDataAccessInterface;
import use_case.recommendation.RecommendationInputBoundary;
import use_case.recommendation.RecommendationInteractor;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.AppShellView;
import view.DashboardView;
import view.FoodEditorView;
import view.LoginView;
import view.MainViewManager;
import view.MealEditorView;
import view.NavbarView;
import view.NutritionView;
import view.ProfileView;
import view.SignupView;
import view.ViewManager;
import view.ViewMealsView;
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

    private AppShellView appShellView;

    private final JPanel mainPanel = new JPanel();
    private final CardLayout mainCardLayout = new CardLayout();
    private final MainViewManagerModel mainViewManagerModel =
            new MainViewManagerModel();
    private final MainViewManager mainViewManager =
            new MainViewManager(
                    mainPanel,
                    mainCardLayout,
                    mainViewManagerModel
            );

    private final UserFactory userFactory =
            new CommonUserFactory();

    private final SQLiteUserDataAccessObject userDataAccessObject =
            new SQLiteUserDataAccessObject();

    private final SQLiteMealDataAccessObject mealDataAccessObject =
            new SQLiteMealDataAccessObject();

    private final AiWorkoutDataAccessInterface aiWorkoutDao =
            new AiWorkoutDataAccessObject();

    private final FoodRecommendationDataAccessInterface foodRecommendationDao =
            new SpoonacularMealRecommendationDataAccessObject();

    private final SearchFoodDataAccessInterface searchFoodDataAccessObject = new MockSearchFoodDataAccessObject();

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

    private final FoodEntryFactory foodEntryFactory =
            new FoodEntryFactory();
    private final MealFactory mealFactory =
            new MealFactory();

    private final AddMealDataAccessInterface addMealDataAccessObject =
            mealDataAccessObject;

    private final ViewMealDataAccessInterface viewMealsDataAccessObject =
            mealDataAccessObject;

    private final EditMealDataAccessInterface editMealDataAccessObject =
            mealDataAccessObject;

    private final EditFoodDataAccessInterface editFoodDataAccessObject =
            mealDataAccessObject;

    private final DeleteMealDataAccessInterface deleteMealDataAccessObject =
            mealDataAccessObject;

    private final DeleteFoodDataAccessInterface deleteFoodDataAccessObject =
            mealDataAccessObject;

    private ViewMealsView viewMealsView;
    private MealEditorView mealEditorView;
    private FoodEditorView foodEditorView;
    private MealEditorViewModel mealEditorViewModel;
    private FoodEditorViewModel foodEditorViewModel;
    private ViewMealsViewModel viewMealsViewModel;

    private RecommendationController recommendationController;
    private RecommendationInputBoundary recommendationInteractor;
    private DashboardInputBoundary dashboardInteractor;

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
        this.profileViewModel = new ProfileViewModel();
        this.profileView = new ProfileView(this.profileViewModel);
        this.mealEditorViewModel = new MealEditorViewModel();
        this.foodEditorViewModel = new FoodEditorViewModel();
        this.foodEditorView = new FoodEditorView(this.foodEditorViewModel);

        final PrepareEditFoodPresenter prepareEditFoodPresenter = new PrepareEditFoodPresenter(foodEditorViewModel,
                mealEditorViewModel);
        final PrepareEditFoodInputBoundary prepareEditFoodInteractor = new PrepareEditFoodInteractor(
                prepareEditFoodPresenter);
        final PrepareEditFoodController prepareEditFoodController = new PrepareEditFoodController(
                prepareEditFoodInteractor);
        mealEditorView = new MealEditorView(mealEditorViewModel, foodEditorView, prepareEditFoodController,
                mainViewManagerModel);
        this.nutritionViewModel = new NutritionViewModel();
        this.viewMealsViewModel = new ViewMealsViewModel();
        final PrepareEditMealPresenter prepareEditMealPresenter = new PrepareEditMealPresenter(mealEditorViewModel,
                this.mainViewManagerModel);
        final PrepareEditMealInputBoundary prepareEditMealInteractor = new PrepareEditMealInteractor(
                prepareEditMealPresenter);
        final PrepareEditMealController prepareEditMealController = new PrepareEditMealController(
                prepareEditMealInteractor);
        this.viewMealsView = new ViewMealsView(viewMealsViewModel, prepareEditMealController);
        this.nutritionView = new NutritionView(nutritionViewModel, mainViewManagerModel,
                mealEditorViewModel, viewMealsView);

        this.mainPanel.add(this.dashboardView, this.dashboardView.getViewName());
        this.mainPanel.add(this.workoutsView, this.workoutsView.getViewName());
        this.mainPanel.add(this.nutritionView, this.nutritionView.getViewName());
        this.mainPanel.add(this.profileView, this.profileView.getViewName());
        this.mainPanel.add(mealEditorView, mealEditorView.getViewName());
        return this;
    }

    /**
     * Adds the Navbar View to the application.
     *
     * @return this builder
     */
    public AppBuilder addNavbarView() {
        this.navbarView = new NavbarView(
                this.mainViewManagerModel,
                this.viewManagerModel,
                this.profileViewModel,
                this.loginViewModel,
                this.dashboardInteractor
        );        return this;
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
     * Adds dashboard use case to application.
     * @return this builder
     */
    public AppBuilder addDashboardUseCase() {

        final DashboardOutputBoundary dashboardPresenter =
                new DashboardPresenter(this.dashboardViewModel);

        this.dashboardInteractor =
                new DashboardInteractor(
                        this.mealDataAccessObject,
                        dashboardPresenter
                );

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
                this.profileViewModel, this.viewMealsViewModel, this.recommendationController,
                this.dashboardInteractor);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                this.userDataAccessObject, loginOutputBoundary, viewMealsDataAccessObject);

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
                this.userDataAccessObject, recommendationOutputBoundary, this.aiWorkoutDao,
                this.foodRecommendationDao);
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
     * Adds the add-food use case.
     *
     * @return this builder
     */
    public AppBuilder addAddFoodUseCase() {
        final AddFoodEntryOutputBoundary addFoodPresenter =
                new AddFoodPresenter(
                        mealEditorViewModel,
                        foodEditorViewModel
                );

        final AddFoodEntryInputBoundary addFoodEntryInteractor =
                new AddFoodEntryInteractor(
                        addFoodPresenter,
                        foodEntryFactory
                );

        final AddFoodController addFoodController =
                new AddFoodController(
                        addFoodEntryInteractor
                );

        foodEditorView.setAddFoodController(
                addFoodController
        );

        return this;
    }

    public AppBuilder addAddMealUseCase() {
        final AddMealOutputBoundary addMealPresenter =
                new AddMealPresenter(
                        this.mealEditorViewModel,
                        this.viewMealsViewModel,
                        this.mainViewManagerModel,
                        this.dashboardInteractor
                );

        final AddMealInputBoundary addMealInteractor =
                new AddMealInteractor(
                        addMealPresenter,
                        this.addMealDataAccessObject,
                        this.mealFactory
                );

        final AddMealController addMealController =
                new AddMealController(
                        addMealInteractor,
                        this.loginViewModel, this.dashboardInteractor
                );

        this.mealEditorView.setAddMealController(
                addMealController
        );

        return this;
    }


    /**
     * Adds the edit-meal use case.
     *
     * @return this builder
     */
    public AppBuilder addEditMealUseCase() {
        final EditMealOutputBoundary editMealPresenter = new EditMealPresenter(viewMealsViewModel, mealEditorViewModel,
                mainViewManagerModel);
        final EditMealInputBoundary editMealInteractor = new EditMealInteractor(editMealPresenter,
                editMealDataAccessObject, deleteFoodDataAccessObject);
        final EditMealController editMealController = new EditMealController(editMealInteractor);
        mealEditorView.setEditMealController(editMealController);
        return this;
    }

    /**
     * Adds the edit-food use case.
     *
     * @return this builder
     */
    public AppBuilder addEditFoodUseCase() {
        final EditFoodOutputBoundary editFoodPresenter =
                new EditFoodPresenter(
                        mealEditorViewModel,
                        foodEditorViewModel
                );

        final EditFoodInputBoundary editFoodInteractor =
                new EditFoodInteractor(
                        editFoodPresenter,
                        editFoodDataAccessObject
                );

        final EditFoodController editFoodController =
                new EditFoodController(
                        editFoodInteractor
                );

        foodEditorView.setEditFoodController(
                editFoodController
        );

        return this;
    }

    /**
     * Adds the delete-meal use case.
     *
     * @return this builder
     */
    public AppBuilder addDeleteMealUseCase() {
        final DeleteMealOutputBoundary deleteMealPresenter =
                new DeleteMealPresenter(
                        viewMealsViewModel
                );

        final DeleteMealInputBoundary deleteMealInteractor =
                new DeleteMealInteractor(
                        deleteMealPresenter,
                        deleteMealDataAccessObject
                );

        final DeleteMealController deleteMealController =
                new DeleteMealController(
                        deleteMealInteractor
                );

        viewMealsView.setDeleteMealController(
                deleteMealController
        );

        return this;
    }

    /**
     * Adds the delete-food use case.
     *
     * @return this builder
     */
    public AppBuilder addDeleteFoodUseCase() {
        final DeleteFoodOutputBoundary deleteFoodPresenter =
                new DeleteFoodPresenter(
                        mealEditorViewModel,
                        viewMealsViewModel
                );

        final DeleteFoodInputBoundary deleteFoodInteractor =
                new DeleteFoodInteractor(
                        deleteFoodPresenter,
                        deleteFoodDataAccessObject
                );

        final DeleteFoodController deleteFoodController =
                new DeleteFoodController(
                        deleteFoodInteractor
                );

        mealEditorView.setDeleteFoodController(
                deleteFoodController
        );

        return this;
    }

    /**
     * Adds the Search Food Use Case to the application.
     * @return this builder
     */
    public AppBuilder addSearchFoodUseCase() {
        final SearchFoodPresenter searchFoodPresenter = new SearchFoodPresenter(foodEditorViewModel);
        final SearchFoodInputBoundary searchFoodInteractor = new SearchFoodInteractor(searchFoodDataAccessObject,
                searchFoodPresenter);
        final SearchFoodController searchFoodController = new SearchFoodController(searchFoodInteractor);
        foodEditorView.setSearchFoodController(searchFoodController);
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