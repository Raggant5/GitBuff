package use_case.profile;

/**
 * Input Boundary for actions related to editing a user's profile.
 */
public interface EditProfileInputBoundary {

    /**
     * Executes the edit profile use case.
     *
     * @param editProfileInputData input data containing updated profile details
     */
    void execute(EditProfileInputData editProfileInputData);
}
