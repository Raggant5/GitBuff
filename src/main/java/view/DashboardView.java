package view;

import interface_adapter.dashboard.DashboardViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DashboardView extends JPanel implements PropertyChangeListener {

    private final String viewName = "dashboard";
    private final JLabel dashboardLabel;

    public DashboardView(DashboardViewModel dashboardViewModel) {

        dashboardViewModel.addPropertyChangeListener(this);
        setLayout(new BorderLayout());
        dashboardLabel = new JLabel();
        this.add(dashboardLabel, BorderLayout.CENTER);
        dashboardLabel.setText(dashboardViewModel.getState());
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        dashboardLabel.setText((String) evt.getNewValue());
    }

    public String getViewName() {
        return viewName;
    }
}