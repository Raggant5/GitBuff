package interface_adapter.nutrition.meal.view_meals;

import interface_adapter.ViewModel;

public class ViewMealsViewModel extends ViewModel<ViewMealsState> {
    public ViewMealsViewModel() {
        super("view meals");
        setState(new ViewMealsState());
    }
}
