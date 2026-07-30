package use_case.nutrition.food.edit_food;

import entity.FoodEntry;
import entity.FoodNutrition;

public class EditFoodInteractor implements EditFoodInputBoundary {

    private final EditFoodOutputBoundary presenter;
    private final EditFoodDataAccessInterface dataAccess;

    public EditFoodInteractor(EditFoodOutputBoundary presenter, EditFoodDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(EditFoodInputData inputData) {
        final FoodEntry food = inputData.getFoodEntry();
        food.setFoodName(inputData.getFoodName());
        final FoodNutrition nutrition = inputData.getNutrition();
        food.setNutrition(nutrition);
        food.setQuantity(inputData.getQuantity());
        food.setUnit(inputData.getUnit());
        food.setGrams(inputData.getGrams());

        dataAccess.editFoodEntry(food);

        presenter.prepareSuccessView(new EditFoodOutputData(food));
    }
}
