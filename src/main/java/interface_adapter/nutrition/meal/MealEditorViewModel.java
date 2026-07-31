package interface_adapter.nutrition.meal;

import interface_adapter.ViewModel;

/**
 * The View Model for the Add Meal View.
 */
public class MealEditorViewModel extends ViewModel<MealEditorState> {

    public MealEditorViewModel() {
        super("meal editor");
        setState(new MealEditorState());
    }

}
