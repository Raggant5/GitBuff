package use_case.profile;

import entity.User;
import use_case.DataAccessException;

/**
 * The Edit Profile Interactor. Saves profile details.
 */
public class EditProfileInteractor implements EditProfileInputBoundary {

    private final ProfileUserDataAccessInterface userDataAccessObject;
    private final EditProfileOutputBoundary profilePresenter;

    /**
     * Constructs an EditProfileInteractor instance.
     *
     * @param userDataAccessObject profile data access object.
     * @param profileOutputBoundary profile output boundary presenter.
     */
    public EditProfileInteractor(final ProfileUserDataAccessInterface userDataAccessObject,
                                 final EditProfileOutputBoundary profileOutputBoundary) {
        this.userDataAccessObject = userDataAccessObject;
        this.profilePresenter = profileOutputBoundary;
    }

    @Override
    public void execute(final EditProfileInputData editProfileInputData) {
        try {
            final String username = this.userDataAccessObject.getCurrentUsername();
            if (username == null) {
                this.profilePresenter.prepareFailView("No user is currently logged in.");
            }
            else if (editProfileInputData.getHeight() <= 0.0f || editProfileInputData.getWeight() <= 0.0f) {
                this.profilePresenter.prepareFailView("Height and weight must both be greater than zero.");
            }
            else {
                saveProfileAndNotify(username, editProfileInputData);
            }
        }
        catch (final DataAccessException exception) {
            this.profilePresenter.prepareFailView("Unable to save your profile right now. Please try again.");
        }
    }

    private void saveProfileAndNotify(final String username, final EditProfileInputData editProfileInputData) {
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

        final EditProfileOutputData outputData = new EditProfileOutputData(user);
        this.profilePresenter.prepareSuccessView(outputData);
    }
}

