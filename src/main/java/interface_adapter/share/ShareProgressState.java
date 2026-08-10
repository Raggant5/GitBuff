package interface_adapter.share;

/**
 * State object for Share Progress modal.
 */
public class ShareProgressState {

    private String previewText = "";
    private String profilePicturePath = "";
    private String statusMessage = "";
    private boolean isSuccess;

    /**
     * Gets preview text.
     *
     * @return preview text string.
     */
    public String getPreviewText() {
        return this.previewText;
    }

    /**
     * Sets preview text.
     *
     * @param previewText preview text string.
     */
    public void setPreviewText(final String previewText) {
        this.previewText = previewText;
    }

    /**
     * Gets profile picture path.
     *
     * @return file path string.
     */
    public String getProfilePicturePath() {
        return this.profilePicturePath;
    }

    /**
     * Sets profile picture path.
     *
     * @param profilePicturePath file path string.
     */
    public void setProfilePicturePath(final String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    /**
     * Gets status message.
     *
     * @return status message string.
     */
    public String getStatusMessage() {
        return this.statusMessage;
    }

    /**
     * Sets status message.
     *
     * @param statusMessage status message string.
     */
    public void setStatusMessage(final String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * Gets success flag.
     *
     * @return true if action succeeded, false otherwise.
     */
    public boolean isSuccess() {
        return this.isSuccess;
    }

    /**
     * Sets success flag.
     *
     * @param success success boolean flag.
     */
    public void setSuccess(final boolean success) {
        this.isSuccess = success;
    }
}
