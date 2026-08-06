package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.dashboard.DashboardViewModel;

/**
 * The View for displaying user dashboard analytics and status.
 */
public class DashboardView extends JPanel implements PropertyChangeListener {

    private final String viewName = "dashboard";
    private final JLabel dashboardLabel;

    /**
     * Constructs a DashboardView instance.
     *
     * @param dashboardViewModel view model managing dashboard state
     */
    public DashboardView(final DashboardViewModel dashboardViewModel) {
        dashboardViewModel.addPropertyChangeListener(this);
        this.setLayout(new BorderLayout());
        this.dashboardLabel = new JLabel();
        this.add(this.dashboardLabel, BorderLayout.CENTER);
        this.dashboardLabel.setText(dashboardViewModel.getState());
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        this.dashboardLabel.setText((String) evt.getNewValue());
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