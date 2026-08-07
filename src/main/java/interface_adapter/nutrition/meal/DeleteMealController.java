package interface_adapter.nutrition.meal;

import entity.Meal;
import interface_adapter.calendar.CalendarController;
import use_case.nutrition.meal.delete_meal.DeleteMealInputBoundary;
import use_case.nutrition.meal.delete_meal.DeleteMealInputData;

public class DeleteMealController {
    private final DeleteMealInputBoundary deleteMealInputInteractor;
    private final CalendarController calendarController;

    public DeleteMealController(DeleteMealInputBoundary deleteMealInputInteractor,
                                CalendarController calendarController) {
        this.deleteMealInputInteractor = deleteMealInputInteractor;
        this.calendarController = calendarController;
    }

    /**
     * Executes the Delete Meal Use Case.
     * @param meal meal to delete
     */
    public void execute(Meal meal) {
        if (calendarController != null) {
            calendarController.removeMeal(meal);
        }
        deleteMealInputInteractor.execute(new DeleteMealInputData(meal.getId()));
    }

}
