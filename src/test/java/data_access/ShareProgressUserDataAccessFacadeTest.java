package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import entity.CommonUser;
import entity.ExercisePerformed;
import entity.LoggedWorkout;
import entity.User;
import use_case.log_workout.logged_workout.get_workouts.ViewWorkoutDataAccessInterface;
import use_case.recommendation.RecommendationUserDataAccessInterface;

class ShareProgressUserDataAccessFacadeTest {

    private static final double FIRST_DURATION = 30.0;
    private static final double SECOND_DURATION = 15.0;

    @Test
    void getCurrentUserReturnsNullWhenNoUserLoggedIn() {
        final FakeUserDao userDao = new FakeUserDao(null);
        final FakeWorkoutDao workoutDao = new FakeWorkoutDao();
        final ShareProgressUserDataAccessFacade facade =
                new ShareProgressUserDataAccessFacade(userDao, workoutDao);

        assertNull(facade.getCurrentUser());
    }

    @Test
    void getCurrentUserReturnsUserWhenLoggedIn() {
        final User user = new CommonUser("aahir", "password");
        final FakeUserDao userDao = new FakeUserDao("aahir");
        userDao.save(user);
        final FakeWorkoutDao workoutDao = new FakeWorkoutDao();
        final ShareProgressUserDataAccessFacade facade =
                new ShareProgressUserDataAccessFacade(userDao, workoutDao);

        assertEquals(user, facade.getCurrentUser());
    }

    @Test
    void getTotalCompletedWorkoutsReturnsZeroWhenNoWorkouts() {
        final FakeUserDao userDao = new FakeUserDao(null);
        final FakeWorkoutDao workoutDao = new FakeWorkoutDao();
        final ShareProgressUserDataAccessFacade facade =
                new ShareProgressUserDataAccessFacade(userDao, workoutDao);

        assertEquals(0, facade.getTotalCompletedWorkouts("aahir"));
    }

    @Test
    void getTotalCompletedWorkoutsCountsWorkoutList() {
        final FakeUserDao userDao = new FakeUserDao(null);
        final FakeWorkoutDao workoutDao = new FakeWorkoutDao();
        workoutDao.setWorkouts("aahir", List.of(
                new LoggedWorkout("aahir", LocalDate.of(2026, 1, 1))));
        final ShareProgressUserDataAccessFacade facade =
                new ShareProgressUserDataAccessFacade(userDao, workoutDao);

        assertEquals(1, facade.getTotalCompletedWorkouts("aahir"));
    }

    @Test
    void getTotalMinutesWorkedOutReturnsZeroWhenUserUnknown() {
        final FakeUserDao userDao = new FakeUserDao(null);
        final FakeWorkoutDao workoutDao = new FakeWorkoutDao();
        final ShareProgressUserDataAccessFacade facade =
                new ShareProgressUserDataAccessFacade(userDao, workoutDao);

        assertEquals(0.0, facade.getTotalMinutesWorkedOut("aahir"));
    }

    @Test
    void getTotalMinutesWorkedOutSumsExerciseDurationsAcrossWorkouts() {
        final FakeUserDao userDao = new FakeUserDao(null);
        userDao.save(new CommonUser("aahir", "password"));
        final FakeWorkoutDao workoutDao = new FakeWorkoutDao();

        final LoggedWorkout workoutWithExercises = new LoggedWorkout("aahir", LocalDate.of(2026, 1, 1));
        workoutWithExercises.getExercises().add(
                new ExercisePerformed("Push-Ups", null, FIRST_DURATION, null, false));
        workoutWithExercises.getExercises().add(
                new ExercisePerformed("Squats", null, SECOND_DURATION, null, false));
        final LoggedWorkout workoutWithoutExercises = new LoggedWorkout("aahir", LocalDate.of(2026, 1, 2));

        workoutDao.setWorkouts("aahir", List.of(workoutWithExercises, workoutWithoutExercises));
        final ShareProgressUserDataAccessFacade facade =
                new ShareProgressUserDataAccessFacade(userDao, workoutDao);

        assertEquals(FIRST_DURATION + SECOND_DURATION, facade.getTotalMinutesWorkedOut("aahir"));
    }

    @Test
    void getWorkoutsForUserDelegatesToWorkoutDao() {
        final FakeUserDao userDao = new FakeUserDao(null);
        final FakeWorkoutDao workoutDao = new FakeWorkoutDao();
        final List<LoggedWorkout> workouts = List.of(new LoggedWorkout("aahir", LocalDate.of(2026, 1, 1)));
        workoutDao.setWorkouts("aahir", workouts);
        final ShareProgressUserDataAccessFacade facade =
                new ShareProgressUserDataAccessFacade(userDao, workoutDao);

        assertEquals(workouts, facade.getWorkoutsForUser("aahir"));
    }

    private static final class FakeUserDao implements RecommendationUserDataAccessInterface {
        private final Map<String, User> users = new HashMap<>();
        private final String currentUsername;

        FakeUserDao(final String currentUsername) {
            this.currentUsername = currentUsername;
        }

        void save(final User user) {
            this.users.put(user.getName(), user);
        }

        @Override
        public User get(final String username) {
            return this.users.get(username);
        }

        @Override
        public String getCurrentUsername() {
            return this.currentUsername;
        }
    }

    private static final class FakeWorkoutDao implements ViewWorkoutDataAccessInterface {
        private final Map<String, List<LoggedWorkout>> workoutsByUser = new HashMap<>();

        void setWorkouts(final String userId, final List<LoggedWorkout> workouts) {
            this.workoutsByUser.put(userId, workouts);
        }

        @Override
        public List<LoggedWorkout> getWorkoutsForUser(final String userId) {
            return this.workoutsByUser.get(userId);
        }

        @Override
        public List<ExercisePerformed> getExercisesForWorkout(final int workoutId) {
            return new ArrayList<>();
        }

        @Override
        public LoggedWorkout getWorkoutById(final int workoutId) {
            return null;
        }
    }
}
