package use_case.nutrition.meal.delete_meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.FoodEntry;
import entity.Meal;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;
import use_case.EventPublisher;
import use_case.nutrition.meal.MealChangedEvent;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;

public class DeleteMealInteractorTest {

    @Test
    public void executeDeletesMealAndNotifiesPresenter() {
        final List<Integer> deletedIds = new ArrayList<>();
        final DeleteMealDataAccessInterface dataAccess = mealId -> deletedIds.add(mealId);
        final Meal existingMeal = new Meal("amir", LocalDate.now(), "Dinner");
        existingMeal.setId(6);
        final FakeViewMealDataAccessObject viewDataAccess = new FakeViewMealDataAccessObject(existingMeal);
        final DeleteMealOutputData[] captured = new DeleteMealOutputData[1];
        final DeleteMealOutputBoundary presenter = new DeleteMealOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteMealOutputData outputData) {
                captured[0] = outputData;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                throw new AssertionError("Expected success view, got failure: " + errorMessage);
            }
        };
        final FakeMealEventPublisher eventPublisher = new FakeMealEventPublisher();

        new DeleteMealInteractor(presenter, dataAccess, viewDataAccess, eventPublisher)
                .execute(new DeleteMealInputData(6));

        assertTrue(deletedIds.contains(6));
        assertEquals(6, captured[0].getMealId());
        assertEquals(1, eventPublisher.publishedEvents.size());
        assertEquals("amir", eventPublisher.publishedEvents.get(0).getUserId());
    }

    @Test
    public void executeWhenDataAccessThrowsPreparesFailView() {
        final DeleteMealDataAccessInterface failingDataAccess = mealId -> {
            throw new DataAccessException("db unavailable");
        };
        final Meal existingMeal = new Meal("amir", LocalDate.now(), "Dinner");
        existingMeal.setId(6);
        final FakeViewMealDataAccessObject viewDataAccess = new FakeViewMealDataAccessObject(existingMeal);
        final boolean[] failed = {false};
        final DeleteMealOutputBoundary presenter = new DeleteMealOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteMealOutputData outputData) {
                throw new AssertionError("Expected failure view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failed[0] = true;
                assertFalse(errorMessage.isEmpty());
            }
        };

        new DeleteMealInteractor(presenter, failingDataAccess, viewDataAccess, new FakeMealEventPublisher())
                .execute(new DeleteMealInputData(6));

        assertTrue(failed[0]);
    }

    private static final class FakeViewMealDataAccessObject implements ViewMealDataAccessInterface {
        private final Meal meal;

        private FakeViewMealDataAccessObject(Meal meal) {
            this.meal = meal;
        }

        @Override
        public List<Meal> getMealsForUser(String userId) {
            return List.of(meal);
        }

        @Override
        public List<FoodEntry> getFoodEntriesForMeal(int mealId) {
            return List.of();
        }

        @Override
        public Meal getMealById(int mealId) {
            return meal;
        }
    }

    private static final class FakeMealEventPublisher implements EventPublisher<MealChangedEvent> {
        private final List<MealChangedEvent> publishedEvents = new ArrayList<>();

        @Override
        public void publish(MealChangedEvent event) {
            publishedEvents.add(event);
        }
    }
}
