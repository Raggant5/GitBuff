package interface_adapter.nutrition.food;

import interface_adapter.ViewModel;

/**
 * The View Model for the Add Food and Edit Food View.
 */
public class FoodEditorViewModel extends ViewModel<FoodEditorState> {

    public FoodEditorViewModel() {
        // viewName not currently used by view manager model due to being built into the MealEditorView
        super("food editor");
        setState(new FoodEditorState());
    }

}
