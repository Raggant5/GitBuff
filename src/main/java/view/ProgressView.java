package view;

import javax.swing.JPanel;

/**
 * The View for displaying user fitness progress over time.
 */
public class ProgressView extends JPanel {

    private final String viewName = "progress";

    /**
     * Gets the view name.
     *
     * @return view name string
     */
    public String getViewName() {
        return this.viewName;
    }
}
