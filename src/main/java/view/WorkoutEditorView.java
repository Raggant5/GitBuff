package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.MainViewManagerModel;
import interface_adapter.log_workout.exercise.DeleteExerciseController;
import interface_adapter.log_workout.exercise.PrepareEditExerciseController;
import interface_adapter.log_workout.workout.AddWorkoutController;
import interface_adapter.log_workout.workout.EditWorkoutController;
import interface_adapter.log_workout.workout.WorkoutEditorState;
import interface_adapter.log_workout.workout.WorkoutEditorViewModel;

public class WorkoutEditorView extends JPanel implements PropertyChangeListener {
    private final String viewName = "workout editor";
    private final String exercisesListName = "exercise list";
    private final String exerciseEditorName = "exercise editor";

    private final WorkoutEditorViewModel workoutEditorViewModel;
    private ExerciseEntryListPanel exerciseEntryListPanel;
    private AddWorkoutController addWorkoutController;
    private EditWorkoutController editWorkoutController;
    private PrepareEditExerciseController prepareEditExerciseController;
    private DeleteExerciseController deleteExerciseController;
    private JPanel exercisePanelContainer;
    private CardLayout cardLayout;

    private final ExerciseEditorView exerciseEditorView;
    private final JLabel errorLabel;

    public WorkoutEditorView(WorkoutEditorViewModel workoutEditorViewModel, ExerciseEditorView exerciseEditorView,
                             PrepareEditExerciseController prepareEditExerciseController,
                             MainViewManagerModel mainViewManagerModel) {
        this.prepareEditExerciseController = prepareEditExerciseController;
        this.workoutEditorViewModel = workoutEditorViewModel;
        this.exerciseEditorView = exerciseEditorView;
        this.workoutEditorViewModel.addPropertyChangeListener(this);
        mainViewManagerModel.addPropertyChangeListener(evt -> {
            if (!"workout editor".equals(evt.getNewValue())) {
                final WorkoutEditorState state = workoutEditorViewModel.getState();
                if (state.getShowExerciseEditor()) {
                    state.setShowExerciseEditor(false);
                    workoutEditorViewModel.firePropertyChanged();
                }
                exerciseEditorView.resetState();
            }
        });

        errorLabel = new JLabel("");
        setLayout(new BorderLayout());

        createExercisePanel();

        final JPanel buttonPanel = new JPanel(new FlowLayout());
        final JButton addExerciseButton = new JButton("Add Exercise");
        addExerciseButton.addActionListener(evt -> {
            prepareEditExerciseController.switchToAddExerciseEditor();
        });

        final JButton saveButton = new JButton("Save Workout");
        saveButton.addActionListener(evt -> {
            final WorkoutEditorState workoutEditorState = workoutEditorViewModel.getState();
            if (workoutEditorState.getEditingWorkout() == null) {
                this.addWorkoutController.execute(workoutEditorState.getExercisesForWorkout(),
                        workoutEditorState.getExercisesDeleteStage());
            }
            else {
                this.editWorkoutController.execute(workoutEditorState.getEditingWorkout(),
                        workoutEditorState.getExercisesForWorkout(), workoutEditorState.getExercisesDeleteStage());
            }
        });

        buttonPanel.add(addExerciseButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(errorLabel);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createExercisePanel() {
        exerciseEntryListPanel = new ExerciseEntryListPanel();
        cardLayout = new CardLayout();
        exercisePanelContainer = new JPanel(cardLayout);
        exercisePanelContainer.add(exerciseEntryListPanel, exercisesListName);
        exercisePanelContainer.add(exerciseEditorView, exerciseEditorName);

        add(exercisePanelContainer, BorderLayout.CENTER);
        cardLayout.show(exercisePanelContainer, exercisesListName);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final WorkoutEditorState currentState = (WorkoutEditorState) evt.getNewValue();
        exerciseEntryListPanel.setExercises(currentState.getExercisesForWorkout(), prepareEditExerciseController,
                deleteExerciseController);
        if (currentState.getShowExerciseEditor()) {
            cardLayout.show(exercisePanelContainer, exerciseEditorName);
        }
        else {
            cardLayout.show(exercisePanelContainer, exercisesListName);
        }
        errorLabel.setText(currentState.getErrorMessage());
    }

    public void setAddWorkoutController(AddWorkoutController addWorkoutController) {
        this.addWorkoutController = addWorkoutController;
    }

    public void setEditWorkoutController(EditWorkoutController editWorkoutController) {
        this.editWorkoutController = editWorkoutController;
    }

    public void setDeleteExerciseController(DeleteExerciseController deleteExerciseController) {
        this.deleteExerciseController = deleteExerciseController;
    }

    public String getViewName() {
        return viewName;
    }
}
