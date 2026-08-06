package interface_adapter.profile;

import use_case.profile.EditProfileInputBoundary;
import use_case.profile.EditProfileInputData;

/**
 * The controller for the Edit Profile Use Case.
 */
public class ProfileController {

    private final EditProfileInputBoundary editProfileUseCaseInteractor;

    /**
     * Constructs a ProfileController instance.
     *
     * @param editProfileUseCaseInteractor interactor boundary for profile modifications
     */
    public ProfileController(final EditProfileInputBoundary editProfileUseCaseInteractor) {
        this.editProfileUseCaseInteractor = editProfileUseCaseInteractor;
    }

    /**
     * Executes the Edit Profile Use Case.
     *
     * @param inputData the profile fields to save, already built by the caller
     */
    public void execute(final EditProfileInputData inputData) {
        this.editProfileUseCaseInteractor.execute(inputData);
    }
}
