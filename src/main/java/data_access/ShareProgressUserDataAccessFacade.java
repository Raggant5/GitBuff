package data_access;

import java.util.List;

import entity.LoggedWorkout;
import entity.User;
import use_case.log_workout.logged_workout.get_workouts.ViewWorkoutDataAccessInterface;
import use_case.recommendation.RecommendationUserDataAccessInterface;
import use_case.share.ShareProgressUserDataAccessInterface;

/**
 * Facade combining the user-profile data source and the workout-log data source behind the
 * single {@link ShareProgressUserDataAccessInterface} that the Share Progress feature needs.
 *
 * <p>This class was previously named {@code ShareProgressUserDataAccessComposite}. It does not
 * implement the Composite design pattern - it does not form a recursive tree of
 * interchangeable leaf/composite nodes - it simply unifies two otherwise-unrelated subsystems
 * (user data, workout data) behind one interface, which is the Facade pattern. It has been
 * renamed and corrected accordingly. The project's actual Composite pattern implementation -
 * a genuine recursive component tree - lives in {@code use_case.share.report}
 * ({@link use_case.share.report.ReportSection}, {@link use_case.share.report.CompositeReportSection}
 * and its leaves), which this facade's data feeds into via
 * {@code use_case.share.ShareProgressInteractor}.
 *
 * <p>Unlike the original version, this facade depends only on use case boundary interfaces
 * ({@link RecommendationUserDataAccessInterface} and {@link ViewWorkoutDataAccessInterface}),
 * not on the concrete {@code SQLiteUserDataAccessObject} / {@code SQLiteWorkoutDataAccessObject}
 * classes - satisfying the Dependency Inversion Principle and making this class trivially
 * testable with fakes.
 */
public class ShareProgressUserDataAccessFacade implements ShareProgressUserDataAccessInterface {

    private final RecommendationUserDataAccessInterface userDataAccessObject;
    private final ViewWorkoutDataAccessInterface workoutDataAccessObject;

    /**
     * Constructs a ShareProgressUserDataAccessFacade instance.
     *
     * @param userDataAccessObject data source for user profile lookups
     * @param workoutDataAccessObject data source for logged workouts
     */
    public ShareProgressUserDataAccessFacade(final RecommendationUserDataAccessInterface userDataAccessObject,
                                             final ViewWorkoutDataAccessInterface workoutDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
        this.workoutDataAccessObject = workoutDataAccessObject;
    }

    @Override
    public User getCurrentUser() {
        final String currentUsername = this.userDataAccessObject.getCurrentUsername();
        User currentUser = null;
        if (currentUsername != null) {
            currentUser = this.userDataAccessObject.get(currentUsername);
        }
        return currentUser;
    }

    @Override
    public int getTotalCompletedWorkouts(final String username) {
        final List<LoggedWorkout> workouts = this.workoutDataAccessObject.getWorkoutsForUser(username);
        return workouts != null ? workouts.size() : 0;
    }

    @Override
    public double getTotalMinutesWorkedOut(final String username) {
        double totalMinutes = 0.0;
        if (this.userDataAccessObject.get(username) != null) {
            final List<LoggedWorkout> workouts = this.workoutDataAccessObject.getWorkoutsForUser(username);
            if (workouts != null) {
                for (final LoggedWorkout workout : workouts) {
                    if (workout.getExercises() != null) {
                        totalMinutes += workout.getExercises().stream()
                                .mapToDouble(exercise -> exercise.getDurationMins())
                                .sum();
                    }
                }
            }
        }
        return totalMinutes;
    }

    @Override
    public List<LoggedWorkout> getWorkoutsForUser(final String username) {
        return this.workoutDataAccessObject.getWorkoutsForUser(username);
    }
}

