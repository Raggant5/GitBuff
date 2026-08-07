package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoggedWorkout {
    private Integer id;
    private final String userId;
    private LocalDate date;
    private final List<ExercisePerformed> exercises = new ArrayList<>();

    public LoggedWorkout(String userId, LocalDate date) {
        this.id = null;
        this.userId = userId;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<ExercisePerformed> getExercises() {
        return exercises;
    }

    /**
     * Replace the whole list of exercises.
     * @param exercises the new list of exercises;
     */
    public void setExercises(List<ExercisePerformed> exercises) {
        this.exercises.clear();
        this.exercises.addAll(exercises);
    }

    /**
     * Remove exercise from the list of exercises.
     * @param exercise the exercise to remove
     */
    public void removeExercise(ExercisePerformed exercise) {
        this.exercises.remove(exercise);
    }

}
