package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.log_workout.exercise.AddExerciseController;
import interface_adapter.log_workout.exercise.EditExerciseController;
import interface_adapter.log_workout.exercise.ExerciseEditorState;
import interface_adapter.log_workout.exercise.ExerciseEditorViewModel;
import interface_adapter.log_workout.exercise.StrengthDetailsDisplayData;

public class ExerciseEditorView extends JPanel implements PropertyChangeListener {

    private AddExerciseController addExerciseController;
    private EditExerciseController editExerciseController;
    private final ExerciseEditorViewModel exerciseEditorViewModel;

    private JTextField exerciseNameField;
    private JTextField setsField;
    private JTextField repsField;
    private JTextField weightField;
    private JTextField durationField;
    private JTextField distanceField;
    private JCheckBox cardioCheckBox;

    private JLabel setsErrorLabel;
    private JLabel repsErrorLabel;
    private JLabel weightErrorLabel;
    private JLabel durationErrorLabel;
    private JLabel distanceErrorLabel;

    private JLabel submitErrorLabel;
    private JPanel formPanel;

    private boolean isUpdatingFromState;

    public ExerciseEditorView(ExerciseEditorViewModel exerciseEditorViewModel) {
        this.exerciseEditorViewModel = exerciseEditorViewModel;
        this.exerciseEditorViewModel.addPropertyChangeListener(this);
        submitErrorLabel = new JLabel("");
        submitErrorLabel.setForeground(Color.RED);
        final int formRowCount = 7;
        final int formColCount = 3;
        formPanel = new JPanel(new GridLayout(formRowCount, formColCount));
        createFormPanel();

        addListeners();

        final JButton saveButton = new JButton("Save Exercise");
        saveButton.addActionListener(evt -> {
            saveExercise();
        });

        final JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(saveButton);
        bottomPanel.add(submitErrorLabel);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JLabel createFieldErrorLabel() {
        final JLabel label = new JLabel("");
        label.setForeground(Color.RED);
        return label;
    }

    private void createFormTexts() {

        exerciseNameField = new JTextField("");
        setsField = new JTextField("");
        repsField = new JTextField("");
        weightField = new JTextField("");
        durationField = new JTextField("1.0");
        distanceField = new JTextField("");
        cardioCheckBox = new JCheckBox();
    }

    private void createFormPanel() {

        createFormTexts();

        setsErrorLabel = createFieldErrorLabel();
        repsErrorLabel = createFieldErrorLabel();
        weightErrorLabel = createFieldErrorLabel();
        durationErrorLabel = createFieldErrorLabel();
        distanceErrorLabel = createFieldErrorLabel();

        formPanel.add(new JLabel("Exercise Name"));
        formPanel.add(exerciseNameField);
        formPanel.add(new JLabel(""));

        formPanel.add(new JLabel("Sets"));
        formPanel.add(setsField);
        formPanel.add(setsErrorLabel);

        formPanel.add(new JLabel("Reps"));
        formPanel.add(repsField);
        formPanel.add(repsErrorLabel);

        formPanel.add(new JLabel("Weight (kg)"));
        formPanel.add(weightField);
        formPanel.add(weightErrorLabel);

        formPanel.add(new JLabel("Duration (min)"));
        formPanel.add(durationField);
        formPanel.add(durationErrorLabel);

        formPanel.add(new JLabel("Distance (km)"));
        formPanel.add(distanceField);
        formPanel.add(distanceErrorLabel);

        formPanel.add(new JLabel("Cardio"));
        formPanel.add(cardioCheckBox);
        formPanel.add(new JLabel(""));
    }

    private void saveExercise() {
        final ExerciseEditorState state = exerciseEditorViewModel.getState();
        final StrengthDetailsDisplayData strengthDetailsDisplayData = new StrengthDetailsDisplayData(
                state.getSets(), state.getReps(), state.getWeight());
        if (state.getEditingExercisePerformedId() == null) {
            addExerciseController.execute(state.getExerciseName(), strengthDetailsDisplayData, state.getIsCardio(),
                    state.getDistanceKm(), state.getDurationMins());
        }
        else {
            editExerciseController.execute(state.getEditingExercisePerformedId(), state.getExerciseName(),
                    strengthDetailsDisplayData, state.getIsCardio(), state.getDistanceKm(),
                    state.getDurationMins());
        }
        exerciseEditorViewModel.firePropertyChanged();
    }

    private void addTextListener(JTextField field, Runnable updater) {

        field.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {
                if (!isUpdatingFromState) {
                    updater.run();
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });
    }

    private void updateState(java.util.function.Consumer<ExerciseEditorState> updater) {
        final ExerciseEditorState state = exerciseEditorViewModel.getState();
        state.setSubmitError("");
        updater.accept(state);
        exerciseEditorViewModel.setState(state);
        exerciseEditorViewModel.firePropertyChanged();
    }

    private void addListeners() {

        addTextListener(exerciseNameField, () -> {
            updateState(state -> {
                state.setExerciseName(exerciseNameField.getText()); }); });

        addTextListener(setsField, () -> {
            final String value = setsField.getText();
            updateState(state -> state.setSets(value));
        });

        addTextListener(repsField, () -> {
            final String value = repsField.getText();
            updateState(state -> state.setReps(value));
        });

        addTextListener(weightField, () -> {
            final String value = weightField.getText();
            updateState(state -> state.setWeight(value));
        });

        addTextListener(durationField, () -> {
            final String value = durationField.getText();
            updateState(state -> state.setDurationMins(value));
        });

        addTextListener(distanceField, () -> {
            final String value = distanceField.getText();
            updateState(state -> state.setDistanceKm(value));
        });

        cardioCheckBox.addActionListener(evt -> {
            if (!isUpdatingFromState) {
                final boolean isCardio = cardioCheckBox.isSelected();
                updateState(state -> state.setIsCardio(isCardio));
            }
        });

    }

    private void updateField(JTextField field, String value) {

        if (!field.hasFocus()) {
            if (!field.getText().equals(value)) {
                field.setText(value);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ExerciseEditorState state = (ExerciseEditorState) evt.getNewValue();

        isUpdatingFromState = true;
        try {
            updateField(exerciseNameField, state.getExerciseName());
            updateField(setsField, state.getSets());
            updateField(repsField, state.getReps());
            updateField(weightField, state.getWeight());
            updateField(durationField, state.getDurationMins());
            updateField(distanceField, state.getDistanceKm());
            if (cardioCheckBox.isSelected() != state.getIsCardio()) {
                cardioCheckBox.setSelected(state.getIsCardio());
            }
        }
        finally {
            isUpdatingFromState = false;
        }
        setsErrorLabel.setText(state.getSetsError());
        repsErrorLabel.setText(state.getRepsError());
        weightErrorLabel.setText(state.getWeightError());
        durationErrorLabel.setText(state.getDurationError());
        distanceErrorLabel.setText(state.getDistanceError());
        submitErrorLabel.setText(state.getSubmitError());
    }

    public void setAddExerciseController(AddExerciseController addExerciseController) {
        this.addExerciseController = addExerciseController;
    }

    public void setEditExerciseController(EditExerciseController editExerciseController) {
        this.editExerciseController = editExerciseController;
    }

    /**
     * Clears any in-progress exercise form state, even when navigating away without saving.
     */
    public void resetState() {
        exerciseEditorViewModel.getState().reset();
        exerciseEditorViewModel.firePropertyChanged();
    }
}
