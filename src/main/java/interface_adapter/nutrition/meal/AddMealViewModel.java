package interface_adapter.nutrition.meal;

import interface_adapter.ViewModel;

/**
 * The View Model for the Add Meal View.
 */
public class AddMealViewModel extends ViewModel<MealState> {

    public AddMealViewModel() {
        super("add meal");
        setState(new MealState());
    }

}
