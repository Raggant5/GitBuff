package use_case.profile;

import entity.User;
import use_case.recommendation.RecommendationInputBoundary;

/**
 * The Edit Profile Interactor. Saves the current user's profile details and
 * refreshes their personalized recommendations.
 */
public class EditProfileInteractor implements EditProfileInputBoundary {

    private final ProfileUserDataAccessInterface userDataAccessObject;
    private final EditProfileOutputBoundary profilePresenter;
    private final RecommendationInputBoundary recommendationInteractor;

    public EditProfileInteractor(ProfileUserDataAccessInterface userDataAccessObject,
                                  EditProfileOutputBoundary profileOutputBoundary,
                                  RecommendationInputBoundary recommendationInteractor) {
        this.userDataAccessObject = userDataAccessObject;
        this.profilePresenter = profileOutputBoundary;
        this.recommendationInteractor = recommendationInteractor;
    }

    @Override
    public void execute(EditProfileInputData editProfileInputData) {
        final String username = userDataAccessObject.getCurrentUsername();
        if (username == null) {
            profilePresenter.prepareFailView("No user is currently logged in.");
            return;
        }

        if (editProfileInputData.getHeight() <= 0.0f || editProfileInputData.getWeight() <= 0.0f) {
            profilePresenter.prepareFailView("Height and weight must both be greater than zero.");
            return;
        }

        final User user = userDataAccessObject.get(username);
        user.setHeight(editProfileInputData.getHeight());
        user.setWeight(editProfileInputData.getWeight());
        user.setActivityLevel(editProfileInputData.getActivityLevel());
        user.setGoal(editProfileInputData.getGoal());
        user.setProfilePicturePath(editProfileInputData.getProfilePicturePath());
        userDataAccessObject.save(user);

        final EditProfileOutputData outputData = new EditProfileOutputData(
                user.getName(), user.getHeight(), user.getWeight(),
                user.getActivityLevel(), user.getGoal(), user.getProfilePicturePath());
        profilePresenter.prepareSuccessView(outputData);

        recommendationInteractor.execute();
    }
}
