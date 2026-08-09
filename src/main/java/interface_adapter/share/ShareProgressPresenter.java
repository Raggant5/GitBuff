package interface_adapter.share;

import use_case.share.ShareProgressOutputBoundary;
import use_case.share.ShareProgressOutputData;

/**
 * Presenter for Share Progress Use Case.
 */
public class ShareProgressPresenter implements ShareProgressOutputBoundary {

    private final ShareProgressViewModel viewModel;

    /**
     * Constructs a ShareProgressPresenter instance.
     *
     * @param viewModel the share progress view model.
     */
    public ShareProgressPresenter(final ShareProgressViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void preparePreviewView(final ShareProgressOutputData outputData) {
        final ShareProgressState state = this.viewModel.getState();
        state.setPreviewText(outputData.getFormattedShareText());
        state.setProfilePicturePath(outputData.getProfilePicturePath());
        state.setStatusMessage("");
        state.setSuccess(false);
        this.viewModel.firePropertyChanged();
    }

    @Override
    public void prepareSendSuccessView(final String message) {
        final ShareProgressState state = this.viewModel.getState();
        state.setStatusMessage(message);
        state.setSuccess(true);
        this.viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(final String errorMessage) {
        final ShareProgressState state = this.viewModel.getState();
        state.setStatusMessage(errorMessage);
        state.setSuccess(false);
        this.viewModel.firePropertyChanged();
    }
}
