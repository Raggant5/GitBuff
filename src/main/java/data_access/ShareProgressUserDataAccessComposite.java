package data_access;

import java.util.List;

import entity.LoggedWorkout;
import entity.User;
import use_case.share.ShareProgressUserDataAccessInterface;

/**
 * Composite DAO wrapping user profile data and logged workout data for sharing.
 */
public class ShareProgressUserDataAccessComposite implements ShareProgressUserDataAccessInterface {

    private final SQLiteUserDataAccessObject userDataAccessObject;
    private final SQLiteWorkoutDataAccessObject workoutDataAccessObject;

    /**
     * Constructs a ShareProgressUserDataAccessComposite instance.
     *
     * @param userDataAccessObject user profile DAO.
     * @param workoutDataAccessObject workout DAO.
     */
    public ShareProgressUserDataAccessComposite(final SQLiteUserDataAccessObject userDataAccessObject,
                                                final SQLiteWorkoutDataAccessObject workoutDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
        this.workoutDataAccessObject = workoutDataAccessObject;
    }

    @Override
    public User getCurrentUser() {
        return this.userDataAccessObject.getCurrentUser();
    }

    @Override
    public int getTotalCompletedWorkouts(final String username) {
        final List<LoggedWorkout> workouts = this.workoutDataAccessObject.getWorkoutsForUser(username);
        return workouts != null ? workouts.size() : 0;
    }

    @Override
    public double getTotalMinutesWorkedOut(final String username) {
        final User user = this.userDataAccessObject.get(username);
        if (user != null) {
            final List<LoggedWorkout> workouts = this.workoutDataAccessObject.getWorkoutsForUser(username);
            double totalMins = 0.0;
            if (workouts != null) {
                for (final LoggedWorkout workout : workouts) {
                    if (workout.getExercises() != null) {
                        for (final var exercise : workout.getExercises()) {
                            totalMins += exercise.getDurationMins();
                        }
                    }
                }
            }
            return totalMins;
        }
        return 0.0;
    }

    @Override
    public List<LoggedWorkout> getWorkoutsForUser(final String username) {
        return this.workoutDataAccessObject.getWorkoutsForUser(username);
    }
}
