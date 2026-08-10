package interface_adapter.log_workout.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Exercise Editor View Model.
 */
class ExerciseEditorViewModelTest {

    @Test
    void constructorSetsViewNameAndInitialState() {
        final ExerciseEditorViewModel viewModel = new ExerciseEditorViewModel();

        assertEquals("exercise editor", viewModel.getViewName());
        assertNotNull(viewModel.getState());
    }
}
