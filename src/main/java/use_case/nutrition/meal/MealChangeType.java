package use_case.nutrition.meal;

/**
 * What happened to a meal, carried by MealChangedEvent so observers can react appropriately
 * (e.g. add vs. remove a calendar event).
 */
public enum MealChangeType {
    ADDED,
    EDITED,
    DELETED
}
