package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Food Editor View Model.
 */
class FoodEditorViewModelTest {

    @Test
    void constructorInitializesViewNameAndState() {
        final FoodEditorViewModel viewModel = new FoodEditorViewModel();

        assertEquals("food editor", viewModel.getViewName());
        assertNotNull(viewModel.getState());
    }
}
