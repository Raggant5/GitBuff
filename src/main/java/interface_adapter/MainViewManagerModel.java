package interface_adapter;

/**
 * Model for the Main View Manager. Its state is the name of the View which
 * is currently active. An initial state of "" is used.
 */
public class MainViewManagerModel extends ViewModel<String> {

    public MainViewManagerModel() {
        super("main view manager");
        this.setState("");
    }

}
