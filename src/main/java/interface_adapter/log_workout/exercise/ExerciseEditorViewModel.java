package interface_adapter.log_workout.exercise;

import interface_adapter.ViewModel;

/**
 * The View Model for the Add Exercise and Edit Exercise View.
 */
public class ExerciseEditorViewModel extends ViewModel<ExerciseEditorState> {

    public ExerciseEditorViewModel() {
        // viewName not currently used by view manager model due to being built into the WorkoutEditorView
        super("exercise editor");
        setState(new ExerciseEditorState());
    }

}
