package interface_adapter.share;

/**
 * State object for Share Progress modal.
 */
public class ShareProgressState {

    private String previewText = "";
    private String profilePicturePath = "";
    private String statusMessage = "";
    private boolean isSuccess = false;

    public String getPreviewText() {
        return this.previewText;
    }

    public void setPreviewText(final String previewText) {
        this.previewText = previewText;
    }

    public String getProfilePicturePath() {
        return this.profilePicturePath;
    }

    public void setProfilePicturePath(final String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public void setStatusMessage(final String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(final boolean success) {
        this.isSuccess = success;
    }
}
