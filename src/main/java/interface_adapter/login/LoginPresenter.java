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

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoginViewModel loginViewModel,
                          SignupViewModel signupViewModel,
                          ProfileViewModel profileViewModel,
                          ViewMealsViewModel mealsViewModel,
                          RecommendationController recommendationController) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
        this.profileViewModel = profileViewModel;
        this.mealsViewModel = mealsViewModel;
        this.recommendationController = recommendationController;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        final LoginState loginState = loginViewModel.getState();

        loginState.setUsername(response.getUsername());
        loginState.setLoginError(null);

        loginViewModel.firePropertyChanged();

        final ProfileState profileState = profileViewModel.getState();

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

        profileViewModel.firePropertyChanged();

        mealsViewModel.getState().setMeals(
                response.getMeals()
        );
        mealsViewModel.firePropertyChanged();

        recommendationController.execute();

        viewManagerModel.setState("app shell");
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();

        loginState.setLoginError(error);

        loginViewModel.firePropertyChanged();
    }

    @Override
    public void switchToSignupView() {
        viewManagerModel.setState(
                signupViewModel.getViewName()
        );

        viewManagerModel.firePropertyChanged();
    }
}