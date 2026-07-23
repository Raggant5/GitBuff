package view;

import interface_adapter.workouts.WorkoutsViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WorkoutsView extends JPanel implements PropertyChangeListener {

    private final String viewName = "workouts";
    private final JLabel workoutLabel;

    public WorkoutsView(WorkoutsViewModel workoutsViewModel) {

        workoutsViewModel.addPropertyChangeListener(this);
        setLayout(new BorderLayout());
        workoutLabel = new JLabel();
        this.add(workoutLabel, BorderLayout.CENTER);
        workoutLabel.setText(workoutsViewModel.getState());
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        workoutLabel.setText((String) evt.getNewValue());
    }
    public String getViewName() {
        return viewName;
    }
}