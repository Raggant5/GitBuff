package interface_adapter.share;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import use_case.share.ShareProgressOutputData;

class ShareProgressPresenterTest {

    @Test
    void preparePreviewViewPopulatesPreviewState() {
        final ShareProgressViewModel viewModel = new ShareProgressViewModel();
        final ShareProgressPresenter presenter = new ShareProgressPresenter(viewModel);

        presenter.preparePreviewView(new ShareProgressOutputData("Report text", "/tmp/pic.png"));

        assertEquals("Report text", viewModel.getState().getPreviewText());
        assertEquals("/tmp/pic.png", viewModel.getState().getProfilePicturePath());
        assertEquals("", viewModel.getState().getStatusMessage());
        assertFalse(viewModel.getState().isSuccess());
    }

    @Test
    void prepareSendSuccessViewSetsSuccessStatus() {
        final ShareProgressViewModel viewModel = new ShareProgressViewModel();
        final ShareProgressPresenter presenter = new ShareProgressPresenter(viewModel);

        presenter.prepareSendSuccessView("Progress shared successfully with friend@example.com!");

        assertEquals("Progress shared successfully with friend@example.com!", viewModel.getState().getStatusMessage());
        assertTrue(viewModel.getState().isSuccess());
    }

    @Test
    void prepareFailViewSetsFailureStatus() {
        final ShareProgressViewModel viewModel = new ShareProgressViewModel();
        final ShareProgressPresenter presenter = new ShareProgressPresenter(viewModel);

        presenter.prepareFailView("No user is currently logged in.");

        assertEquals("No user is currently logged in.", viewModel.getState().getStatusMessage());
        assertFalse(viewModel.getState().isSuccess());
    }
}
