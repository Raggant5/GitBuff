package use_case.share;

/**
 * Output data for the share progress feature.
 */
public class ShareProgressOutputData {

    private final String formattedShareText;
    private final String profilePicturePath;

    /**
     * Constructs ShareProgressOutputData instance.
     *
     * @param formattedShareText formatted text summary
     * @param profilePicturePath profile picture file path (nullable)
     */
    public ShareProgressOutputData(final String formattedShareText, final String profilePicturePath) {
        this.formattedShareText = formattedShareText;
        this.profilePicturePath = profilePicturePath;
    }

    public String getFormattedShareText() {
        return this.formattedShareText;
    }

    public String getProfilePicturePath() {
        return this.profilePicturePath;
    }
}
