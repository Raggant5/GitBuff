package interface_adapter.profile;

import entity.ActivityLevel;
import entity.FitnessGoal;
import use_case.profile.EditProfileInputBoundary;
import use_case.profile.EditProfileInputData;

/**
 * The controller for the Edit Profile Use Case.
 */
public class ProfileController {

    private final EditProfileInputBoundary editProfileUseCaseInteractor;

    public ProfileController(EditProfileInputBoundary editProfileUseCaseInteractor) {
        this.editProfileUseCaseInteractor = editProfileUseCaseInteractor;
    }

    /**
     * Executes the Edit Profile Use Case.
     * @param height the user's height, in metres
     * @param weight the user's weight, in kilograms
     * @param activityLevel the user's typical activity level
     * @param goal the user's fitness goal
     * @param profilePicturePath the file path of the user's chosen profile picture, or null
     */
    public void execute(float height, float weight, ActivityLevel activityLevel,
                         FitnessGoal goal, String profilePicturePath) {
        final EditProfileInputData inputData = new EditProfileInputData(
                height, weight, activityLevel, goal, profilePicturePath);
        editProfileUseCaseInteractor.execute(inputData);
    }
}
