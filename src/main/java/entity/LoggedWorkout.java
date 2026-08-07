package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a workout completed and logged by a user.
 */
public class LoggedWorkout {

    private Integer id;
    private final String userId;
    private LocalDate date;

    private final List<ExercisePerformed> exercises =
            new ArrayList<>();

    /**
     * Constructs a newly logged workout.
     *
     * @param userId user who completed the workout
     * @param date date the workout was completed
     */
    public LoggedWorkout(
            final String userId,
            final LocalDate date
    ) {
        this.id = null;
        this.userId = userId;
        this.date = date;
    }

    /**
     * Constructs a workout loaded from persistence.
     *
     * @param id database workout ID
     * @param userId user who completed the workout
     * @param date date the workout was completed
     */
    public LoggedWorkout(
            final Integer id,
            final String userId,
            final LocalDate date
    ) {
        this.id = id;
        this.userId = userId;
        this.date = date;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }

    public List<ExercisePerformed> getExercises() {
        return this.exercises;
    }

    /**
     * Replaces the whole list of exercises.
     *
     * @param exercises new list of exercises
     */
    public void setExercises(
            final List<ExercisePerformed> exercises
    ) {
        this.exercises.clear();
        this.exercises.addAll(exercises);
    }

    /**
     * Adds an exercise to this workout.
     *
     * @param exercise exercise to add
     */
    public void addExercise(
            final ExercisePerformed exercise
    ) {
        this.exercises.add(exercise);
    }

    /**
     * Removes an exercise from this workout.
     *
     * @param exercise exercise to remove
     */
    public void removeExercise(
            final ExercisePerformed exercise
    ) {
        this.exercises.remove(exercise);
    }
}