package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import use_case.nutrition.meal.delete_meal.DeleteMealOutputData;

/**
 * Unit tests for the Delete Meal Presenter.
 */
class DeleteMealPresenterTest {

    @Test
    void prepareSuccessViewRemovesMatchingMealAndClearsError() {
        final ViewMealsViewModel viewMealsViewModel = new ViewMealsViewModel();
        viewMealsViewModel.getState().setError("stale error");
        viewMealsViewModel.getState().addMeal(buildMeal(1, "Lunch"));
        viewMealsViewModel.getState().addMeal(buildMeal(2, "Dinner"));

        final DeleteMealPresenter presenter = new DeleteMealPresenter(viewMealsViewModel);

        presenter.prepareSuccessView(new DeleteMealOutputData(1));

        final ViewMealsState state = viewMealsViewModel.getState();
        assertEquals("", state.getError());
        assertEquals(1, state.getMeals().size());
        assertEquals(2, state.getMeals().get(0).getId());
    }

    @Test
    void prepareFailViewSetsErrorMessage() {
        final ViewMealsViewModel viewMealsViewModel = new ViewMealsViewModel();
        final DeleteMealPresenter presenter = new DeleteMealPresenter(viewMealsViewModel);

        presenter.prepareFailView("Meal not found.");

        assertEquals("Meal not found.", viewMealsViewModel.getState().getError());
    }

    private MealDisplayData buildMeal(final int id, final String name) {
        return new MealDisplayData(id, LocalDate.now(), name, new ArrayList<>());
    }
}
