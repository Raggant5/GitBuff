package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.log_workout.exercise.ExercisePerformedDisplayData;
import interface_adapter.log_workout.workout.DeleteWorkoutController;
import interface_adapter.log_workout.workout.LoggedWorkoutDisplayData;
import interface_adapter.log_workout.workout.PrepareEditWorkoutController;

public class WorkoutPanel extends JPanel {

    public WorkoutPanel(LoggedWorkoutDisplayData workout, PrepareEditWorkoutController prepareEditWorkoutController,
                        DeleteWorkoutController deleteWorkoutController) {

        this.setLayout(new BorderLayout());
        final JLabel dateLabel = new JLabel("Date: " + workout.getDate());
        final JLabel exerciseCountLabel = new JLabel("Exercises: " + workout.getExercises().size());
        final JLabel durationLabel = new JLabel("Duration: " + formatDuration(totalDurationMins(workout)));

        final JPanel workoutInfoPanel = new JPanel();
        workoutInfoPanel.setLayout(new FlowLayout());
        workoutInfoPanel.add(dateLabel);
        workoutInfoPanel.add(exerciseCountLabel);
        workoutInfoPanel.add(durationLabel);
        this.add(workoutInfoPanel, BorderLayout.NORTH);

        final JPanel buttonPanel = new JPanel();
        final JButton editButton = new JButton("Edit Workout");
        final JButton deleteButton = new JButton("Delete Workout");

        editButton.addActionListener(evt -> {
            prepareEditWorkoutController.execute(workout.getId());
        });

        deleteButton.addActionListener(evt -> {
            deleteWorkoutController.execute(workout.getId());
        });

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private static double totalDurationMins(LoggedWorkoutDisplayData workout) {
        double total = 0;
        for (ExercisePerformedDisplayData exercise : workout.getExercises()) {
            total += exercise.getDurationMins();
        }
        return total;
    }

    private static String formatDuration(double totalMinutes) {
        final int minutesPerHour = 60;
        final int roundedMinutes = (int) Math.round(totalMinutes);
        final int hours = roundedMinutes / minutesPerHour;
        final int minutes = roundedMinutes % minutesPerHour;
        final String result;
        if (hours > 0) {
            result = hours + "h " + minutes + "m";
        }
        else {
            result = minutes + "m";
        }
        return result;
    }
}
