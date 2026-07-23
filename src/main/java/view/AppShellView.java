package view;

import javax.swing.*;
import java.awt.*;

/**
 * The view for when the user is logged into the program, combining the navbar with the appropriate page.
 */
public class AppShellView extends JPanel {

    private final String viewName = "app shell";

    public AppShellView(JPanel mainPanel, NavbarView navbarView) {
        this.setLayout(new BorderLayout());

        this.add(navbarView, BorderLayout.WEST);
        this.add(mainPanel, BorderLayout.CENTER);
    }

    public String getViewName() { return this.viewName; }
}

