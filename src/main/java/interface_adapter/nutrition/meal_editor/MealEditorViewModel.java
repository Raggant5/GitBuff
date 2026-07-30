package interface_adapter.nutrition.meal_editor;

import interface_adapter.ViewModel;

/**
 * The View Model for the Add Meal View.
 */
public class MealEditorViewModel extends ViewModel<MealEditorState> {

    public MealEditorViewModel() {
        super("add meal");
        setState(new MealEditorState());
    }

}
