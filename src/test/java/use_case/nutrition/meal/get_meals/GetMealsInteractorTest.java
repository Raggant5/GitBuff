package use_case.nutrition.meal.get_meals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import entity.FoodEntry;
import entity.Meal;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;

class GetMealsInteractorTest {

    @Test
    void executeReturnsMealsForUser() {
        final Meal meal = new Meal("amir", LocalDate.of(2026, 8, 6), "Lunch");
        meal.setId(12);
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(List.of(meal));
        final CapturingPresenter presenter = new CapturingPresenter();

        new GetMealsInteractor(presenter, dataAccessObject).execute(new GetMealsInputData("amir"));

        assertEquals(1, presenter.successData.getMeals().size());
        assertEquals("Lunch", presenter.successData.getMeals().get(0).getName());
    }

    @Test
    void executeWhenDataAccessThrowsFails() {
        final FakeDataAccessObject dataAccessObject = new FakeDataAccessObject(List.of()) {
            @Override
            public List<Meal> getMealsForUser(final String userId) {
                throw new DataAccessException("Database unavailable");
            }
        };
        final CapturingPresenter presenter = new CapturingPresenter();

        new GetMealsInteractor(presenter, dataAccessObject).execute(new GetMealsInputData("amir"));

        assertEquals("Unable to load meals. Please try again.", presenter.failMessage);
    }

    private static class FakeDataAccessObject implements ViewMealDataAccessInterface {
        private final List<Meal> meals;

        private FakeDataAccessObject(final List<Meal> meals) {
            this.meals = meals;
        }

        @Override
        public List<Meal> getMealsForUser(final String userId) {
            return meals;
        }

        @Override
        public List<FoodEntry> getFoodEntriesForMeal(final int mealId) {
            return List.of();
        }

        @Override
        public Meal getMealById(final int mealId) {
            final Meal result;
            if (meals.isEmpty()) {
                result = null;
            }
            else {
                result = meals.get(0);
            }
            return result;
        }
    }

    private static final class CapturingPresenter implements GetMealsOutputBoundary {
        private GetMealsOutputData successData;
        private String failMessage;

        @Override
        public void prepareSuccessView(final GetMealsOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(final String errorMessage) {
            this.failMessage = errorMessage;
        }
    }
}
