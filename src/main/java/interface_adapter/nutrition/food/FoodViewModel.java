package interface_adapter.nutrition.food;

import interface_adapter.ViewModel;

/**
 * The View Model for the Add Food View.
 */
public class FoodViewModel extends ViewModel<FoodState> {

    public FoodViewModel() {
        super("add food");
        setState(new FoodState());
    }

}
