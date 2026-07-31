package use_case.profile;

import entity.User;
import use_case.recommendation.RecommendationInputBoundary;

/**
 * The Edit Profile Interactor. Saves profile details and refreshes personalized recommendations.
 */
public class EditProfileInteractor implements EditProfileInputBoundary {

    private final ProfileUserDataAccessInterface userDataAccessObject;
    private final EditProfileOutputBoundary profilePresenter;
    private final RecommendationInputBoundary recommendationInteractor;

    /**
     * Constructs an EditProfileInteractor instance.
     *
     * @param userDataAccessObject profile data access object
     * @param profileOutputBoundary profile output boundary presenter
     * @param recommendationInteractor interactor boundary for refreshing recommendations
     */
    public EditProfileInteractor(final ProfileUserDataAccessInterface userDataAccessObject,
                                 final EditProfileOutputBoundary profileOutputBoundary,
                                 final RecommendationInputBoundary recommendationInteractor) {
        this.userDataAccessObject = userDataAccessObject;
        this.profilePresenter = profileOutputBoundary;
        this.recommendationInteractor = recommendationInteractor;
    }

    @Override
    public void execute(final EditProfileInputData editProfileInputData) {
        final String username = this.userDataAccessObject.getCurrentUsername();
        if (username == null) {
            this.profilePresenter.prepareFailView("No user is currently logged in.");
            return;
        }

        if (editProfileInputData.getHeight() <= 0.0f || editProfileInputData.getWeight() <= 0.0f) {
            this.profilePresenter.prepareFailView("Height and weight must both be greater than zero.");
            return;
        }

        final User user = this.userDataAccessObject.get(username);
        user.setHeight(editProfileInputData.getHeight());
        user.setWeight(editProfileInputData.getWeight());
        user.setActivityLevel(editProfileInputData.getActivityLevel());
        user.setGoal(editProfileInputData.getGoal());
        user.setProfilePicturePath(editProfileInputData.getProfilePicturePath());
        user.setDateOfBirth(editProfileInputData.getDateOfBirth());
        user.setGender(editProfileInputData.getGender());
        user.setBio(editProfileInputData.getBio());
        user.setPreferredUnitSystem(editProfileInputData.getPreferredUnitSystem());
        user.setEquipment(editProfileInputData.getEquipment());
        user.setDietaryRestrictions(editProfileInputData.getDietaryRestrictions());
        user.setPreferredWorkoutDays(editProfileInputData.getPreferredWorkoutDays());
        user.setPreferredWorkoutDurationMinutes(editProfileInputData.getPreferredWorkoutDurationMinutes());
        user.setPrivacySettings(editProfileInputData.getPrivacySettings());

        this.userDataAccessObject.save(user);

        final EditProfileOutputData outputData = new EditProfileOutputData(
                user.getName(), user.getHeight(), user.getWeight(),
                user.getActivityLevel(), user.getGoal(), user.getProfilePicturePath(),
                user.getDateOfBirth(), user.getGender(), user.getBio(),
                user.getPreferredUnitSystem(), user.getEquipment(), user.getDietaryRestrictions(),
                user.getPreferredWorkoutDays(), user.getPreferredWorkoutDurationMinutes(),
                user.getPrivacySettings());
        this.profilePresenter.prepareSuccessView(outputData);

        this.recommendationInteractor.execute();
    }
}
