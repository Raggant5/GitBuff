package interface_adapter.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import entity.ActivityLevel;
import entity.FitnessGoal;
import entity.Gender;
import entity.UnitSystem;
import interface_adapter.workouts.WorkoutsViewModel;
import use_case.profile.EditProfileInputBoundary;
import use_case.profile.EditProfileInputData;

/**
 * Unit tests for the Profile Controller. Recommendation refresh after a profile save is no
 * longer this controller's job - {@code use_case.profile.EditProfileInteractor} publishes a
 * {@code ProfileUpdatedEvent} and
 * {@code use_case.profile.RecommendationRefreshOnProfileUpdateObserver} reacts; see
 * {@code EditProfileInteractorTest} for that behavior.
 */
public class ProfileControllerTest {

    private static final float TEST_HEIGHT = 1.8f;
    private static final float TEST_WEIGHT = 80f;
    private static final int TEST_DURATION = 45;
    private static final int AWAIT_SECONDS = 5;

    @Test
    public void executeWithPreBuiltInputDataDelegatesSynchronouslyToInteractor() {
        final FakeEditProfileInteractor interactor = new FakeEditProfileInteractor(null);
        final ProfileController controller = new ProfileController(interactor);

        final EditProfileInputData inputData = buildInputData();

        controller.execute(inputData);

        assertTrue(interactor.executeCalled);
        assertEquals(inputData, interactor.receivedInputData);
    }

    @Test
    public void executeWithPrimitiveArgsEventuallyDelegatesToInteractorInBackground() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final FakeEditProfileInteractor interactor = new FakeEditProfileInteractor(latch);
        final ProfileController controller = new ProfileController(interactor);

        controller.execute(TEST_HEIGHT, TEST_WEIGHT, ActivityLevelOption.VERY_ACTIVE,
                FitnessGoalOption.MUSCLE_AND_STRENGTH_GAIN, "/tmp/pic.png", LocalDate.of(2000, 1, 1),
                GenderOption.MALE, "Hello world", UnitSystemOption.METRIC, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), TEST_DURATION, new HashSet<>());

        if (!latch.await(AWAIT_SECONDS, TimeUnit.SECONDS)) {
            fail("Expected the background worker to invoke the interactor within " + AWAIT_SECONDS + " seconds.");
        }
        assertTrue(interactor.executeCalled);
        assertEquals(TEST_HEIGHT, interactor.receivedInputData.getHeight(), 0.0001);
        assertEquals(TEST_WEIGHT, interactor.receivedInputData.getWeight(), 0.0001);
    }

    @Test
    public void executeWithPrimitiveArgsSetsWorkoutsLoadingImmediately() {
        final FakeEditProfileInteractor interactor = new FakeEditProfileInteractor(new CountDownLatch(1));
        final ProfileController controller = new ProfileController(interactor);
        final WorkoutsViewModel workoutsViewModel = new WorkoutsViewModel();
        controller.setWorkoutsViewModel(workoutsViewModel);

        controller.execute(TEST_HEIGHT, TEST_WEIGHT, ActivityLevelOption.VERY_ACTIVE,
                FitnessGoalOption.MUSCLE_AND_STRENGTH_GAIN, "/tmp/pic.png", LocalDate.of(2000, 1, 1),
                GenderOption.MALE, "Hello world", UnitSystemOption.METRIC, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), TEST_DURATION, new HashSet<>());

        assertTrue(workoutsViewModel.getState().isLoading());
    }

    private EditProfileInputData buildInputData() {
        return new EditProfileInputData.Builder()
                .height(TEST_HEIGHT)
                .weight(TEST_WEIGHT)
                .activityLevel(ActivityLevel.VERY_ACTIVE)
                .goal(FitnessGoal.MUSCLE_AND_STRENGTH_GAIN)
                .profilePicturePath("/tmp/pic.png")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender(Gender.MALE)
                .bio("Hello world")
                .preferredUnitSystem(UnitSystem.METRIC)
                .equipment(new HashSet<>())
                .dietaryRestrictions(new HashSet<>())
                .preferredWorkoutDays(new HashSet<>())
                .preferredWorkoutDurationMinutes(TEST_DURATION)
                .privacySettings(new HashSet<>())
                .build();
    }

    private static final class FakeEditProfileInteractor implements EditProfileInputBoundary {
        private final CountDownLatch latch;
        private boolean executeCalled;
        private EditProfileInputData receivedInputData;

        FakeEditProfileInteractor(final CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void execute(final EditProfileInputData editProfileInputData) {
            this.executeCalled = true;
            this.receivedInputData = editProfileInputData;
            if (this.latch != null) {
                this.latch.countDown();
            }
        }
    }
}
