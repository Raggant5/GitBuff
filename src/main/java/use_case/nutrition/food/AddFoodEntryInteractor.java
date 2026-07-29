package use_case.nutrition.food;

import entity.FoodEntry;
import entity.FoodEntryFactory;

public class AddFoodEntryInteractor implements AddFoodEntryInputBoundary {

    private final AddFoodEntryOutputBoundary addFoodPresenter;
    private final FoodEntryFactory foodEntryFactory;

    public AddFoodEntryInteractor(AddFoodEntryOutputBoundary addFoodPresenter,
                                  FoodEntryFactory foodEntryFactory) {
        this.addFoodPresenter = addFoodPresenter;
        this.foodEntryFactory = foodEntryFactory;
    }

    @Override
    public void execute(AddFoodEntryInputData inputData) {
        final FoodEntry food = foodEntryFactory.create(
                inputData.getFoodName(), inputData.getFoodNutrition(), inputData.getQuantity(),
                inputData.getUnit(), inputData.getGrams());
        addFoodPresenter.prepareSuccessView(new AddFoodEntryOutputData(food));
    }
}
