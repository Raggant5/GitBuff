package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.MainViewManagerModel;
import interface_adapter.log_workout.workout.DeleteWorkoutController;
import interface_adapter.log_workout.workout.LoggedWorkoutDisplayData;
import interface_adapter.log_workout.workout.PrepareEditWorkoutController;
import interface_adapter.log_workout.workout.ViewWorkoutsState;
import interface_adapter.log_workout.workout.ViewWorkoutsViewModel;
import interface_adapter.log_workout.workout.WorkoutEditorViewModel;

public class ViewWorkoutsView extends JPanel implements PropertyChangeListener {
    private final String viewName = "view workouts";
    private final ViewWorkoutsViewModel viewWorkoutsViewModel;
    private final PrepareEditWorkoutController prepareEditWorkoutController;
    private DeleteWorkoutController deleteWorkoutController;

    private final JPanel workoutListContainer;
    private final JLabel errorLabel;

    public ViewWorkoutsView(ViewWorkoutsViewModel viewWorkoutsViewModel,
                            PrepareEditWorkoutController prepareEditWorkoutController,
                            WorkoutEditorViewModel workoutEditorViewModel,
                            MainViewManagerModel mainViewManagerModel) {

        this.prepareEditWorkoutController = prepareEditWorkoutController;
        this.viewWorkoutsViewModel = viewWorkoutsViewModel;
        this.viewWorkoutsViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        final JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        final JLabel titleLabel = new JLabel("View Workouts");
        errorLabel = new JLabel("");
        final JButton addWorkoutButton = new JButton("Add Workout");

        addWorkoutButton.addActionListener(evt -> {
            workoutEditorViewModel.getState().reset();
            workoutEditorViewModel.firePropertyChanged();
            mainViewManagerModel.setState("workout editor");
            mainViewManagerModel.firePropertyChanged();
        });

        headerPanel.add(titleLabel);
        headerPanel.add(addWorkoutButton);
        headerPanel.add(errorLabel);

        add(headerPanel, BorderLayout.NORTH);

        workoutListContainer = new JPanel();
        workoutListContainer.setLayout(new BoxLayout(workoutListContainer, BoxLayout.Y_AXIS));
        add(workoutListContainer, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ViewWorkoutsState state = (ViewWorkoutsState) evt.getNewValue();
        displayWorkouts(state);
    }

    private void displayWorkouts(ViewWorkoutsState state) {

        workoutListContainer.removeAll();
        errorLabel.setText(state.getError());
        for (LoggedWorkoutDisplayData workout : state.getWorkouts()) {
            workoutListContainer.add(new WorkoutPanel(workout, prepareEditWorkoutController,
                    deleteWorkoutController));
        }

        workoutListContainer.revalidate();
        workoutListContainer.repaint();
    }

    public void setDeleteWorkoutController(DeleteWorkoutController deleteWorkoutController) {
        this.deleteWorkoutController = deleteWorkoutController;
    }

    public String getViewName() {
        return viewName;
    }
}
