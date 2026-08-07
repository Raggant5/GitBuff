package data_access;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.ExercisePerformed;
import entity.FoodEntry;
import entity.LoggedWorkout;
import entity.Meal;
import entity.User;
import use_case.log_workout.exercise_performed.delete_exercise.DeleteExerciseDataAccessInterface;
import use_case.log_workout.exercise_performed.edit_exercise.EditExerciseDataAccessInterface;
import use_case.log_workout.logged_workout.add_workout.AddWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.delete_workout.DeleteWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.edit_workout.EditWorkoutDataAccessInterface;
import use_case.log_workout.logged_workout.get_workouts.ViewWorkoutDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.nutrition.food.delete_food.DeleteFoodDataAccessInterface;
import use_case.nutrition.food.edit_food.EditFoodDataAccessInterface;
import use_case.nutrition.meal.add_meal.AddMealDataAccessInterface;
import use_case.nutrition.meal.delete_meal.DeleteMealDataAccessInterface;
import use_case.nutrition.meal.edit_meal.EditMealDataAccessInterface;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;
import use_case.profile.ProfileUserDataAccessInterface;
import use_case.recommendation.RecommendationUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * Mock In Memory implementation of the DAO for storing user data.
 * This implementation persists user data in the GitBuff database,
 * allowing users to log in across application sessions.
 */

public class InMemoryDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface, LogoutUserDataAccessInterface, ProfileUserDataAccessInterface,
        RecommendationUserDataAccessInterface, ViewMealDataAccessInterface, AddMealDataAccessInterface,
        EditMealDataAccessInterface, EditFoodDataAccessInterface, DeleteMealDataAccessInterface,
        DeleteFoodDataAccessInterface, ViewWorkoutDataAccessInterface, AddWorkoutDataAccessInterface,
        EditWorkoutDataAccessInterface, EditExerciseDataAccessInterface, DeleteWorkoutDataAccessInterface,
        DeleteExerciseDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();
    private final Map<Integer, Meal> meals = new HashMap<>();
    private final Map<Integer, FoodEntry> foodEntries = new HashMap<>();
    private final Map<Integer, LoggedWorkout> workouts = new HashMap<>();
    private final Map<Integer, ExercisePerformed> exercisesPerformed = new HashMap<>();
    private String currentUsername;
    private int nextMealId = 1;
    private int nextFoodEntryId = 1;
    private int nextWorkoutId = 1;
    private int nextExercisePerformedId = 1;

    @Override
    public boolean existsByName(String identifier) {
        return users.containsKey(identifier);
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        this.currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return this.currentUsername;
    }

    @Override
    public int saveMeal(Meal meal) {
        final int id = nextMealId++;
        meal.setId(id);
        meals.put(id, meal);
        return id;
    }

    @Override
    public int saveFoodEntry(FoodEntry foodEntry) {
        final int id = nextFoodEntryId++;
        foodEntry.setId(id);
        foodEntries.put(id, foodEntry);
        return id;
    }

    @Override
    public List<FoodEntry> getFoodEntriesForMeal(int mealId) {
        final List<FoodEntry> result = new ArrayList<>();
        for (FoodEntry entry : foodEntries.values()) {
            if (entry.getMealId().equals(mealId)) {
                result.add(entry);
            }
        }
        return result;
    }

    @Override
    public List<Meal> getMealsForUser(String userId) {

        final LocalDate cutoff = LocalDate.now().minusDays(6);
        final List<Meal> result = new ArrayList<>();
        for (Meal meal : meals.values()) {
            if (meal.getUserId().equals(userId)
                    && !meal.getDate().isBefore(cutoff)) {
                meal.setFoodEntries(getFoodEntriesForMeal(meal.getId()));
                result.add(meal);
            }
        }
        return result;
    }

    @Override
    public void editMeal(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    @Override
    public void editFoodEntry(FoodEntry foodEntry) {
        foodEntries.put(foodEntry.getId(), foodEntry);
    }

    @Override
    public void deleteMeal(int mealId) {
        meals.remove(mealId);
        foodEntries.values().removeIf(foodEntry -> foodEntry.getMealId().equals(mealId));
    }

    @Override
    public void deleteFoodEntry(int foodEntryId) {
        final FoodEntry entry = foodEntries.remove(foodEntryId);
        if (entry != null) {
            final Meal meal = meals.get(entry.getMealId());
            if (meal != null) {
                meal.removeFoodEntry(entry);
            }
        }
    }

    @Override
    public int saveWorkout(LoggedWorkout workout) {
        final int id = nextWorkoutId++;
        workout.setId(id);
        workouts.put(id, workout);
        return id;
    }

    @Override
    public int saveExercisePerformed(ExercisePerformed exercisePerformed) {
        final int id = nextExercisePerformedId++;
        exercisePerformed.setId(id);
        exercisesPerformed.put(id, exercisePerformed);
        return id;
    }

    @Override
    public List<ExercisePerformed> getExercisesForWorkout(int workoutId) {
        final List<ExercisePerformed> result = new ArrayList<>();
        for (ExercisePerformed exercisePerformed : exercisesPerformed.values()) {
            if (exercisePerformed.getWorkoutId().equals(workoutId)) {
                result.add(exercisePerformed);
            }
        }
        return result;
    }

    @Override
    public List<LoggedWorkout> getWorkoutsForUser(String userId) {

        final LocalDate cutoff = LocalDate.now().minusDays(6);
        final List<LoggedWorkout> result = new ArrayList<>();
        for (LoggedWorkout workout : workouts.values()) {
            if (workout.getUserId().equals(userId)
                    && !workout.getDate().isBefore(cutoff)) {
                workout.setExercises(getExercisesForWorkout(workout.getId()));
                result.add(workout);
            }
        }
        return result;
    }

    @Override
    public void editWorkout(LoggedWorkout workout) {
        workouts.put(workout.getId(), workout);
        for (ExercisePerformed exercise : workout.getExercises()) {
            if (exercise.getWorkoutId() == null) {
                exercise.setWorkoutId(workout.getId());
            }
            if (exercise.getId() == null) {
                saveExercisePerformed(exercise);
            }
            else {
                editExercisePerformed(exercise);
            }
        }
    }

    @Override
    public void editExercisePerformed(ExercisePerformed exercisePerformed) {
        exercisesPerformed.put(exercisePerformed.getId(), exercisePerformed);
    }

    @Override
    public void deleteWorkout(int workoutId) {
        workouts.remove(workoutId);
        exercisesPerformed.values().removeIf(exercisePerformed -> exercisePerformed.getWorkoutId().equals(workoutId));
    }

    @Override
    public void deleteExercisePerformed(int exercisePerformedId) {
        final ExercisePerformed exercisePerformed = exercisesPerformed.remove(exercisePerformedId);
        if (exercisePerformed != null) {
            final LoggedWorkout workout = workouts.get(exercisePerformed.getWorkoutId());
            if (workout != null) {
                workout.removeExercise(exercisePerformed);
            }
        }
    }
}
