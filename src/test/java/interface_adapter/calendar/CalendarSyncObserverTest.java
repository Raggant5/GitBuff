package interface_adapter.calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import entity.Meal;
import interface_adapter.login.LoginViewModel;
import interface_adapter.nutrition.meal.MealDisplayData;
import interface_adapter.session.UserLoggedInEvent;
import use_case.login.LoginOutputData;

class CalendarSyncObserverTest {

    @Test
    void loginLoadsCalendarAndSynchronizesSavedMeals() {
        final RecordingCalendarController controller = new RecordingCalendarController();
        final CalendarSyncObserver observer = new CalendarSyncObserver(controller);
        final Meal meal = new Meal("amir", LocalDate.of(2026, 8, 9), "Lunch");
        meal.setId(12);
        final LoginOutputData loginOutput = new LoginOutputData(
                "amir", 0, 0, null, null, null, null, null, null, null,
                Set.of(), Set.of(), Set.of(), 0, Set.of(), List.of(meal),
                List.of(), false);

        observer.onUserLoggedIn(new UserLoggedInEvent(loginOutput));

        assertEquals(1, controller.loadCalls);
        assertEquals(1, controller.synchronizedMeals.size());
        assertEquals(12, controller.synchronizedMeals.get(0).getId());
        assertEquals("Lunch", controller.synchronizedMeals.get(0).getName());
    }

    private static final class RecordingCalendarController extends CalendarController {
        private int loadCalls;
        private List<MealDisplayData> synchronizedMeals = new ArrayList<>();

        RecordingCalendarController() {
            super(inputData -> { }, inputData -> { }, inputData -> { },
                    new LoginViewModel(), new CalendarViewModel());
        }

        @Override
        public void loadCalendarEvents() {
            this.loadCalls++;
        }

        @Override
        public void synchronizeMeals(List<MealDisplayData> meals) {
            this.synchronizedMeals = List.copyOf(meals);
        }
    }
}
