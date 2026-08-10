package interface_adapter.nutrition.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.FoodUnit;
import interface_adapter.MainViewManagerModel;
import interface_adapter.nutrition.food.FoodUnitOption;
import use_case.nutrition.food.FoodEntryData;
import use_case.nutrition.food.FoodNutritionData;
import use_case.nutrition.meal.edit_meal.EditMealOutputData;

/**
 * Unit tests for the Edit Meal Presenter.
 */
class EditMealPresenterTest {

    private static final int MEAL_ID = 5;

    @Test
    void prepareSuccessViewReplacesMealResetsEditorAndSwitchesToNutritionView() {
        final ViewMealsViewModel viewMealsViewModel = new ViewMealsViewModel();
        viewMealsViewModel.getState().addMeal(
                new MealDisplayData(MEAL_ID, LocalDate.of(2024, 1, 1), "Old", new ArrayList<>()));
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        mealEditorViewModel.getState().setName("editing");
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();

        final EditMealPresenter presenter =
                new EditMealPresenter(viewMealsViewModel, mealEditorViewModel, mainViewManagerModel);

        final FoodNutritionData nutrition = new FoodNutritionData(100, 10, 20, 5);
        final FoodEntryData food = new FoodEntryData(1, "Egg", nutrition, 2, FoodUnit.GRAM, 100);
        final EditMealOutputData outputData =
                new EditMealOutputData(MEAL_ID, LocalDate.of(2024, 2, 2), "New", List.of(food));

        presenter.prepareSuccessView(outputData);

        final ViewMealsState viewMealsState = viewMealsViewModel.getState();
        assertEquals(1, viewMealsState.getMeals().size());
        final MealDisplayData updatedMeal = viewMealsState.getMeals().get(0);
        assertEquals("New", updatedMeal.getName());
        assertEquals(LocalDate.of(2024, 2, 2), updatedMeal.getDate());
        assertEquals(1, updatedMeal.getFoodEntries().size());
        assertEquals("Egg", updatedMeal.getFoodEntries().get(0).getFoodName());
        assertEquals(FoodUnitOption.GRAM, updatedMeal.getFoodEntries().get(0).getUnit());

        assertEquals("", mealEditorViewModel.getState().getName());
        assertEquals("nutrition", mainViewManagerModel.getState());
    }

    @Test
    void prepareFailViewSetsErrorMessageOnEditorState() {
        final ViewMealsViewModel viewMealsViewModel = new ViewMealsViewModel();
        final MealEditorViewModel mealEditorViewModel = new MealEditorViewModel();
        final MainViewManagerModel mainViewManagerModel = new MainViewManagerModel();
        final EditMealPresenter presenter =
                new EditMealPresenter(viewMealsViewModel, mealEditorViewModel, mainViewManagerModel);

        presenter.prepareFailView("Invalid meal name.");

        assertEquals("Invalid meal name.", mealEditorViewModel.getState().getErrorMessage());
    }
}
