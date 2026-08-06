package view;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import entity.ExercisePerformed;
import interface_adapter.log_workout.exercise.DeleteExerciseController;
import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.log_workout.exercise.PrepareEditExerciseController;

public class ExerciseEntryListPanel extends JPanel {

    public ExerciseEntryListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * Refreshes the list of exercises performed displayed on the screen given a new list.
     *
     * @param exercisesList the list of new exercises performed to display
     * @param prepareEditExerciseController executes the Prepare Switch to Edit Exercise View Use Case
     * @param deleteExerciseController executes the Delete Exercise Use Case
     */
    public void setExercises(List<ExercisePerformed> exercisesList,
                             PrepareEditExerciseController prepareEditExerciseController,
                             DeleteExerciseController deleteExerciseController) {
        removeAll();
        if (exercisesList != null) {
            for (ExercisePerformed exercisePerformed : exercisesList) {
                this.add(new ExerciseEntryPanel(new ExercisePerformedDisplayData(exercisePerformed),
                        prepareEditExerciseController, deleteExerciseController));
            }
        }
        revalidate();
        repaint();
    }
}
