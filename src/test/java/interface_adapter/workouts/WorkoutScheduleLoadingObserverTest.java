package interface_adapter.workouts;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import use_case.login.LoginOutputData;
import use_case.session.UserLoggedInEvent;

class WorkoutScheduleLoadingObserverTest {

    @Test
    void onUserLoggedInMarksWorkoutsAsLoading() {
        final WorkoutsViewModel workoutsViewModel = new WorkoutsViewModel();
        final WorkoutScheduleLoadingObserver observer = new WorkoutScheduleLoadingObserver(workoutsViewModel);

        final LoginOutputData loginOutputData = new LoginOutputData(
                "amir", 1.8f, 80f, null, null, null, null, null, null, null,
                null, null, null, 45, null, false);

        observer.onUserLoggedIn(new UserLoggedInEvent(loginOutputData));

        assertTrue(workoutsViewModel.getState().isLoading());
    }
}
