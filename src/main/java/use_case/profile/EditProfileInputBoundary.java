package use_case.profile;

/**
 * Input Boundary for actions which are related to editing a user's profile.
 */
public interface EditProfileInputBoundary {

    /**
     * Executes the edit profile use case.
     * @param editProfileInputData the input data
     */
    void execute(EditProfileInputData editProfileInputData);
}
