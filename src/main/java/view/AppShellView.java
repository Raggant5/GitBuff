package view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * The view for when the user is logged into the program, combining the navbar with the active page.
 */
public class AppShellView extends JPanel {

    private final String viewName = "app shell";

    /**
     * Constructs an AppShellView instance.
     *
     * @param mainPanel main view card container
     * @param navbarView side navigation panel
     */
    public AppShellView(final JPanel mainPanel, final NavbarView navbarView) {
        this.setLayout(new BorderLayout());
        this.add(navbarView, BorderLayout.WEST);
        this.add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Gets the view name.
     *
     * @return view name string
     */
    public String getViewName() {
        return this.viewName;
    }
}

