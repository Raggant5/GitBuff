package data_access;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.FoodEntry;
import entity.Meal;
import entity.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.nutrition.meal.AddMealDataAccessInterface;
import use_case.nutrition.meal.ViewNutritionDataAccessInterface;
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
        RecommendationUserDataAccessInterface, ViewNutritionDataAccessInterface, AddMealDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();
    private final Map<Integer, Meal> meals = new HashMap<>();
    private final Map<Integer, FoodEntry> foodEntries = new HashMap<>();
    private String currentUsername;
    private int nextMealId = 1;
    private int nextFoodEntryId = 1;

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
                for (FoodEntry foodEntry : foodEntries.values()) {
                    if (foodEntry.getMealId().equals(meal.getId())) {
                        meal.addFoodEntry(foodEntry);
                    }
                }
                result.add(meal);
            }
        }
        return result;
    }
}
