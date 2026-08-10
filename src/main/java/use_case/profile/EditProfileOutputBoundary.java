package use_case.profile;

/**
 * The output boundary for the Edit Profile Use Case.
 */
public interface EditProfileOutputBoundary {

    /**
     * Prepares the success view for the Edit Profile Use Case.
     *
     * @param outputData output data containing updated profile details.
     */
    void prepareSuccessView(EditProfileOutputData outputData);

    /**
     * Prepares the failure view for the Edit Profile Use Case.
     *
     * @param errorMessage explanation of failure cause.
     */
    void prepareFailView(String errorMessage);
}
