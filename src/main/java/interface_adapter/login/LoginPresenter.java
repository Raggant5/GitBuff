package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.nutrition.meal.ViewMealsViewModel;
import interface_adapter.profile.ProfileState;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.recommendation.RecommendationController;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signupViewModel;
    private final ProfileViewModel profileViewModel;
    private final ViewMealsViewModel mealsViewModel;
    private final RecommendationController recommendationController;

    /**
     * Constructs a LoginPresenter instance.
     *
     * @param viewManagerModel manager model for top-level view navigation
     * @param loginViewModel view model for login state
     * @param signupViewModel view model for signup state
     * @param profileViewModel view model for user profile state
     * @param recommendationController controller to trigger recommendations after login
     */
    public LoginPresenter(final ViewManagerModel viewManagerModel,
                          final LoginViewModel loginViewModel,
                          final SignupViewModel signupViewModel,
                          final ProfileViewModel profileViewModel,
                          final ViewMealsViewModel mealsViewModel,
                          final RecommendationController recommendationController) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
        this.profileViewModel = profileViewModel;
        this.mealsViewModel = mealsViewModel;
        this.recommendationController = recommendationController;
    }

    @Override
    public void prepareSuccessView(final LoginOutputData response) {
        final LoginState loginState = this.loginViewModel.getState();
        loginState.setUsername(response.getUsername());
        loginState.setLoginError(null);
        this.loginViewModel.firePropertyChanged();

        final ProfileState profileState = this.profileViewModel.getState();
        profileState.setUsername(response.getUsername());
        profileState.setHeightText(
                String.valueOf(response.getHeight())
        );
        profileState.setWeightText(
                String.valueOf(response.getWeight())
        );
        profileState.setActivityLevel(
                response.getActivityLevel()
        );
        profileState.setGoal(
                response.getGoal()
        );
        profileState.setProfilePicturePath(
                response.getProfilePicturePath()
        );

        profileState.setDateOfBirth(
                response.getDateOfBirth()
        );
        profileState.setGender(
                response.getGender()
        );
        profileState.setBio(
                response.getBio()
        );
        profileState.setPreferredUnitSystem(
                response.getPreferredUnitSystem()
        );
        profileState.setEquipment(
                response.getEquipment()
        );
        profileState.setDietaryRestrictions(
                response.getDietaryRestrictions()
        );
        profileState.setPreferredWorkoutDays(
                response.getPreferredWorkoutDays()
        );
        profileState.setPreferredWorkoutDurationMinutes(
                response.getPreferredWorkoutDurationMinutes()
        );
        profileState.setPrivacySettings(
                response.getPrivacySettings()
        );

        profileState.setProfileError(null);
        profileState.setSaveConfirmation(null);

        this.profileViewModel.firePropertyChanged();

        mealsViewModel.getState().setMeals(
                response.getMeals()
        );
        mealsViewModel.firePropertyChanged();

        recommendationController.execute();
        if (this.recommendationController != null) {
            this.recommendationController.execute();
        }

        this.viewManagerModel.setState("app shell");
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();

        loginState.setLoginError(error);
        this.loginViewModel.firePropertyChanged();
    }

    @Override
    public void switchToSignupView() {
        this.viewManagerModel.setState(this.signupViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}