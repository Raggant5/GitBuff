package app;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.SQLiteMealDataAccessObject;
import data_access.AiWorkoutDataAccessObject;
import data_access.InMemoryDataAccessObject;
import data_access.MockSearchFoodDataAccessObject;
import data_access.SQLiteUserDataAccessObject;
import data_access.SearchFoodDataAccessObject;
import data_access.SpoonacularMealRecommendationDataAccessObject;
import entity.CommonUserFactory;
import entity.ExercisePerformedFactory;
import entity.FoodEntryFactory;
import entity.LoggedWorkoutFactory;
import entity.MealFactory;
import entity.UserFactory;
import interface_adapter.MainViewManagerModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.log_workout.exercise.AddExerciseController;
import interface_adapter.log_workout.exercise.AddExercisePresenter;
import interface_adapter.log_workout.exercise.DeleteExerciseController;
import interface_adapter.log_workout.exercise.DeleteExercisePresenter;
import interface_adapter.log_workout.exercise.EditExerciseController;
import interface_adapter.log_workout.exercise.EditExercisePresenter;
import interface_adapter.log_workout.exercise.ExerciseEditorViewModel;
import interface_adapter.log_workout.exercise.PrepareEditExerciseController;
import interface_adapter.log_workout.exercise.PrepareEditExercisePresenter;
import interface_adapter.log_workout.workout.AddWorkoutController;
import interface_adapter.log_workout.workout.AddWorkoutPresenter;
import interface_adapter.log_workout.workout.DeleteWorkoutController;
import interface_adapter.log_workout.workout.DeleteWorkoutPresenter;
import interface_adapter.log_workout.workout.EditWorkoutController;
import interface_adapter.log_workout.workout.EditWorkoutPresenter;
import interface_adapter.log_workout.workout.PrepareEditWorkoutController;
import interface_adapter.log_workout.workout.PrepareEditWorkoutPresenter;
import interface_adapter.log_workout.workout.ViewWorkoutsViewModel;
import interface_adapter.log_workout.workout.WorkoutEditorViewModel;
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
import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedInputBoundary;
import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedInteractor;
import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedOutputBoundary;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseDataAccessInterface;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseInputBoundary;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseInteractor;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseOutputBoundary;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseDataAccessInterface;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseInputBoundary;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseInteractor;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseOutputBoundary;
import use_case.log_workout.exercise_performed.prepare_edit_exercise.PrepareEditExerciseInputBoundary;
import use_case.log_workout.exercise_performed.prepare_edit_exercise.PrepareEditExerciseInteractor;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutInputBoundary;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutInteractor;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutOutputBoundary;
import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutInputBoundary;
import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutInteractor;
import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutOutputBoundary;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutInputBoundary;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutInteractor;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutOutputBoundary;
import use_case.log_workout.logged_workout.get_workouts.ViewWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutInputBoundary;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutInteractor;
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
import view.ExerciseEditorView;
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
import view.ViewWorkoutsView;
import view.WorkoutEditorView;
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

    private final InMemoryDataAccessObject userDataAccessObject =
            new InMemoryDataAccessObject();
    private final InMemoryDataAccessObject mealDataAccessObject = userDataAccessObject;

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

    private final ExercisePerformedFactory exercisePerformedFactory = new ExercisePerformedFactory();
    private final LoggedWorkoutFactory loggedWorkoutFactory = new LoggedWorkoutFactory();
    private final InMemoryDataAccessObject workoutDataAccessObject = userDataAccessObject;
    private final AddWorkoutDataAccessInterface addWorkoutDataAccessObject = workoutDataAccessObject;
    private final ViewWorkoutDataAccessInterface viewWorkoutsDataAccessObject = workoutDataAccessObject;
    private final EditWorkoutDataAccessInterface editWorkoutDataAccessObject = workoutDataAccessObject;
    private final EditExerciseDataAccessInterface editExerciseDataAccessObject = workoutDataAccessObject;
    private final DeleteWorkoutDataAccessInterface deleteWorkoutDataAccessObject = workoutDataAccessObject;
    private final DeleteExerciseDataAccessInterface deleteExerciseDataAccessObject = workoutDataAccessObject;
    private WorkoutEditorViewModel workoutEditorViewModel;
    private ExerciseEditorViewModel exerciseEditorViewModel;
    private ViewWorkoutsViewModel viewWorkoutsViewModel;
    private ViewWorkoutsView viewWorkoutsView;
    private WorkoutEditorView workoutEditorView;
    private ExerciseEditorView exerciseEditorView;

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

        this.workoutEditorViewModel = new WorkoutEditorViewModel();
        this.exerciseEditorViewModel = new ExerciseEditorViewModel();
        this.exerciseEditorView = new ExerciseEditorView(this.exerciseEditorViewModel);

        final PrepareEditExercisePresenter prepareEditExercisePresenter = new PrepareEditExercisePresenter(
                exerciseEditorViewModel, workoutEditorViewModel);
        final PrepareEditExerciseInputBoundary prepareEditExerciseInteractor = new PrepareEditExerciseInteractor(
                prepareEditExercisePresenter);
        final PrepareEditExerciseController prepareEditExerciseController = new PrepareEditExerciseController(
                prepareEditExerciseInteractor);
        this.workoutEditorView = new WorkoutEditorView(workoutEditorViewModel, exerciseEditorView,
                prepareEditExerciseController, mainViewManagerModel);

        this.viewWorkoutsViewModel = new ViewWorkoutsViewModel();
        final PrepareEditWorkoutPresenter prepareEditWorkoutPresenter = new PrepareEditWorkoutPresenter(
                workoutEditorViewModel, this.mainViewManagerModel);
        final PrepareEditWorkoutInputBoundary prepareEditWorkoutInteractor = new PrepareEditWorkoutInteractor(
                prepareEditWorkoutPresenter);
        final PrepareEditWorkoutController prepareEditWorkoutController = new PrepareEditWorkoutController(
                prepareEditWorkoutInteractor);
        this.viewWorkoutsView = new ViewWorkoutsView(viewWorkoutsViewModel, prepareEditWorkoutController,
                workoutEditorViewModel, mainViewManagerModel);

        this.mainPanel.add(this.dashboardView, this.dashboardView.getViewName());
        this.mainPanel.add(this.workoutsView, this.workoutsView.getViewName());
        this.mainPanel.add(this.nutritionView, this.nutritionView.getViewName());
        this.mainPanel.add(this.profileView, this.profileView.getViewName());
        this.mainPanel.add(mealEditorView, mealEditorView.getViewName());
        this.mainPanel.add(this.viewWorkoutsView, this.viewWorkoutsView.getViewName());
        this.mainPanel.add(workoutEditorView, workoutEditorView.getViewName());
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
                this.profileViewModel, this.viewMealsViewModel, this.viewWorkoutsViewModel,
                this.recommendationController);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                this.userDataAccessObject, loginOutputBoundary, viewMealsDataAccessObject,
                viewWorkoutsDataAccessObject);

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

    /**
     * Adds the add-meal use case.
     *
     * @return this builder
     */
    public AppBuilder addAddMealUseCase() {
        final AddMealOutputBoundary addMealPresenter =
                new AddMealPresenter(
                        mealEditorViewModel,
                        viewMealsViewModel,
                        mainViewManagerModel
                );

        final AddMealInputBoundary addMealInteractor =
                new AddMealInteractor(
                        addMealPresenter,
                        addMealDataAccessObject,
                        mealFactory
                );

        final AddMealController addMealController =
                new AddMealController(
                        addMealInteractor,
                        loginViewModel
                );

        mealEditorView.setAddMealController(
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
                        mealEditorViewModel
                );

        final DeleteFoodInputBoundary deleteFoodInteractor =
                new DeleteFoodInteractor(
                        deleteFoodPresenter
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
     * Adds the Add Exercise Use Case to the application.
     * @return this builder
     */
    public AppBuilder addAddExerciseUseCase() {
        final AddExercisePerformedOutputBoundary addExercisePresenter = new AddExercisePresenter(
                workoutEditorViewModel, exerciseEditorViewModel);
        final AddExercisePerformedInputBoundary addExerciseInteractor = new AddExercisePerformedInteractor(
                addExercisePresenter, exercisePerformedFactory);
        final AddExerciseController addExerciseController = new AddExerciseController(addExerciseInteractor);
        exerciseEditorView.setAddExerciseController(addExerciseController);
        return this;
    }

    /**
     * Adds the Add Workout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addAddWorkoutUseCase() {
        final AddWorkoutOutputBoundary addWorkoutPresenter = new AddWorkoutPresenter(workoutEditorViewModel,
                viewWorkoutsViewModel, mainViewManagerModel);
        final AddWorkoutInputBoundary addWorkoutInteractor = new AddWorkoutInteractor(addWorkoutPresenter,
                addWorkoutDataAccessObject, loggedWorkoutFactory);
        final AddWorkoutController addWorkoutController = new AddWorkoutController(addWorkoutInteractor,
                loginViewModel);
        workoutEditorView.setAddWorkoutController(addWorkoutController);
        return this;
    }

    /**
     * Adds the Edit Workout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addEditWorkoutUseCase() {
        final EditWorkoutOutputBoundary editWorkoutPresenter = new EditWorkoutPresenter(viewWorkoutsViewModel,
                workoutEditorViewModel, mainViewManagerModel);
        final EditWorkoutInputBoundary editWorkoutInteractor = new EditWorkoutInteractor(editWorkoutPresenter,
                editWorkoutDataAccessObject, deleteExerciseDataAccessObject);
        final EditWorkoutController editWorkoutController = new EditWorkoutController(editWorkoutInteractor);
        workoutEditorView.setEditWorkoutController(editWorkoutController);
        return this;
    }

    /**
     * Adds the Edit Exercise Use Case to the application.
     * @return this builder
     */
    public AppBuilder addEditExerciseUseCase() {
        final EditExerciseOutputBoundary editExercisePresenter = new EditExercisePresenter(workoutEditorViewModel,
                exerciseEditorViewModel);
        final EditExerciseInputBoundary editExerciseInteractor = new EditExerciseInteractor(editExercisePresenter,
                editExerciseDataAccessObject);
        final EditExerciseController editExerciseController = new EditExerciseController(editExerciseInteractor);
        exerciseEditorView.setEditExerciseController(editExerciseController);
        return this;
    }

    /**
     * Adds the Delete Workout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addDeleteWorkoutUseCase() {
        final DeleteWorkoutOutputBoundary deleteWorkoutPresenter = new DeleteWorkoutPresenter(viewWorkoutsViewModel);
        final DeleteWorkoutInputBoundary deleteWorkoutInteractor = new DeleteWorkoutInteractor(
                deleteWorkoutPresenter, deleteWorkoutDataAccessObject);
        final DeleteWorkoutController deleteWorkoutController = new DeleteWorkoutController(deleteWorkoutInteractor);
        viewWorkoutsView.setDeleteWorkoutController(deleteWorkoutController);
        return this;
    }

    /**
     * Adds the Delete Exercise Use Case to the application.
     * @return this builder
     */
    public AppBuilder addDeleteExerciseUseCase() {
        final DeleteExerciseOutputBoundary deleteExercisePresenter =
                new DeleteExercisePresenter(workoutEditorViewModel);
        final DeleteExerciseInputBoundary deleteExerciseInteractor = new DeleteExerciseInteractor(
                deleteExercisePresenter);
        final DeleteExerciseController deleteExerciseController = new DeleteExerciseController(
                deleteExerciseInteractor);
        workoutEditorView.setDeleteExerciseController(deleteExerciseController);
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
