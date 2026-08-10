package use_case.nutrition.meal.delete_meal;

import entity.Meal;
import use_case.DataAccessException;
import use_case.EventPublisher;
import use_case.nutrition.meal.MealChangeType;
import use_case.nutrition.meal.MealChangedEvent;
import use_case.nutrition.meal.get_meals.ViewMealDataAccessInterface;

public class DeleteMealInteractor implements DeleteMealInputBoundary {

    private final DeleteMealOutputBoundary deleteMealPresenter;
    private final DeleteMealDataAccessInterface mealDataAccessObject;
    private final ViewMealDataAccessInterface viewMealDataAccessObject;
    private final EventPublisher<MealChangedEvent> mealEventPublisher;

    public DeleteMealInteractor(DeleteMealOutputBoundary deleteMealPresenter,
                                DeleteMealDataAccessInterface mealDataAccessObject,
                                ViewMealDataAccessInterface viewMealDataAccessObject,
                                EventPublisher<MealChangedEvent> mealEventPublisher) {
        this.deleteMealPresenter = deleteMealPresenter;
        this.mealDataAccessObject = mealDataAccessObject;
        this.viewMealDataAccessObject = viewMealDataAccessObject;
        this.mealEventPublisher = mealEventPublisher;
    }

    @Override
    public void execute(DeleteMealInputData deleteMealInputData) {
        try {
            final Meal meal = this.viewMealDataAccessObject.getMealById(deleteMealInputData.getMealId());
            mealDataAccessObject.deleteMeal(deleteMealInputData.getMealId());
            deleteMealPresenter.prepareSuccessView(new DeleteMealOutputData(deleteMealInputData.getMealId()));
            if (meal != null) {
                this.mealEventPublisher.publish(new MealChangedEvent(
                        meal.getUserId(), deleteMealInputData.getMealId(), null, null, MealChangeType.DELETED));
            }
        }
        catch (DataAccessException exc) {
            deleteMealPresenter.prepareFailView("Unable to delete meal. Please try again.");
        }
    }
}
