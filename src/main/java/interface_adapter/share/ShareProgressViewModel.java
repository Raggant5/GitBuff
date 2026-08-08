package interface_adapter.share;

import interface_adapter.ViewModel;

/**
 * ViewModel for Share Progress feature.
 */
public class ShareProgressViewModel extends ViewModel<ShareProgressState> {

    public ShareProgressViewModel() {
        super("share progress");
        setState(new ShareProgressState());
    }
}
