package interface_adapter.nutrition.food;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import use_case.nutrition.food.change_serving_size.ChangeServingSizeOutputData;

/**
 * Unit tests for the Change Serving Size Presenter.
 */
class ChangeServingSizePresenterTest {

    @Test
    void prepareSuccessViewUpdatesServingDetailsAndRecalculatesTotals() {
        final FoodEditorViewModel foodEditorViewModel = new FoodEditorViewModel();
        final ChangeServingSizePresenter presenter = new ChangeServingSizePresenter(foodEditorViewModel);

        foodEditorViewModel.getState().getServingDetails().setQuantity("2");

        final ChangeServingSizeOutputData outputData = new ChangeServingSizeOutputData(
                FoodUnit.TABLESPOON, 15.0, 30.0, 2.0, 4.0, 1.0);

        presenter.prepareSuccessView(outputData);

        final FoodServingDetails servingDetails = foodEditorViewModel.getState().getServingDetails();
        assertEquals(FoodUnitOption.TABLESPOON, servingDetails.getUnit());
        assertEquals(15.0, servingDetails.getServingGrams());
        assertEquals(30.0, servingDetails.getServingCalories());
        assertEquals(2.0, servingDetails.getServingProtein());
        assertEquals(4.0, servingDetails.getServingCarbs());
        assertEquals(1.0, servingDetails.getServingFat());
        assertEquals("30.0", servingDetails.getTotalGramsDisplay());
        assertEquals("60.0", servingDetails.getTotalCaloriesDisplay());
    }
}
