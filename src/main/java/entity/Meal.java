package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Meal {

    private Integer id;
    private String userId;
    private LocalDate date;
    private String name;
    private final List<FoodEntry> foodEntries = new ArrayList<>();

    public Meal(String userId, LocalDate date, String name) {
        this.userId = userId;
        this.date = date;
        this.name = name;
    }

    public List<FoodEntry> getFoodEntries() {
        return foodEntries;
    }

    /**
     * Remove all foods in the list of foods.
     */
    public void clearFoodEntries() {
        foodEntries.clear();
    }

    /**
     * Replace the whole list of food entries.
     * @param foodEntries the new list of food entries
     */
    public void setFoodEntries(List<FoodEntry> foodEntries) {
        final List<FoodEntry> copy = new ArrayList<>(foodEntries);
        this.foodEntries.clear();
        this.foodEntries.addAll(copy);
    }

    /**
     * Add food to the list of foods.
     * @param foodEntry the food being added to foods
     */
    public void addFoodEntry(FoodEntry foodEntry) {
        foodEntries.add(foodEntry);
    }

    /**
     * Remove food from the list of foods.
     * @param foodEntry the food to remove
     */
    public void removeFoodEntry(FoodEntry foodEntry) {
        foodEntries.remove(foodEntry);
    }

    /**
     * Remove the food entry matching the given id, if present.
     * @param foodId the id of the food entry to remove
     */
    public void removeFoodEntryById(int foodId) {
        foodEntries.removeIf(foodEntry -> foodEntry.getId() == foodId);
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
