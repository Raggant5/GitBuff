package interface_adapter.nutrition.meal;

import interface_adapter.ViewModel;

public class ViewMealsViewModel extends ViewModel<ViewMealsState> {
    public ViewMealsViewModel() {
        super("view meals");
        setState(new ViewMealsState());
    }
}
