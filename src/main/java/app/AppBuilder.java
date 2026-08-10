package app;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.AiWorkoutDataAccessObject;
import data_access.GoogleCalendarDataAccessObject;
import data_access.GoogleCalendarServiceFactory;
import data_access.JavaMailDataAccessObject;
import data_access.SQLiteMealDataAccessObject;
import data_access.SQLiteUserDataAccessObject;
import data_access.SQLiteWorkoutDataAccessObject;
import data_access.SearchFoodDataAccessObject;
import data_access.ShareProgressUserDataAccessFacade;
import data_access.SpoonacularMealRecommendationDataAccessObject;
import entity.CommonUserFactory;
import entity.ExercisePerformedFactory;
import entity.FoodEntryFactory;
import entity.LoggedWorkoutFactory;
import entity.MealFactory;
import entity.UserFactory;
import interface_adapter.MainViewManagerModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.calendar.CalendarController;
import interface_adapter.calendar.CalendarPresenter;
import interface_adapter.calendar.CalendarState;
import interface_adapter.calendar.CalendarViewModel;
import interface_adapter.dashboard.DashboardPresenter;
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
import interface_adapter.log_workout.workout.GetWorkoutsController;
import interface_adapter.log_workout.workout.GetWorkoutsPresenter;
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
import interface_adapter.nutrition.food.ChangeServingSizeController;
import interface_adapter.nutrition.food.ChangeServingSizePresenter;
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
import interface_adapter.nutrition.meal.GetMealsController;
import interface_adapter.nutrition.meal.GetMealsPresenter;
import interface_adapter.nutrition.meal.MealEditorViewModel;
import interface_adapter.nutrition.meal.PrepareEditMealController;
import interface_adapter.nutrition.meal.PrepareEditMealPresenter;
import interface_adapter.nutrition.meal.ViewMealsViewModel;
import interface_adapter.profile.ProfileController;
import interface_adapter.profile.ProfilePresenter;
import interface_adapter.profile.ProfileSessionObserver;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.recommendation.RecommendWorkoutPlanController;
import interface_adapter.recommendation.RecommendWorkoutPlanPresenter;
import interface_adapter.recommendation.RefreshMealRecommendationsController;
import interface_adapter.recommendation.RefreshMealRecommendationsPresenter;
import interface_adapter.share.ShareProgressController;
import interface_adapter.share.ShareProgressPresenter;
import interface_adapter.share.ShareProgressViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.workouts.WorkoutScheduleLoadingObserver;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.EventBus;
import use_case.calendar.CalendarEventDataAccessInterface;
import use_case.calendar.load_events.LoadCalendarEventsInputBoundary;
import use_case.calendar.load_events.LoadCalendarEventsInteractor;
import use_case.calendar.sync_meal_event.UpdateMealCalendarEventInputBoundary;
import use_case.calendar.sync_meal_event.UpdateMealCalendarEventInteractor;
import use_case.calendar.sync_meals.SyncMealCalendarEventsInputBoundary;
import use_case.calendar.sync_meals.SyncMealCalendarEventsInteractor;
import use_case.calendar.sync_workouts.SyncWorkoutCalendarEventsInputBoundary;
import use_case.calendar.sync_workouts.SyncWorkoutCalendarEventsInteractor;
import use_case.dashboard.DashboardInputBoundary;
import use_case.dashboard.DashboardInteractor;
import use_case.dashboard.DashboardOutputBoundary;
import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedInputBoundary;
import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedInteractor;
import use_case.log_workout.exercise_performed.create_exercise.AddExercisePerformedOutputBoundary;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseInputBoundary;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseInteractor;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseOutputBoundary;
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
import use_case.log_workout.logged_workout.get_workouts.GetWorkoutsInputBoundary;
import use_case.log_workout.logged_workout.get_workouts.GetWorkoutsInteractor;
import use_case.log_workout.logged_workout.get_workouts.ViewWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutInputBoundary;
import use_case.log_workout.logged_workout.prepare_edit_workout.PrepareEditWorkoutInteractor;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.nutrition.food.change_serving_size.ChangeServingSizeInputBoundary;
import use_case.nutrition.food.change_serving_size.ChangeServingSizeInteractor;
import use_case.nutrition.food.change_serving_size.ChangeServingSizeOutputBoundary;
import use_case.nutrition.food.create_food.AddFoodEntryInputBoundary;
import use_case.nutrition.food.create_food.AddFoodEntryInteractor;
import use_case.nutrition.food.create_food.AddFoodEntryOutputBoundary;
import use_case.nutrition.food.delete_food.DeleteFoodInputBoundary;
import use_case.nutrition.food.delete_food.DeleteFoodInteractor;
import use_case.nutrition.food.delete_food.DeleteFoodOutputBoundary;
import use_case.nutrition.food.edit_food.EditFoodInputBoundary;
import use_case.nutrition.food.edit_food.EditFoodInteractor;
import use_case.nutrition.food.edit_food.EditFoodOutputBoundary;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodInputBoundary;
import use_case.nutrition.food.prepare_edit_food.PrepareEditFoodInteractor;
import use_case.nutrition.food.search_food.SearchFoodDataAccessInterface;
import use_case.nutrition.food.search_food.SearchFoodInputBoundary;
import use_case.nutrition.food.search_food.SearchFoodInteractor;
import use_case.nutrition.food.search_food.SearchFoodOutputBoundary;
import use_case.nutrition.meal.DashboardRefreshOnMealChangeObserver;
import use_case.nutrition.meal.MealCalendarSyncObserver;
import use_case.nutrition.meal.MealChangedEvent;
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
import use_case.nutrition.meal.get_meals.GetMealsInputBoundary;
import use_case.nutrition.meal.get_meals.GetMealsInteractor;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealInputBoundary;
import use_case.nutrition.meal.prepare_edit_meal.PrepareEditMealInteractor;
import use_case.profile.EditProfileInputBoundary;
import use_case.profile.EditProfileInteractor;
import use_case.profile.EditProfileOutputBoundary;
import use_case.profile.ProfileUpdatedEvent;
import use_case.profile.RecommendationRefreshOnProfileUpdateObserver;
import use_case.recommendation.AiWorkoutDataAccessInterface;
import use_case.recommendation.FoodRecommendationDataAccessInterface;
import use_case.recommendation.RecommendWorkoutPlanInputBoundary;
import use_case.recommendation.RecommendWorkoutPlanInteractor;
import use_case.recommendation.RecommendWorkoutPlanOutputBoundary;
import use_case.recommendation.RefreshMealRecommendationsInputBoundary;
import use_case.recommendation.RefreshMealRecommendationsInteractor;
import use_case.recommendation.RefreshMealRecommendationsOutputBoundary;
import use_case.recommendation.WorkoutCalendarSyncObserver;
import use_case.recommendation.WorkoutPlanGeneratedEvent;
import use_case.session.CalendarSyncObserver;
import use_case.session.DashboardRefreshObserver;
import use_case.session.RecommendationRefreshObserver;
import use_case.session.UserLoggedInEvent;
import use_case.share.ShareEmailDataAccessInterface;
import use_case.share.ShareProgressInputBoundary;
import use_case.share.ShareProgressInteractor;
import use_case.share.ShareProgressOutputBoundary;
import use_case.share.ShareProgressUserDataAccessInterface;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.AppShellView;
import view.CalendarPanel;
import view.DashboardView;
import view.ExerciseEditorView;
import view.FoodEditorView;
import view.LoginView;
import view.MainViewManager;
import view.MealEditorView;
import view.NavbarView;
import view.NutritionView;
import view.ProfileView;
import view.ShareProgressDialogView;
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

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();

    private AppShellView appShellView;

    private final JPanel mainPanel = new JPanel();
    private final CardLayout mainCardLayout = new CardLayout();
    private final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();

    private final UserFactory userFactory = new CommonUserFactory();

    private final SQLiteUserDataAccessObject userDataAccessObject = new SQLiteUserDataAccessObject();
    private final SQLiteMealDataAccessObject mealDataAccessObject = new SQLiteMealDataAccessObject();

    private final AiWorkoutDataAccessInterface aiWorkoutDao = new AiWorkoutDataAccessObject();
    private final FoodRecommendationDataAccessInterface foodRecommendationDao =
            new SpoonacularMealRecommendationDataAccessObject();

    private final SearchFoodDataAccessInterface searchFoodDataAccessObject = new SearchFoodDataAccessObject();
    private final ShareEmailDataAccessInterface emailDataAccessObject = new JavaMailDataAccessObject();

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

    private final FoodEntryFactory foodEntryFactory = new FoodEntryFactory();
    private final MealFactory mealFactory = new MealFactory();

    private final AddMealDataAccessInterface addMealDataAccessObject = mealDataAccessObject;
    private final ViewMealDataAccessInterface viewMealsDataAccessObject = mealDataAccessObject;
    private final EditMealDataAccessInterface editMealDataAccessObject = mealDataAccessObject;
    private final DeleteMealDataAccessInterface deleteMealDataAccessObject = mealDataAccessObject;

    private ViewMealsView viewMealsView;
    private MealEditorView mealEditorView;
    private FoodEditorView foodEditorView;
    private MealEditorViewModel mealEditorViewModel;
    private FoodEditorViewModel foodEditorViewModel;
    private ViewMealsViewModel viewMealsViewModel;

    private final ExercisePerformedFactory exercisePerformedFactory = new ExercisePerformedFactory();
    private final LoggedWorkoutFactory loggedWorkoutFactory = new LoggedWorkoutFactory();

    // Single shared instance of SQLiteWorkoutDataAccessObject
    private final SQLiteWorkoutDataAccessObject workoutDataAccessObject = new SQLiteWorkoutDataAccessObject();
    private final AddWorkoutDataAccessInterface addWorkoutDataAccessObject = this.workoutDataAccessObject;
    private final ViewWorkoutDataAccessInterface viewWorkoutsDataAccessObject = this.workoutDataAccessObject;
    private final EditWorkoutDataAccessInterface editWorkoutDataAccessObject = this.workoutDataAccessObject;
    private final DeleteWorkoutDataAccessInterface deleteWorkoutDataAccessObject = this.workoutDataAccessObject;

    private WorkoutEditorViewModel workoutEditorViewModel;
    private ExerciseEditorViewModel exerciseEditorViewModel;
    private ViewWorkoutsViewModel viewWorkoutsViewModel;
    private ViewWorkoutsView viewWorkoutsView;
    private WorkoutEditorView workoutEditorView;
    private ExerciseEditorView exerciseEditorView;

    private RecommendWorkoutPlanController recommendWorkoutPlanController;
    private RecommendWorkoutPlanInputBoundary recommendWorkoutPlanInteractor;
    private RefreshMealRecommendationsController refreshMealRecommendationsController;
    private RefreshMealRecommendationsInputBoundary refreshMealRecommendationsInteractor;
    private DashboardInputBoundary dashboardInteractor;

    private GetMealsController getMealsController;
    private GetWorkoutsController getWorkoutsController;

    private CalendarPanel calendarPanel;
    private CalendarViewModel calendarViewModel;
    private CalendarController calendarController;
    private CalendarEventDataAccessInterface calendarDataAccessObject;
    private CalendarPresenter calendarPresenter;
    private SyncMealCalendarEventsInputBoundary syncMealCalendarEventsInteractor;
    private SyncWorkoutCalendarEventsInputBoundary syncWorkoutCalendarEventsInteractor;
    private EventBus<MealChangedEvent> mealEventBus;

    private ShareProgressViewModel shareProgressViewModel;
    private ShareProgressController shareProgressController;
    private ShareProgressDialogView shareProgressDialogView;

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
     * Creates the calendar view model and calendar panel for the dashboard.
     *
     * @return this builder
     */
    public AppBuilder addCalendarPanel() {
        this.calendarViewModel = new CalendarViewModel();
        this.calendarViewModel.setState(new CalendarState());
        this.calendarPanel = new CalendarPanel(this.calendarViewModel);
        return this;
    }

    /**
     * Adds the calendar add, remove, load, and sync use cases to the application.
     *
     * @return this builder
     */
    public AppBuilder addCalendarUseCase() {
        this.calendarDataAccessObject = new GoogleCalendarDataAccessObject(() -> GoogleCalendarServiceFactory.create());
        this.calendarPresenter = new CalendarPresenter(this.calendarViewModel);

        final LoadCalendarEventsInputBoundary loadInteractor =
                new LoadCalendarEventsInteractor(this.calendarDataAccessObject, this.calendarPresenter);

        this.calendarController = new CalendarController(loadInteractor, this.loginViewModel);

        this.syncMealCalendarEventsInteractor = new SyncMealCalendarEventsInteractor(
                this.calendarDataAccessObject, this.viewMealsDataAccessObject, this.calendarPresenter);
        this.syncWorkoutCalendarEventsInteractor = new SyncWorkoutCalendarEventsInteractor(
                this.calendarDataAccessObject, this.calendarPresenter);

        final UpdateMealCalendarEventInputBoundary updateMealCalendarEventInteractor =
                new UpdateMealCalendarEventInteractor(this.calendarDataAccessObject, this.calendarPresenter);
        final MealCalendarSyncObserver mealCalendarSyncObserver =
                new MealCalendarSyncObserver(updateMealCalendarEventInteractor);
        this.mealEventBus = new EventBus<>();
        this.mealEventBus.subscribe(mealCalendarSyncObserver);
        this.mealEventBus.subscribe(new DashboardRefreshOnMealChangeObserver(this.dashboardInteractor));
        return this;
    }

    /**
     * Adds the Share Progress Use Case.
     *
     * @return this builder
     */
    public AppBuilder addShareProgressUseCase() {
        this.shareProgressViewModel = new ShareProgressViewModel();
        final ShareProgressOutputBoundary presenter = new ShareProgressPresenter(this.shareProgressViewModel);

        final ShareProgressUserDataAccessInterface shareDataFacade =
                new ShareProgressUserDataAccessFacade(this.userDataAccessObject, this.workoutDataAccessObject);

        final ShareProgressInputBoundary interactor = new ShareProgressInteractor(
                shareDataFacade, this.emailDataAccessObject, presenter);
        this.shareProgressController = new ShareProgressController(interactor);

        if (this.dashboardView != null) {
            this.dashboardView.setShareProgressController(this.shareProgressController);
        }
        return this;
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
        if (this.workoutViewModel != null) {
            this.loginView.setWorkoutsViewModel(this.workoutViewModel);
        }
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
        this.dashboardView = new DashboardView(this.dashboardViewModel, this.calendarPanel);
        if (this.shareProgressController != null) {
            this.dashboardView.setShareProgressController(this.shareProgressController);
        }

        this.workoutViewModel = new WorkoutsViewModel();
        this.workoutsView = new WorkoutsView(this.workoutViewModel);
        this.nutritionViewModel = new NutritionViewModel();
        this.profileViewModel = new ProfileViewModel();
        this.profileView = new ProfileView(this.profileViewModel);
        this.profileView.setWorkoutsViewModel(this.workoutViewModel);
        this.mealEditorViewModel = new MealEditorViewModel();
        this.foodEditorViewModel = new FoodEditorViewModel();
        this.foodEditorView = new FoodEditorView(this.foodEditorViewModel);

        if (this.loginView != null) {
            this.loginView.setWorkoutsViewModel(this.workoutViewModel);
        }

        final PrepareEditFoodPresenter prepareEditFoodPresenter = new PrepareEditFoodPresenter(
                foodEditorViewModel, mealEditorViewModel);
        final PrepareEditFoodInputBoundary prepareEditFoodInteractor = new PrepareEditFoodInteractor(
                prepareEditFoodPresenter);
        final PrepareEditFoodController prepareEditFoodController = new PrepareEditFoodController(
                prepareEditFoodInteractor);
        mealEditorView = new MealEditorView(mealEditorViewModel, foodEditorView, prepareEditFoodController,
                mainViewManagerModel);
        this.nutritionViewModel = new NutritionViewModel();
        this.viewMealsViewModel = new ViewMealsViewModel();
        final PrepareEditMealPresenter prepareEditMealPresenter = new PrepareEditMealPresenter(
                mealEditorViewModel, this.viewMealsViewModel, this.mainViewManagerModel);
        final PrepareEditMealInputBoundary prepareEditMealInteractor = new PrepareEditMealInteractor(
                prepareEditMealPresenter, viewMealsDataAccessObject);
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
                workoutEditorViewModel, this.viewWorkoutsViewModel, this.mainViewManagerModel);
        final PrepareEditWorkoutInputBoundary prepareEditWorkoutInteractor = new PrepareEditWorkoutInteractor(
                prepareEditWorkoutPresenter, viewWorkoutsDataAccessObject);
        final PrepareEditWorkoutController prepareEditWorkoutController = new PrepareEditWorkoutController(
                prepareEditWorkoutInteractor);
        this.viewWorkoutsView = new ViewWorkoutsView(viewWorkoutsViewModel, prepareEditWorkoutController,
                workoutEditorViewModel, mainViewManagerModel);

        final GetMealsInputBoundary getMealsInteractor = new GetMealsInteractor(
                new GetMealsPresenter(this.viewMealsViewModel), this.viewMealsDataAccessObject);
        this.getMealsController = new GetMealsController(getMealsInteractor, this.loginViewModel);

        final GetWorkoutsInputBoundary getWorkoutsInteractor = new GetWorkoutsInteractor(
                new GetWorkoutsPresenter(this.viewWorkoutsViewModel), this.viewWorkoutsDataAccessObject);
        this.getWorkoutsController = new GetWorkoutsController(getWorkoutsInteractor, this.loginViewModel);

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
        this.navbarView = new NavbarView(
                this.mainViewManagerModel,
                this.viewManagerModel,
                this.profileViewModel,
                this.loginViewModel,
                this.dashboardInteractor,
                this.calendarController,
                this.getMealsController,
                this.getWorkoutsController
        );
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
     * Adds dashboard use case to application.
     *
     * @return this builder
     */
    public AppBuilder addDashboardUseCase() {
        final DashboardOutputBoundary dashboardPresenter = new DashboardPresenter(this.dashboardViewModel);
        this.dashboardInteractor = new DashboardInteractor(this.mealDataAccessObject, dashboardPresenter);
        return this;
    }

    /**
     * Adds the Login Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addLoginUseCase() {
        final EventBus<UserLoggedInEvent> userSessionEventBus = new EventBus<>();
        userSessionEventBus.subscribe(new ProfileSessionObserver(this.profileViewModel));
        userSessionEventBus.subscribe(new WorkoutScheduleLoadingObserver(this.workoutViewModel));
        userSessionEventBus.subscribe(new CalendarSyncObserver(this.syncMealCalendarEventsInteractor));
        userSessionEventBus.subscribe(new RecommendationRefreshObserver(
                this.recommendWorkoutPlanInteractor, this.refreshMealRecommendationsInteractor));
        userSessionEventBus.subscribe(new DashboardRefreshObserver(this.dashboardInteractor));

        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(
                this.viewManagerModel, this.loginViewModel, this.signupViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                this.userDataAccessObject, loginOutputBoundary, userSessionEventBus);

        final LoginController loginController = new LoginController(loginInteractor);
        this.loginView.setLoginController(loginController);
        return this;
    }

    /**
     * Adds the Recommend Workout Plan and Refresh Meal Recommendations Use Cases to the
     * application.
     *
     * @return this builder
     */
    public AppBuilder addRecommendationUseCase() {
        final RecommendWorkoutPlanOutputBoundary recommendWorkoutPlanOutputBoundary =
                new RecommendWorkoutPlanPresenter(this.workoutViewModel);

        final EventBus<WorkoutPlanGeneratedEvent> workoutPlanEventBus = new EventBus<>();
        workoutPlanEventBus.subscribe(new WorkoutCalendarSyncObserver(this.syncWorkoutCalendarEventsInteractor));

        this.recommendWorkoutPlanInteractor = new RecommendWorkoutPlanInteractor(
                this.userDataAccessObject, recommendWorkoutPlanOutputBoundary, this.aiWorkoutDao,
                workoutPlanEventBus);
        this.recommendWorkoutPlanController = new RecommendWorkoutPlanController(this.recommendWorkoutPlanInteractor);
        this.workoutsView.setRecommendWorkoutPlanController(this.recommendWorkoutPlanController);

        final RefreshMealRecommendationsOutputBoundary refreshMealRecommendationsOutputBoundary =
                new RefreshMealRecommendationsPresenter(this.nutritionViewModel);
        this.refreshMealRecommendationsInteractor = new RefreshMealRecommendationsInteractor(
                this.userDataAccessObject, refreshMealRecommendationsOutputBoundary,
                this.foodRecommendationDao);
        this.refreshMealRecommendationsController =
                new RefreshMealRecommendationsController(this.refreshMealRecommendationsInteractor);
        this.nutritionView.setRefreshMealRecommendationsController(this.refreshMealRecommendationsController);
        return this;
    }

    /**
     * Adds the Edit Profile Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addProfileUseCase() {
        final EditProfileOutputBoundary profileOutputBoundary = new ProfilePresenter(this.profileViewModel);

        final EventBus<ProfileUpdatedEvent> profileEventBus = new EventBus<>();
        profileEventBus.subscribe(new RecommendationRefreshOnProfileUpdateObserver(
                this.recommendWorkoutPlanInteractor, this.refreshMealRecommendationsInteractor));

        final EditProfileInputBoundary editProfileInteractor = new EditProfileInteractor(
                this.userDataAccessObject, profileOutputBoundary, profileEventBus);

        final ProfileController profileController = new ProfileController(editProfileInteractor);
        profileController.setWorkoutsViewModel(this.workoutViewModel);
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
        final AddFoodEntryOutputBoundary addFoodPresenter = new AddFoodPresenter(
                mealEditorViewModel, foodEditorViewModel);
        final AddFoodEntryInputBoundary addFoodEntryInteractor = new AddFoodEntryInteractor(
                addFoodPresenter, foodEntryFactory);
        final AddFoodController addFoodController = new AddFoodController(addFoodEntryInteractor);

        foodEditorView.setAddFoodController(addFoodController);
        return this;
    }

    /**
     * Adds the add-meal use case.
     *
     * @return this builder
     */
    public AppBuilder addAddMealUseCase() {
        final AddMealOutputBoundary addMealPresenter = new AddMealPresenter(
                this.mealEditorViewModel,
                this.viewMealsViewModel,
                this.mainViewManagerModel
        );

        final AddMealInputBoundary addMealInteractor = new AddMealInteractor(
                addMealPresenter,
                this.addMealDataAccessObject,
                this.mealFactory,
                this.foodEntryFactory,
                this.mealEventBus
        );

        final AddMealController addMealController = new AddMealController(
                addMealInteractor,
                this.loginViewModel
        );

        this.mealEditorView.setAddMealController(addMealController);
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
                editMealDataAccessObject, viewMealsDataAccessObject, foodEntryFactory, this.mealEventBus);
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
        final EditFoodOutputBoundary editFoodPresenter = new EditFoodPresenter(
                mealEditorViewModel, foodEditorViewModel);
        final EditFoodInputBoundary editFoodInteractor = new EditFoodInteractor(
                editFoodPresenter, foodEntryFactory);
        final EditFoodController editFoodController = new EditFoodController(editFoodInteractor);

        foodEditorView.setEditFoodController(editFoodController);
        return this;
    }

    /**
     * Adds the change-serving-size use case.
     *
     * @return this builder
     */
    public AppBuilder addChangeServingSizeUseCase() {
        final ChangeServingSizeOutputBoundary changeServingSizePresenter = new ChangeServingSizePresenter(
                foodEditorViewModel);
        final ChangeServingSizeInputBoundary changeServingSizeInteractor = new ChangeServingSizeInteractor(
                changeServingSizePresenter);
        final ChangeServingSizeController changeServingSizeController = new ChangeServingSizeController(
                changeServingSizeInteractor);

        foodEditorView.setChangeServingSizeController(changeServingSizeController);
        return this;
    }

    /**
     * Adds the delete-meal use case.
     *
     * @return this builder
     */
    public AppBuilder addDeleteMealUseCase() {
        final DeleteMealOutputBoundary deleteMealPresenter = new DeleteMealPresenter(viewMealsViewModel);
        final DeleteMealInputBoundary deleteMealInteractor = new DeleteMealInteractor(
                deleteMealPresenter, deleteMealDataAccessObject, viewMealsDataAccessObject, this.mealEventBus);
        final DeleteMealController deleteMealController = new DeleteMealController(deleteMealInteractor);

        viewMealsView.setDeleteMealController(deleteMealController);
        return this;
    }

    /**
     * Adds the delete-food use case.
     *
     * @return this builder
     */
    public AppBuilder addDeleteFoodUseCase() {
        final DeleteFoodOutputBoundary deleteFoodPresenter = new DeleteFoodPresenter(mealEditorViewModel);
        final DeleteFoodInputBoundary deleteFoodInteractor = new DeleteFoodInteractor(deleteFoodPresenter);
        final DeleteFoodController deleteFoodController = new DeleteFoodController(deleteFoodInteractor);

        mealEditorView.setDeleteFoodController(deleteFoodController);
        return this;
    }

    /**
     * Adds the Search Food Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addSearchFoodUseCase() {
        final SearchFoodOutputBoundary searchFoodPresenter = new SearchFoodPresenter(foodEditorViewModel);
        final SearchFoodInputBoundary searchFoodInteractor = new SearchFoodInteractor(searchFoodDataAccessObject,
                searchFoodPresenter);
        final SearchFoodController searchFoodController = new SearchFoodController(searchFoodInteractor);

        foodEditorView.setSearchFoodController(searchFoodController);
        return this;
    }

    /**
     * Adds the Add Exercise Use Case to the application.
     *
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
     *
     * @return this builder
     */
    public AppBuilder addAddWorkoutUseCase() {
        final AddWorkoutOutputBoundary addWorkoutPresenter = new AddWorkoutPresenter(workoutEditorViewModel,
                viewWorkoutsViewModel, mainViewManagerModel);
        final AddWorkoutInputBoundary addWorkoutInteractor = new AddWorkoutInteractor(addWorkoutPresenter,
                addWorkoutDataAccessObject, loggedWorkoutFactory, exercisePerformedFactory);
        final AddWorkoutController addWorkoutController = new AddWorkoutController(addWorkoutInteractor,
                loginViewModel);
        workoutEditorView.setAddWorkoutController(addWorkoutController);
        return this;
    }

    /**
     * Adds the Edit Workout Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addEditWorkoutUseCase() {
        final EditWorkoutOutputBoundary editWorkoutPresenter = new EditWorkoutPresenter(viewWorkoutsViewModel,
                workoutEditorViewModel, mainViewManagerModel);
        final EditWorkoutInputBoundary editWorkoutInteractor = new EditWorkoutInteractor(editWorkoutPresenter,
                editWorkoutDataAccessObject, viewWorkoutsDataAccessObject, exercisePerformedFactory);
        final EditWorkoutController editWorkoutController = new EditWorkoutController(editWorkoutInteractor);
        workoutEditorView.setEditWorkoutController(editWorkoutController);
        return this;
    }

    /**
     * Adds the Edit Exercise Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addEditExerciseUseCase() {
        final EditExerciseOutputBoundary editExercisePresenter = new EditExercisePresenter(workoutEditorViewModel,
                exerciseEditorViewModel);
        final EditExerciseInputBoundary editExerciseInteractor = new EditExerciseInteractor(editExercisePresenter,
                exercisePerformedFactory);
        final EditExerciseController editExerciseController = new EditExerciseController(editExerciseInteractor);
        exerciseEditorView.setEditExerciseController(editExerciseController);
        return this;
    }

    /**
     * Adds the Delete Workout Use Case to the application.
     *
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
     *
     * @return this builder
     */
    public AppBuilder addDeleteExerciseUseCase() {
        final DeleteExerciseOutputBoundary deleteExercisePresenter = new DeleteExercisePresenter(
                workoutEditorViewModel);
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

        if (this.shareProgressViewModel != null) {
            this.shareProgressDialogView = new ShareProgressDialogView(application, this.shareProgressViewModel);
            if (this.shareProgressController != null) {
                this.shareProgressDialogView.setController(this.shareProgressController);
            }
        }

        this.viewManagerModel.setState(this.signupView.getViewName());
        this.viewManagerModel.firePropertyChanged();

        return application;
    }
}
