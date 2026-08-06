package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.log_workout.exercise.DeleteExerciseController;
import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.log_workout.exercise.PrepareEditExerciseController;

public class ExerciseEntryPanel extends JPanel {
    private final String kmUnit = " km";
    private final String minsUnit = " min";

    public ExerciseEntryPanel(ExercisePerformedDisplayData exercisePerformed,
                              PrepareEditExerciseController prepareEditExerciseController,
                              DeleteExerciseController deleteExerciseController) {
        setLayout(new BorderLayout());
        final JLabel nameLabel = new JLabel("Name: " + exercisePerformed.getExerciseName());
        final JLabel durationLabel = new JLabel("Duration: " + exercisePerformed.getDurationMins() + minsUnit);

        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        infoPanel.add(nameLabel);

        if (exercisePerformed.getIsCardio()) {
            final JLabel distanceLabel = new JLabel("Distance: " + exercisePerformed.getDistanceKm() + kmUnit);
            infoPanel.add(distanceLabel);
        }
        else {
            final JLabel setsLabel = new JLabel("Sets: " + exercisePerformed.getSets());
            final JLabel repsLabel = new JLabel("Reps: " + exercisePerformed.getReps());
            final JLabel weightLabel = new JLabel("Weight: " + exercisePerformed.getWeight());
            infoPanel.add(setsLabel);
            infoPanel.add(repsLabel);
            infoPanel.add(weightLabel);
        }

        infoPanel.add(durationLabel);

        final JButton editButton = new JButton("Edit");
        final JButton deleteButton = new JButton("Delete");

        editButton.addActionListener(evt -> {
            prepareEditExerciseController.execute(exercisePerformed.getEntity());
        });

        deleteButton.addActionListener(evt -> {
            deleteExerciseController.execute(exercisePerformed.getEntity());
        });

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
